package com.ecommerce.website.movie.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectInputStream;
import com.ecommerce.website.movie.constant.Constant;
import com.ecommerce.website.movie.controller.WatchedMovieController;
import com.ecommerce.website.movie.dto.ApiResponseDto;
import com.ecommerce.website.movie.dto.UploadFileDto;
import com.ecommerce.website.movie.dto.UploadVideoDto;
import com.ecommerce.website.movie.dto.aws.FileS3Dto;
import com.ecommerce.website.movie.form.UploadFileForm;
import com.ecommerce.website.movie.model.Movie;
import com.ecommerce.website.movie.model.MovieGenre;
import com.ecommerce.website.movie.model.Review;
import com.ecommerce.website.movie.model.WatchedMovies;
import com.ecommerce.website.movie.repository.MovieRepository;
import com.ecommerce.website.movie.repository.ReviewRepository;
import com.ecommerce.website.movie.repository.WatchedMovieRepository;
import com.ecommerce.website.movie.service.aws.S3Service;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.RandomStringUtils;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DefaultDataBufferFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.annotation.Autowired;
import org.apache.commons.io.FilenameUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;

import java.io.*;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

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
    private WatchedMovieRepository watchedMovieRepository;
    @Autowired
    private MovieRepository movieRepository;
    @Autowired
    ReviewRepository reviewRepository;
    private final WebClient webClient;
    private final AmazonS3 s3Client;

    public MovieService(WebClient.Builder webClientBuilder, AmazonS3 s3Client) {
        this.webClient = webClientBuilder.baseUrl(Constant.AWS_ENDPOINT).build();
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

    public List<Movie> rankingMovieToRecommendListFavoriteByGenre(Long accountId) {
        List<Movie> movieListFavorite = new ArrayList<>();
        List<WatchedMovies> watchedMovies = watchedMovieRepository.findAllByAccountId(accountId);
        for (WatchedMovies watchedMovie : watchedMovies) {
            if (watchedMovie.getWatchedTime() > watchedMovie.getDuration() / 2) {
                Movie movie = movieRepository.findById(watchedMovie.getMovieId()).orElse(null);
                if (movie != null) {
                    movieListFavorite.add(movie);
                }
            }
        }

        Map<Long, Integer> countMovieByGenre = new HashMap<>();
        for (Movie movie : movieListFavorite) {
            for (MovieGenre genre : movie.getGenres()) {
                countMovieByGenre.put(genre.getId(), countMovieByGenre.getOrDefault(genre.getId(), 0) + 1);
            }
        }

        Long mostFrequentGenreId = null;
        int maxCount = 0;
        for (Map.Entry<Long, Integer> entry : countMovieByGenre.entrySet()) {
            if (entry.getValue() > maxCount) {
                maxCount = entry.getValue();
                mostFrequentGenreId = entry.getKey();
            }
        }

        List<Movie> movieListFavoriteByGenre = new ArrayList<>();
        if (mostFrequentGenreId != null) {
            List<Movie> moviesByGenre = movieRepository.findAllByCategoryId(mostFrequentGenreId);

            for (Movie movie : moviesByGenre) {
                if (!movieListFavorite.contains(movie)) {
                    movieListFavoriteByGenre.add(movie);
                }
            }
        }
        return movieListFavoriteByGenre;
    }
}