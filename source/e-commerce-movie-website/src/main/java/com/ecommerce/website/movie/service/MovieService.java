package com.ecommerce.website.movie.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectInputStream;
import com.ecommerce.website.movie.constant.Constant;
import com.ecommerce.website.movie.controller.WatchedMovieController;
import com.ecommerce.website.movie.dto.ApiResponseDto;
import com.ecommerce.website.movie.dto.UploadFileDto;
import com.ecommerce.website.movie.dto.UploadVideoDto;
import com.ecommerce.website.movie.dto.aws.FileS3Dto;
import com.ecommerce.website.movie.form.UploadFileForm;
import com.ecommerce.website.movie.form.movie.watchedmovie.CreateWatchedMovieForm;
import com.ecommerce.website.movie.service.aws.S3Service;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.RandomStringUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.annotation.Autowired;
import org.apache.commons.io.FilenameUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.concurrent.CompletableFuture;

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
    @Autowired
    WatchedMovieController watchedMovieController;
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

    private String getFilePath(String fileName) {
        S3Object s3Object = s3Client.getObject(bucketName, fileName);
        URL url = null;
        try {
            url = s3Object.getObjectContent().getHttpRequest().getURI().toURL();
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (url != null) {
            return new File(url.getFile()).getAbsolutePath();
        } else {
            throw new RuntimeException("Cannot retrieve file URL from S3 for " + fileName);
        }
    }

    private Long sizeFromFile(Path path) {
        try {
            return Files.size(path);
        } catch (IOException ioException) {
            log.error("Error while getting the file size", ioException);
        }
        return 0L;
    }

    private byte[] readByteRange(String key, long start, long end) throws IOException {
        GetObjectRequest rangeObjectRequest = new GetObjectRequest(bucketName, key)
                .withRange(start, end);
        S3Object objectPortion = s3Client.getObject(rangeObjectRequest);
        return objectPortion.getObjectContent().readAllBytes();
    }

    public byte[] readByteRangeNew(String filename, long start, long end) throws IOException {
        GetObjectRequest getObjectRequest = new GetObjectRequest(bucketName, filename)
                .withRange(start, end);
        S3Object s3Object = s3Client.getObject(getObjectRequest);

        try (InputStream inputStream = s3Object.getObjectContent();
             ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {

            byte[] buffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, bytesRead);
            }
            return outputStream.toByteArray();
        }
    }

    public ResponseEntity<byte[]> prepareContent(final String videoUrl, final String range, final Long accountID, final Long movieID) {
        try {
            final String awsEndpoint = "https://movies-website-tlcn-project.s3.ap-southeast-1.amazonaws.com/";
            if (!videoUrl.contains(awsEndpoint)) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
            }

            final String fileKey = videoUrl.replace(awsEndpoint, "");
            final String fileType = videoUrl.substring(videoUrl.lastIndexOf(".") + 1);
            long rangeStart = 0;
            long rangeEnd = 1048576; // 1 MB chunk size

            GetObjectRequest getObjectRequest = new GetObjectRequest(bucketName, fileKey);
            S3Object s3Object = s3Client.getObject(getObjectRequest);
            long fileSize = s3Object.getObjectMetadata().getContentLength();

            if (range != null) {
                String[] ranges = range.split("-");
                rangeStart = Long.parseLong(ranges[0].substring(6));
                if (ranges.length > 1) {
                    rangeEnd = Long.parseLong(ranges[1]);
                } else {
                    rangeEnd = rangeStart + 1048576;
                }
                rangeEnd = Math.min(rangeEnd, fileSize - 1);
            }

            byte[] data = readByteRange(fileKey, rangeStart, rangeEnd);
            String contentLength = String.valueOf((rangeEnd - rangeStart) + 1);
            String contentRange = "bytes " + rangeStart + "-" + rangeEnd + "/" + fileSize;

            HttpStatus status = (rangeStart == 0 && rangeEnd >= fileSize) ? HttpStatus.OK : HttpStatus.PARTIAL_CONTENT;
            return ResponseEntity.status(status)
                    .header(HttpHeaders.CONTENT_TYPE, "video/" + fileType)
                    .header(HttpHeaders.ACCEPT_RANGES, "bytes")
                    .header(HttpHeaders.CONTENT_LENGTH, contentLength)
                    .header(HttpHeaders.CONTENT_RANGE, contentRange)
                    .body(data);

        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}