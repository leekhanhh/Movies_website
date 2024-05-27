package com.ecommerce.website.movie.controller;

import com.ecommerce.website.movie.dto.ApiResponseDto;
import com.ecommerce.website.movie.dto.ErrorCode;
import com.ecommerce.website.movie.dto.ResponseListDto;
import com.ecommerce.website.movie.dto.review.ReviewDto;
import com.ecommerce.website.movie.form.review.CreateReviewFrom;
import com.ecommerce.website.movie.form.review.UpdateReviewForm;
import com.ecommerce.website.movie.mapper.ReviewMapper;
import com.ecommerce.website.movie.model.Account;
import com.ecommerce.website.movie.model.Movie;
import com.ecommerce.website.movie.model.Review;
import com.ecommerce.website.movie.model.criteria.ReviewCriteria;
import com.ecommerce.website.movie.repository.AccountRepository;
import com.ecommerce.website.movie.repository.MovieRepository;
import com.ecommerce.website.movie.repository.ReviewRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/v1/review")
@CrossOrigin(origins = "*", allowedHeaders = "*")
@Slf4j
public class ReviewController {
    @Autowired
    private ReviewRepository reviewRepository;
    @Autowired
    private ReviewMapper reviewMapper;
    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    MovieRepository movieRepository;

    @PostMapping(value = "/create", produces = MediaType.APPLICATION_JSON_VALUE)
    @Transactional
    public ApiResponseDto<Long> createReview(@Valid @RequestBody CreateReviewFrom createReviewForm, BindingResult bindingResult) {
        ApiResponseDto<Long> apiResponseDto = new ApiResponseDto<>();
        Account account = accountRepository.findById(createReviewForm.getAccountId()).get();
        if (account == null) {
            apiResponseDto.setResult(false);
            apiResponseDto.setCode(ErrorCode.ACCOUNT_NOT_FOUND);
            apiResponseDto.setMessage("Account not found!");
            return apiResponseDto;
        }
        Movie movie = movieRepository.findById(createReviewForm.getMovieId()).get();
        if (movie == null) {
            apiResponseDto.setResult(false);
            apiResponseDto.setCode(ErrorCode.MOVIE_NOT_FOUND);
            apiResponseDto.setMessage("Movie not found!");
            return apiResponseDto;
        }
        Review review = reviewMapper.fromCreateReviewEntity(createReviewForm);
        reviewRepository.save(review);
        apiResponseDto.setMessage("Review has been saved successfully!");
        return apiResponseDto;
    }

    @PutMapping(value = "/update", produces = MediaType.APPLICATION_JSON_VALUE)
    @Transactional
    public ApiResponseDto<Long> updateReview(@Valid @RequestBody UpdateReviewForm updateReviewForm, BindingResult bindingResult) {
        ApiResponseDto<Long> apiResponseDto = new ApiResponseDto<>();
        Review review = reviewRepository.findById(updateReviewForm.getId()).orElse(null);
        if (review == null) {
            apiResponseDto.setResult(false);
            apiResponseDto.setCode(ErrorCode.REVIEW_NOT_FOUND);
            apiResponseDto.setMessage("Review not found!");
            return apiResponseDto;
        }
        reviewMapper.updateReviewEntity(updateReviewForm, review);
        reviewRepository.save(review);
        apiResponseDto.setMessage("Review has been updated successfully!");
        return apiResponseDto;
    }

    @DeleteMapping(value = "/delete/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @Transactional
    public ApiResponseDto<Long> deleteReview(@PathVariable Long id) {
        ApiResponseDto<Long> apiResponseDto = new ApiResponseDto<>();
        Review review = reviewRepository.findById(id).orElse(null);
        if (review == null) {
            apiResponseDto.setResult(false);
            apiResponseDto.setCode(ErrorCode.REVIEW_NOT_FOUND);
            apiResponseDto.setMessage("Review not found!");
            return apiResponseDto;
        }
        reviewRepository.delete(review);
        apiResponseDto.setMessage("Review has been deleted successfully!");
        return apiResponseDto;
    }

    @GetMapping(value = "/get/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiResponseDto<ReviewDto> getReview(@PathVariable("id") Long id) {
        ApiResponseDto<ReviewDto> apiResponseDto = new ApiResponseDto<>();
        Review review = reviewRepository.findById(id).orElse(null);
        if (review != null) {
            apiResponseDto.setMessage("Review found!");
            apiResponseDto.setData(reviewMapper.fromReviewEntityToDto(review));
        } else {
            apiResponseDto.setResult(false);
            apiResponseDto.setMessage("Review not found!");
            apiResponseDto.setCode(ErrorCode.REVIEW_NOT_FOUND);
        }
        return apiResponseDto;
    }

    @GetMapping(value = "/list", produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiResponseDto<ResponseListDto<ReviewDto>> listReview(ReviewCriteria reviewCriteria, Pageable pageable) {
        ApiResponseDto<ResponseListDto<ReviewDto>> apiResponseDto = new ApiResponseDto<>();
        Page<Review> reviewPage = reviewRepository.findAll(reviewCriteria.getSpecification(), pageable);
        ResponseListDto<ReviewDto> responseListDto = new ResponseListDto(reviewMapper.fromReviewEntityListToDtoList(reviewPage.getContent()), reviewPage.getTotalElements(), reviewPage.getTotalPages());
        apiResponseDto.setData(responseListDto);
        apiResponseDto.setMessage("Get review list successfully!");
        return apiResponseDto;
    }

    @GetMapping(value = "/list-movie/{movieId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiResponseDto<ResponseListDto<ReviewDto>> listReviewByMovieId(@PathVariable Long movieId, ReviewCriteria reviewCriteria, Pageable pageable) {
        ApiResponseDto<ResponseListDto<ReviewDto>> apiResponseDto = new ApiResponseDto<>();
        reviewCriteria.setMovieId(movieId);
        Page<Review> reviewPage = reviewRepository.findAll(reviewCriteria.getSpecification(), pageable);
        if (reviewPage.isEmpty()) {
            apiResponseDto.setResult(false);
            apiResponseDto.setCode(ErrorCode.REVIEW_NOT_FOUND);
            apiResponseDto.setMessage("Review not found!");
            return apiResponseDto;
        }
        ResponseListDto<ReviewDto> responseListDto = new ResponseListDto(
                reviewMapper.fromReviewEntityListToDtoList(reviewPage.getContent()),
                reviewPage.getTotalElements(),
                reviewPage.getTotalPages());

        apiResponseDto.setData(responseListDto);
        apiResponseDto.setMessage("Get review by movie list successfully!");
        return apiResponseDto;
    }

    @GetMapping(value = "/list-account/{userId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiResponseDto<ResponseListDto<ReviewDto>> listReviewByAccountId(@PathVariable Long userId, ReviewCriteria reviewCriteria, Pageable pageable) {
        ApiResponseDto<ResponseListDto<ReviewDto>> apiResponseDto = new ApiResponseDto<>();
        reviewCriteria.setUserId(userId);
        Page<Review> reviewPage = reviewRepository.findAll(reviewCriteria.getSpecification(), pageable);
        if (reviewPage.isEmpty()) {
            apiResponseDto.setResult(false);
            apiResponseDto.setCode(ErrorCode.REVIEW_NOT_FOUND);
            apiResponseDto.setMessage("Review not found!");
            return apiResponseDto;
        }
        ResponseListDto<ReviewDto> responseListDto = new ResponseListDto(
                reviewMapper.fromReviewEntityListToDtoList(reviewPage.getContent()),
                reviewPage.getTotalElements(),
                reviewPage.getTotalPages());

        apiResponseDto.setData(responseListDto);
        apiResponseDto.setMessage("Get review by account list successfully!");
        return apiResponseDto;
    }
}
