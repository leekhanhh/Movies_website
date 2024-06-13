package com.ecommerce.website.movie.service;

import com.amazonaws.HttpMethod;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.GeneratePresignedUrlRequest;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.util.IOUtils;
import com.ecommerce.website.movie.dto.ApiResponseDto;
import com.ecommerce.website.movie.dto.UploadFileDto;
import com.ecommerce.website.movie.dto.UploadVideoDto;
import com.ecommerce.website.movie.dto.aws.FileS3Dto;
import com.ecommerce.website.movie.form.UploadFileForm;
import com.ecommerce.website.movie.service.aws.S3Service;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.RandomStringUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.annotation.Autowired;
import org.apache.commons.io.FilenameUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

@Service
@Slf4j
public class MovieService {
    private static final String[] UPLOAD_TYPES = new String[]{"LOGO", "AVATAR", "IMAGE", "DOCUMENT"};
    @Value("${cloud.aws.s3.bucket}")
    private String bucketName;
    @Value("${cloud.aws.s3.endpoint.url}")
    private String endpointUrl;
    @Value("${cloud.aws.region.static}")
    private String region;
    @Autowired
    private S3Service s3Service;

    private final AmazonS3 s3Client;

    public MovieService(AmazonS3 s3Client) {
        this.s3Client = s3Client;
    }

    public ApiResponseDto<UploadFileDto> uploadFileS3(UploadFileForm uploadFileForm) {
        ApiResponseDto<UploadFileDto> apiMessageDto = new ApiResponseDto<>();
        boolean contains = Arrays.stream(UPLOAD_TYPES).anyMatch(uploadFileForm.getType()::equalsIgnoreCase);
        if (!contains) {
            apiMessageDto.setResult(false);
            apiMessageDto.setMessage("[AWS S3] Type is required in AVATAR or IMAGE");
            return apiMessageDto;
        }
        uploadFileForm.setType(uploadFileForm.getType().toUpperCase());
        String fileName = StringUtils.cleanPath(Objects.requireNonNull(uploadFileForm.getFile().getOriginalFilename()));
        String ext = FilenameUtils.getExtension(fileName);
        String finalFile = uploadFileForm.getType() + "_" + RandomStringUtils.randomAlphanumeric(10) + "." + ext;
        String fileUrl = "https://" + bucketName + ".s3." + region + ".amazonaws.com/" + finalFile;
        UploadFileDto uploadFileDto = new UploadFileDto();
        uploadFileDto.setFilePath(fileUrl);
        apiMessageDto.setData(uploadFileDto);
        apiMessageDto.setMessage(s3Service.uploadFile(uploadFileForm.getFile(), finalFile));
        return apiMessageDto;
    }

    public ApiResponseDto<UploadVideoDto> uploadVideoS3(MultipartFile file, String bandwidth) {
        ApiResponseDto<UploadVideoDto> apiResponseDto = new ApiResponseDto<>();
        String fileUrl = s3Service.uploadVideo(file, bandwidth);
        Long fileSize = file.getSize();

        UploadVideoDto uploadVideoDto = new UploadVideoDto();
        uploadVideoDto.setSize(fileSize);
        uploadVideoDto.setVideoPath(fileUrl);
        apiResponseDto.setData(uploadVideoDto);
        apiResponseDto.setMessage("Upload successful");
        return apiResponseDto;
    }
    public FileS3Dto loadFileAsResource(String fileName) {
        return s3Service.downloadFile(fileName);
    }

    public void deleteVideoS3ByLink(String videoPath) {
        String awsEndpoint = "https://movies-website-tlcn-project.s3.ap-southeast-1.amazonaws.com/";
        if (!videoPath.contains(awsEndpoint)) {
            log.error("[AWS S3] Video not found!");
            return;
        }
        String key = videoPath.replace(awsEndpoint, "");
        s3Service.deleteVideo(key);
    }

    public void deleteFileS3ByLink(String avatarPath) {
        String awsEndpoint = endpointUrl;
        if (!avatarPath.contains(awsEndpoint)) {
            log.error("[AWS S3] File not found!");
            return;
        }
        String key = avatarPath.replace(awsEndpoint, "");
        s3Service.deleteFile(key);
    }
    public void deleteFileS3(String avatarPath) {
        s3Service.deleteFile(avatarPath);
    }

    public ApiResponseDto<String> deleteVideoS3(String fileName) {
        ApiResponseDto<String> apiResponseDto = new ApiResponseDto<>();
        String message = s3Service.deleteVideo(fileName);
        apiResponseDto.setMessage(message);
        return apiResponseDto;
    }

    public Long takeDuration(String source) throws IOException, InterruptedException {
        List<String> commands = new ArrayList<>();
        commands.add("ffmpeg");
        commands.add("-i");
        commands.add(source);

        Process process = new ProcessBuilder(commands)
                .redirectErrorStream(true)
                .start();

        long result = 0;
        // Read the output of the command
        BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
        String line;
        while ((line = reader.readLine()) != null) {
            if (line.contains("Duration")) {
                // Extract and print the duration information
                result =  extractDurationInSeconds(line);
                log.debug("Video Duration: " + result);
                break;
            }
        }
        process.waitFor();
        //startInputAndErrorThreads(process);
        return result;
    }

    private static long extractDurationInSeconds(String line) {
        // Assuming the duration information is in the format "Duration: HH:MM:SS.ss,"
        String[] parts = line.split(",")[0].split(":");
        int hours = Integer.parseInt(parts[1].trim());
        int minutes = Integer.parseInt(parts[2].trim());
        double seconds = Double.parseDouble(parts[3].trim());

        // Convert to total seconds
        return (long) (hours * 3600L + minutes * 60L + seconds);
    }

    public static File downloadVideo(String videoUrl) throws IOException {
        URL url = new URL(videoUrl);
        File tempFile = File.createTempFile("video", ".mp4");
        FileUtils.copyURLToFile(url, tempFile);
        return tempFile;
    }
}