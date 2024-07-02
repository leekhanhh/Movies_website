package com.ecommerce.website.movie.service.aws;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.*;
import com.amazonaws.util.IOUtils;
import com.ecommerce.website.movie.dto.aws.FileS3Dto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Objects;

@Service
@Slf4j
public class S3Service {
    @Value("${cloud.aws.s3.bucket}")
    private String bucketName;
    @Value("${cloud.aws.region.static}")
    private String region;

    @Autowired
    private AmazonS3 s3Client;


    public String uploadFile(MultipartFile file, String fileName) {
        File fileObj = convertMultiPartFileToFile(file);
        s3Client.putObject(new PutObjectRequest(bucketName, fileName, fileObj).withCannedAcl(CannedAccessControlList.PublicReadWrite));
        boolean delete = fileObj.delete();
        if (!delete) {
            log.error("[AWS S3] Uploaded file failed");
            return "Uploaded file failed";
        }
        log.info("[AWS S3] File uploaded : " + fileName);
        return "File uploaded : " + fileName;
    }

    public FileS3Dto downloadFile(String fileName) {
        S3Object s3Object = s3Client.getObject(bucketName, fileName);
        S3ObjectInputStream inputStream = s3Object.getObjectContent();
        try {
            byte[] content = IOUtils.toByteArray(inputStream);
            FileS3Dto fileS3Dto = new FileS3Dto();
            fileS3Dto.setFileByte(content);
            fileS3Dto.setFileType(s3Object.getObjectMetadata().getContentType());
            return fileS3Dto;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }


    public void deleteFile(String fileName) {
        log.info("[AWS S3] Deleting file: " + fileName + " from bucket: " + bucketName + "...");
        String file_name_new = fileName.replace("https://" + bucketName + ".s3." + region + ".amazonaws.com/", "");
        DeleteObjectRequest deleteObjectRequest = new DeleteObjectRequest(bucketName, file_name_new).withKey(file_name_new);
        s3Client.deleteObject(deleteObjectRequest);
    }

    public String deleteVideo(String fileName) {
        try {
            String key = extractKeyFromUrl(fileName);
            s3Client.deleteObject(new DeleteObjectRequest(bucketName, key));
            return "Video deleted successfully.";
        } catch (Exception e) {
            throw new RuntimeException("Failed to delete video from S3", e);
        }
    }

    private String extractKeyFromUrl(String url) {
        String prefix = "https://" + bucketName + ".s3.";
        int prefixEndIndex = url.indexOf("/", prefix.length());
        return url.substring(prefixEndIndex + 1);
    }


    private File convertMultiPartFileToFile(MultipartFile file) {
        File convertedFile = new File(Objects.requireNonNull(file.getOriginalFilename()));
        try (FileOutputStream fos = new FileOutputStream(convertedFile)) {
            fos.write(file.getBytes());
        } catch (IOException e) {
            log.error("Error converting multipartFile to file", e);
        }
        return convertedFile;
    }

    public String uploadVideo(MultipartFile file, String bandwidth) {
        try {
            String fileName = file.getOriginalFilename();
            String key = "Video/" + bandwidth + "/" + fileName;
            ObjectMetadata metadata = new ObjectMetadata();
            metadata.setContentLength(file.getSize());
            s3Client.putObject(new PutObjectRequest(bucketName, key, file.getInputStream(), metadata).withCannedAcl(CannedAccessControlList.PublicReadWrite));
            log.info("[AWS S3] Video uploaded : " + metadata.getContentLength() + " bytes");
            return "https://" + bucketName + ".s3." + region + ".amazonaws.com/" + key;
        } catch (IOException e) {
            log.error("[AWS S3] Uploaded video failed", e);
            return null;
        }
    }



}