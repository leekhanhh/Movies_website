package com.ecommerce.website.movie.controller;

import com.ecommerce.website.movie.dto.ApiResponseDto;
import com.ecommerce.website.movie.dto.ErrorCode;
import com.ecommerce.website.movie.dto.ResponseListDto;
import com.ecommerce.website.movie.dto.movie.MovieDto;
import com.ecommerce.website.movie.dto.rating.RatingDto;
import com.ecommerce.website.movie.form.rating.CreateRatingForm;
import com.ecommerce.website.movie.form.rating.UpdateRatingForm;
import com.ecommerce.website.movie.mapper.MovieMapper;
import com.ecommerce.website.movie.mapper.RatingMapper;
import com.ecommerce.website.movie.model.Account;
import com.ecommerce.website.movie.model.Movie;
import com.ecommerce.website.movie.model.Rating;
import com.ecommerce.website.movie.model.User;
import com.ecommerce.website.movie.model.criteria.RatingCriteria;
import com.ecommerce.website.movie.repository.AccountRepository;
import com.ecommerce.website.movie.repository.MovieRepository;
import com.ecommerce.website.movie.repository.RatingRepository;
import com.ecommerce.website.movie.repository.UserRepository;
import com.ecommerce.website.movie.service.RatingService;
import lombok.extern.slf4j.Slf4j;
import org.h2.expression.condition.ExistsPredicate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/v1/rating")
@CrossOrigin(origins = "*", allowedHeaders = "*")
@Slf4j
public class RatingController {
    @Autowired
    RatingRepository ratingRepository;
    @Autowired
    RatingMapper ratingMapper;
    @Autowired
    MovieRepository movieRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    RatingService ratingService;
    @Autowired
    MovieMapper movieMapper;

    @PostMapping(value = "/create", produces = MediaType.APPLICATION_JSON_VALUE)
    @Transactional
    public ApiResponseDto<Long> createRating(@Valid @RequestBody CreateRatingForm ratingForm, BindingResult bindingResult) {
        ApiResponseDto<Long> apiResponseDto = new ApiResponseDto<>();
        User user = userRepository.findById(ratingForm.getAccountId()).orElse(null);
        if (user == null) {
            apiResponseDto.setResult(false);
            apiResponseDto.setCode(ErrorCode.ACCOUNT_NOT_FOUND);
            apiResponseDto.setMessage("User not found!");
            return apiResponseDto;
        }
        Movie movie = movieRepository.findById(ratingForm.getMovieId()).orElse(null);
        if (movie == null) {
            apiResponseDto.setResult(false);
            apiResponseDto.setCode(ErrorCode.MOVIE_NOT_FOUND);
            apiResponseDto.setMessage("Movie not found!");
            return apiResponseDto;
        }
        if (ratingRepository.findByAccountIdAndMovieId(ratingForm.getAccountId(), ratingForm.getMovieId()) != null) {
            apiResponseDto.setResult(false);
            apiResponseDto.setCode(ErrorCode.RATING_ALREADY_EXIST);
            apiResponseDto.setMessage("Rating already exist!");
            return apiResponseDto;
        }
        Rating rating = ratingMapper.fromRatingForm(ratingForm);
        ratingRepository.save(rating);
        apiResponseDto.setMessage("Rating has been saved successfully!");
        return apiResponseDto;
    }

    @GetMapping(value = "/getAudienceScoreByMovieId/{movieId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiResponseDto<Double> getAudienceScoreByMovieId(@PathVariable Long movieId) {
        ApiResponseDto<Double> apiResponseDto = new ApiResponseDto<>();
        Double audienceScore = ratingService.calculateAudienceScore(movieId);
        apiResponseDto.setResult(true);
        apiResponseDto.setData(audienceScore);
        return apiResponseDto;
    }

    @GetMapping(value = "/isMoviePositivelyRated/{movieId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiResponseDto<Boolean> isMoviePositivelyRated(@PathVariable Long movieId) {
        ApiResponseDto<Boolean> apiResponseDto = new ApiResponseDto<>();
        Boolean isPositivelyRated = ratingService.isMoviePositivelyRated(movieId);
        apiResponseDto.setResult(true);
        apiResponseDto.setData(isPositivelyRated);
        return apiResponseDto;
    }

    @PutMapping(value = "/update", produces = MediaType.APPLICATION_JSON_VALUE)
    @Transactional
    public ApiResponseDto<Long> updateRating(@Valid @RequestBody UpdateRatingForm updateRatingForm, BindingResult bindingResult) {
        ApiResponseDto<Long> apiResponseDto = new ApiResponseDto<>();
        User user = userRepository.findById(updateRatingForm.getAccountId()).orElse(null);
        if (user == null) {
            apiResponseDto.setResult(false);
            apiResponseDto.setCode(ErrorCode.ACCOUNT_NOT_FOUND);
            apiResponseDto.setMessage("Account not found!");
            return apiResponseDto;
        }
        Movie movie = movieRepository.findById(updateRatingForm.getMovieId()).orElse(null);
        if (movie == null) {
            apiResponseDto.setResult(false);
            apiResponseDto.setCode(ErrorCode.MOVIE_NOT_FOUND);
            apiResponseDto.setMessage("Movie not found!");
            return apiResponseDto;
        }
        Rating rating = ratingRepository.findByAccountIdAndMovieId(updateRatingForm.getAccountId(), updateRatingForm.getMovieId());
        if (rating == null) {
            apiResponseDto.setResult(false);
            apiResponseDto.setCode(ErrorCode.RATING_ALREADY_EXIST);
            apiResponseDto.setMessage("Rating not found!");
            return apiResponseDto;
        }
        ratingMapper.updateRatingFromRatingForm(updateRatingForm, rating);
        ratingRepository.save(rating);
        apiResponseDto.setMessage("Rating has been updated successfully!");
        return apiResponseDto;
    }

    @DeleteMapping(value = "/delete/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @Transactional
    public ApiResponseDto<Long> deleteRating(@PathVariable Long id) {
        ApiResponseDto<Long> apiResponseDto = new ApiResponseDto<>();
        Rating rating = ratingRepository.findById(id).orElse(null);
        if (rating == null) {
            apiResponseDto.setResult(false);
            apiResponseDto.setCode(ErrorCode.RATING_NOT_FOUND);
            apiResponseDto.setMessage("Rating not found!");
            return apiResponseDto;
        }
        ratingRepository.delete(rating);
        apiResponseDto.setMessage("Rating has been deleted successfully!");
        return apiResponseDto;
    }

    @GetMapping(value = "/list", produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiResponseDto<ResponseListDto<RatingDto>> listRating(RatingCriteria ratingCriteria, Pageable pageable){
        ApiResponseDto<ResponseListDto<RatingDto>> apiResponseDto = new ApiResponseDto<>();
        Page<Rating> ratingPage = ratingRepository.findAll(ratingCriteria.getSpecification(), pageable);
        ResponseListDto<RatingDto> ratingDtoList =  new ResponseListDto(ratingMapper.toRatingDtoList(ratingPage.getContent()), ratingPage.getTotalElements(), ratingPage.getTotalPages());
        apiResponseDto.setData(ratingDtoList);
        apiResponseDto.setMessage("Get rating list successfully!");
        return apiResponseDto;
    }

    @GetMapping(value = "/list-recommending-movie", produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiResponseDto<ResponseListDto<MovieDto>> listRecommendingMovie(RatingCriteria ratingCriteria, Pageable pageable){
        ApiResponseDto<ResponseListDto<MovieDto>> apiResponseDto = new ApiResponseDto<>();
        Page<Rating> ratingPage = ratingRepository.findAll(ratingCriteria.getSpecification(), pageable);

        Set<Rating> recommendingRatingList = new HashSet<>();
        Set<Long> positivelyRatedMovieIds = new HashSet<>();

        for (Rating rating : ratingPage.getContent()) {
            if (ratingService.isMoviePositivelyRated(rating.getMovie().getId())) {
                positivelyRatedMovieIds.add(rating.getMovie().getId());
            }
        }

        for (Rating rating : ratingPage.getContent()) {
            if (positivelyRatedMovieIds.contains(rating.getMovie().getId())) {
                recommendingRatingList.add(rating);
            }
        }

        if (ratingPage.isEmpty()){
            apiResponseDto.setMessage("No recommending movie found!");
            return apiResponseDto;
        }
        List<Movie> movieList = new ArrayList<>();
        for(Rating rating : recommendingRatingList){
            Movie movie = movieRepository.findById(rating.getMovie().getId()).orElse(null);
            if (movie != null && !movieList.contains(movie)) {
                movieList.add(movie);
            }
        }
        int start = Math.toIntExact(pageable.getOffset());
        int end = Math.min((start + pageable.getPageSize()), movieList.size());
        Page<Movie> recommendingMoviePage = new PageImpl<>(movieList.subList(start, end), pageable, movieList.size());

        ResponseListDto<MovieDto> ratingDtoList =  new ResponseListDto(movieMapper.toClientMovieDtoList(recommendingMoviePage.getContent()), recommendingMoviePage.getTotalElements(), recommendingMoviePage.getTotalPages());
        apiResponseDto.setData(ratingDtoList);
        apiResponseDto.setMessage("Get rating list successfully!");
        return apiResponseDto;
    }

}
