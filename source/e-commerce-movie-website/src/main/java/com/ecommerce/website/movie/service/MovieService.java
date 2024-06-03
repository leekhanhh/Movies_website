package com.ecommerce.website.movie.service;

import com.ecommerce.website.movie.dto.ApiResponseDto;
import com.ecommerce.website.movie.dto.UploadFileDto;
import com.ecommerce.website.movie.dto.VideoResponseDto;
import com.ecommerce.website.movie.dto.aws.FileS3Dto;
import com.ecommerce.website.movie.dto.movie.MovieDto;
import com.ecommerce.website.movie.form.UploadFileForm;
import com.ecommerce.website.movie.model.Movie;
import com.ecommerce.website.movie.repository.MovieRepository;
import com.ecommerce.website.movie.service.aws.S3Service;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.mongodb.client.gridfs.GridFSBucket;
import com.mongodb.client.gridfs.model.GridFSFile;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.RandomStringUtils;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.gridfs.GridFsOperations;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.annotation.Autowired;
import org.apache.commons.io.FilenameUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Objects;
import java.util.Optional;

@Service
@Slf4j
public class MovieService {
    static final String[] UPLOAD_TYPES = new String[]{"LOGO", "AVATAR", "IMAGE", "DOCUMENT"};
    @Value("${cloud.aws.s3.bucket}")
    private String bucketName;
    @Value("${cloud.aws.s3.endpoint.url}")
    private String endpointUrl;
    @Autowired
    private S3Service s3Service;
    @Autowired
    private GridFsTemplate gridFsTemplate;
    @Autowired
    private GridFSBucket gridFSBucket;
    @Autowired
    GridFsOperations gridFsOperations;
    @Autowired
    private MovieRepository movieRepository;

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
        String fileUrl = endpointUrl + bucketName + "/" + finalFile;
        UploadFileDto uploadFileDto = new UploadFileDto();
        uploadFileDto.setFilePath(fileUrl);
        apiMessageDto.setData(uploadFileDto);
        apiMessageDto.setMessage(s3Service.uploadFile(uploadFileForm.getFile(), finalFile));
        return apiMessageDto;
    }

    public FileS3Dto loadFileAsResource(String fileName) {
        return s3Service.downloadFile(fileName);
    }

    public void deleteFileS3(String avatarPath) {
        String awsEndpoint = endpointUrl + bucketName + "/";
        if (!avatarPath.contains(awsEndpoint)) {
            log.error("[AWS S3] File not found!");
            return;
        }
        String key = avatarPath.replace(awsEndpoint, "");
        s3Service.deleteFile(key);
    }

    public InputStream getVideoStreamById(String videoId) throws IOException {
        GridFSFile gridFSFile = gridFsTemplate.findOne(new Query(Criteria.where("_id").is(videoId)));
        if (gridFSFile != null) {
            return gridFsOperations.getResource(gridFSFile).getInputStream();
        }
        throw new FileNotFoundException("Video not found");
    }

    public void deleteVideo(String id) {
        gridFsTemplate.delete(new Query(Criteria.where("_id").is(id)));
    }

    public VideoResponseDto getVideo(String id) throws IllegalStateException, IOException {
        GridFSFile file = gridFsTemplate.findOne(new Query(Criteria.where("_id").is(id)));
        VideoResponseDto video = new VideoResponseDto();
        assert Objects.requireNonNull(file).getMetadata() != null;
        video.setTitle(file.getMetadata().get("title").toString());
        video.setStream(gridFsOperations.getResource(file).getInputStream());
        return video;
    }
}