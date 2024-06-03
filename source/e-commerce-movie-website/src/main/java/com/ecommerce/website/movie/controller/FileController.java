package com.ecommerce.website.movie.controller;

import com.ecommerce.website.movie.dto.ApiResponseDto;
import com.ecommerce.website.movie.dto.UploadFileDto;
import com.ecommerce.website.movie.dto.aws.FileS3Dto;
import com.ecommerce.website.movie.form.UploadFileForm;
import com.ecommerce.website.movie.service.MovieService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.*;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/v1/file")
@CrossOrigin(origins = "*", allowedHeaders = "*")
@Slf4j

public class FileController {
    @Autowired
    MovieService movieService;

    @PostMapping(value = "/upload/s3", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiResponseDto<UploadFileDto> upload(@Valid UploadFileForm uploadFileForm, BindingResult bindingResult) {
        return movieService.uploadFileS3(uploadFileForm);
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

    @DeleteMapping(value = "/delete/s3/{fileName}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> deleteFile(@PathVariable String fileName) {
        movieService.deleteFileS3(fileName);
        return ResponseEntity.ok("File deleted successfully");
    }
}
