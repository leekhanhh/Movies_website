package com.ecommerce.website.movie.controller;

import com.ecommerce.website.movie.dto.ApiResponseDto;
import com.ecommerce.website.movie.dto.ErrorCode;
import com.ecommerce.website.movie.dto.UploadFileDto;
import com.ecommerce.website.movie.dto.UploadVideoDto;
import com.ecommerce.website.movie.dto.aws.FileS3Dto;
import com.ecommerce.website.movie.form.DeleteFileForm;
import com.ecommerce.website.movie.form.UploadFileForm;
import com.ecommerce.website.movie.model.Movie;
import com.ecommerce.website.movie.repository.MovieRepository;
import com.ecommerce.website.movie.service.MovieService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.*;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.io.File;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/v1/file")
@CrossOrigin(origins = "*", allowedHeaders = "*")
@Slf4j

public class FileController {
    @Autowired
    MovieService movieService;
    @Autowired
    MovieRepository movieRepository;

    @PostMapping(value = "/upload-file/s3", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiResponseDto<UploadFileDto> upload(@Valid UploadFileForm uploadFileForm, BindingResult bindingResult) {
        return movieService.uploadFileS3(uploadFileForm);
    }

    @PostMapping(value = "/upload-video/s3", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiResponseDto<UploadVideoDto> uploadVideo(@RequestParam("file") MultipartFile file, @RequestParam("bandwidth") String bandwidth) {
        return movieService.uploadVideoS3(file, bandwidth);
    }

    @GetMapping(value = "/load/s3/{fileName:.+}")
    public ResponseEntity<?> loadFileS3(@PathVariable String fileName) {
        FileS3Dto fileS3Dto = movieService.loadFileAsResource(fileName);
        if (fileS3Dto == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok()
                .contentLength(fileS3Dto.getFileByte().length)
                .contentType(MediaType.parseMediaType(fileS3Dto.getFileType()))
                .cacheControl(CacheControl.maxAge(7776000, TimeUnit.SECONDS))
                .body(fileS3Dto.getFileByte());
    }

    @GetMapping(value = "/download/s3/{fileName:.+}")
    public ResponseEntity<ByteArrayResource> downloadFileS3(@PathVariable String fileName) {
        byte[] data = movieService.loadFileAsResource(fileName).getFileByte();
        if (data == null) {
            return ResponseEntity.notFound().build();
        }
        ByteArrayResource resource = new ByteArrayResource(data);
        return ResponseEntity.ok()
                .contentLength(data.length)
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileName + "\"")
                .cacheControl(CacheControl.maxAge(7776000, TimeUnit.SECONDS))
                .body(resource);
    }

    @DeleteMapping(value = "/delete/s3") 
    public ApiResponseDto<String> deleteFile(@Valid @RequestBody DeleteFileForm deleteFileForm, BindingResult bindingResult) {
        ApiResponseDto<String> apiResponseDto = new ApiResponseDto<>();
        movieService.deleteFileS3(deleteFileForm.getFileName());
        apiResponseDto.setMessage("File deleted successfully by key - " + deleteFileForm.getFileName());
        return  apiResponseDto;
    }

}
