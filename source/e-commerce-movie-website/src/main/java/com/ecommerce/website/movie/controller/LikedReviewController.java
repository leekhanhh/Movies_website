package com.ecommerce.website.movie.controller;

import com.ecommerce.website.movie.constant.Constant;
import com.ecommerce.website.movie.dto.ApiResponseDto;
import com.ecommerce.website.movie.dto.ErrorCode;
import com.ecommerce.website.movie.form.likedreview.CreateLikedReviewForm;
import com.ecommerce.website.movie.form.likedreview.UpdateLikedReviewForm;
import com.ecommerce.website.movie.mapper.LikedReviewMapper;
import com.ecommerce.website.movie.model.LikedReview;
import com.ecommerce.website.movie.model.Review;
import com.ecommerce.website.movie.repository.AccountRepository;
import com.ecommerce.website.movie.repository.LikedReviewRepository;
import com.ecommerce.website.movie.repository.ReviewRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import javax.validation.Valid;

@Slf4j
@RestController
@RequestMapping("/v1/liked-reviews")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class LikedReviewController{
    @Autowired
    LikedReviewRepository likedReviewRepository;
    @Autowired
    LikedReviewMapper likedReviewMapper;
    @Autowired
    AccountRepository accountRepository;
    @Autowired
    ReviewRepository reviewRepository;

    @PostMapping(value = "/create", produces = MediaType.APPLICATION_JSON_VALUE)
    @Transactional
    public ApiResponseDto<Long> createLikedReview(@Valid @RequestBody CreateLikedReviewForm likedReviewForm, BindingResult bindingResult) {
        ApiResponseDto<Long> apiResponseDto = new ApiResponseDto<>();
        Review review = reviewRepository.findById(likedReviewForm.getReviewId()).orElse(null);
        if (review == null) {
            apiResponseDto.setResult(false);
            apiResponseDto.setCode(ErrorCode.REVIEW_NOT_FOUND);
            apiResponseDto.setMessage("Review not found!");
            return apiResponseDto;
        }
        LikedReview likedReview = likedReviewRepository.findByAccountIdAndReviewId(likedReviewForm.getAccountId(), likedReviewForm.getReviewId());
        if (likedReview != null) {
            apiResponseDto.setResult(false);
            apiResponseDto.setCode(ErrorCode.LIKED_REVIEW_ALREADY_EXIST);
            apiResponseDto.setMessage("Liked review already exist!");
            return apiResponseDto;
        }

        likedReview = new LikedReview();
        if (likedReviewForm.getEmotion() == 1)
        {
            likedReview.setEmotion(Constant.EMOTION_LIKE);
            apiResponseDto.setMessage("Emotion is like");
        }
        else if(likedReviewForm.getEmotion() == 2)
        {
            likedReview.setEmotion(Constant.EMOTION_LOVE);
            apiResponseDto.setMessage("Emotion is love");
        }
        else if (likedReviewForm.getEmotion() == 3)
        {
            likedReview.setEmotion(Constant.EMOTION_HAHA);
            apiResponseDto.setMessage("Emotion is haha");
        }
        else if (likedReviewForm.getEmotion() == 4)
        {
            likedReview.setEmotion(Constant.EMOTION_WOW);
            apiResponseDto.setMessage("Emotion is wow");
        }
        else if (likedReviewForm.getEmotion() == 5)
        {
            likedReview.setEmotion(Constant.EMOTION_SAD);
            apiResponseDto.setMessage("Emotion is sad");
        }
        else if (likedReviewForm.getEmotion() == 6)
        {
            likedReview.setEmotion(Constant.EMOTION_ANGRY);
            apiResponseDto.setMessage("Emotion is angry");
        }

        likedReviewRepository.save(likedReviewMapper.fromCreateLikedReviewToEntity(likedReviewForm));
        apiResponseDto.setResult(true);
        apiResponseDto.setData(likedReview.getId());
        return apiResponseDto;
    }

    @PutMapping(value = "/update", produces = MediaType.APPLICATION_JSON_VALUE)
    @Transactional
    public ApiResponseDto<Long> updateLikedReview(@Valid @RequestBody UpdateLikedReviewForm updateLikedReviewForm, BindingResult bindingResult) {
        ApiResponseDto<Long> apiResponseDto = new ApiResponseDto<>();
        LikedReview likedReview = likedReviewRepository.findById(updateLikedReviewForm.getId()).orElse(null);
        if (likedReview == null) {
            apiResponseDto.setResult(false);
            apiResponseDto.setCode(ErrorCode.LIKED_REVIEW_NOT_FOUND);
            apiResponseDto.setMessage("Liked review not found!");
            return apiResponseDto;
        }

        likedReviewMapper.updateLikedReviewFromEntity(updateLikedReviewForm, likedReview);
        if (updateLikedReviewForm.getEmotion() == 1)
        {
            likedReview.setEmotion(Constant.EMOTION_LIKE);
            apiResponseDto.setMessage("Emotion is like");
        }
        else if(updateLikedReviewForm.getEmotion() == 2)
        {
            likedReview.setEmotion(Constant.EMOTION_LOVE);
            apiResponseDto.setMessage("Emotion is love");
        }
        else if (updateLikedReviewForm.getEmotion() == 3)
        {
            likedReview.setEmotion(Constant.EMOTION_HAHA);
            apiResponseDto.setMessage("Emotion is haha");
        }
        else if (updateLikedReviewForm.getEmotion() == 4)
        {
            likedReview.setEmotion(Constant.EMOTION_WOW);
            apiResponseDto.setMessage("Emotion is wow");
        }
        else if (updateLikedReviewForm.getEmotion() == 5)
        {
            likedReview.setEmotion(Constant.EMOTION_SAD);
            apiResponseDto.setMessage("Emotion is sad");
        }
        else if (updateLikedReviewForm.getEmotion() == 6)
        {
            likedReview.setEmotion(Constant.EMOTION_ANGRY);
            apiResponseDto.setMessage("Emotion is angry");
        }
        likedReviewRepository.save(likedReview);
        apiResponseDto.setResult(true);
        apiResponseDto.setData(likedReview.getId());
        return apiResponseDto;
    }

    @DeleteMapping(value = "/delete/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @Transactional
    public ApiResponseDto<Long> deleteLikedReview(@PathVariable Long id) {
        ApiResponseDto<Long> apiResponseDto = new ApiResponseDto<>();
        LikedReview likedReview = likedReviewRepository.findById(id).orElse(null);
        if (likedReview == null) {
            apiResponseDto.setResult(false);
            apiResponseDto.setCode(ErrorCode.LIKED_REVIEW_NOT_FOUND);
            apiResponseDto.setMessage("Liked review not found!");
            return apiResponseDto;
        }

        likedReviewRepository.delete(likedReview);
        apiResponseDto.setResult(true);
        apiResponseDto.setMessage("Liked review deleted successfully!");
        return apiResponseDto;
    }
}
