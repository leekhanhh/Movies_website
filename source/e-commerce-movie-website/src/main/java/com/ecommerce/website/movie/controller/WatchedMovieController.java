package com.ecommerce.website.movie.controller;

import com.ecommerce.website.movie.dto.ApiResponseDto;
import com.ecommerce.website.movie.dto.ResponseListDto;
import com.ecommerce.website.movie.dto.movie.WatchedMovieDto;
import com.ecommerce.website.movie.form.movie.watchedmovie.CreateWatchedMovieForm;
import com.ecommerce.website.movie.mapper.WatchedMovieMapper;
import com.ecommerce.website.movie.model.Movie;
import com.ecommerce.website.movie.model.WatchedMovies;
import com.ecommerce.website.movie.model.criteria.WatchedMovieCriteria;
import com.ecommerce.website.movie.repository.MovieRepository;
import com.ecommerce.website.movie.repository.WatchedMovieRepository;
import lombok.extern.slf4j.Slf4j;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@org.springframework.web.bind.annotation.RestController
@org.springframework.web.bind.annotation.RequestMapping("/v1/watched-movie")
@CrossOrigin(origins = "*", allowedHeaders = "*")
@Slf4j
public class WatchedMovieController {
    @Autowired
    WatchedMovieRepository watchedMovieRepository;
    @Autowired
    WatchedMovieMapper watchedMovieMapper;
    @Autowired
    MovieRepository movieRepository;
    @Autowired
    private MongoTemplate mongoTemplate;

    @PostMapping(value = "/create-watched-movie", produces = org.springframework.http.MediaType.APPLICATION_JSON_VALUE)
    @Transactional
    public ApiResponseDto<WatchedMovies> createWatchedMovie(@Valid @RequestBody CreateWatchedMovieForm createWatchedMovieForm, BindingResult bindingResult) {
        ApiResponseDto<WatchedMovies> apiResponseDto = new ApiResponseDto<>();
        Movie movie = movieRepository.findById(createWatchedMovieForm.getMovieId()).orElse(null);
        if (movie == null) {
            apiResponseDto.setResult(false);
            apiResponseDto.setCode(com.ecommerce.website.movie.dto.ErrorCode.MOVIE_NOT_FOUND);
            apiResponseDto.setMessage("Movie not found!");
            return apiResponseDto;
        }

        WatchedMovies watchedMovies = watchedMovieMapper.fromCreateWatchedMovieForm(createWatchedMovieForm);
        watchedMovieRepository.save(watchedMovies);
        apiResponseDto.setResult(true);
        apiResponseDto.setData(watchedMovies);
        return apiResponseDto;
    }

//    @GetMapping(value = "/get-watched-movie-by-account-id", produces = org.springframework.http.MediaType.APPLICATION_JSON_VALUE)
//    public ApiResponseDto<ResponseListDto<WatchedMovies>> getWatchedMovieByAccountId(@RequestParam("accountId") Long accountId, WatchedMovieCriteria watchedMovieCriteria, Pageable pageable){
//        ApiResponseDto<ResponseListDto<WatchedMovies>> apiResponseDto = new ApiResponseDto<>();
//        watchedMovieCriteria.setAccountId(accountId);
//        Page<WatchedMovies> watchedMovies = watchedMovieRepository.findAll(watchedMovieCriteria.toSpecification(), pageable);
//        ResponseListDto<WatchedMovies> responseListDto = new ResponseListDto(watchedMovieMapper.toWatchedMovieDtoList(watchedMovies.getContent()), watchedMovies.getTotalElements(), watchedMovies.getTotalPages());
//        apiResponseDto.setResult(true);
//        apiResponseDto.setData(responseListDto);
//        return apiResponseDto;
//    }



    @DeleteMapping(value = "/delete-watched-movie-until-20-latest", produces = org.springframework.http.MediaType.APPLICATION_JSON_VALUE)
    public ApiResponseDto<Long> deleteWatchedMovieUntil20Latest(@RequestParam("accountId") Long accountId){
        ApiResponseDto<Long> apiResponseDto = new ApiResponseDto<>();
        Query query = new Query(Criteria.where("accountId").is(accountId))
                .with(Sort.by(Sort.Direction.DESC, "createdDate"));
        List<WatchedMovies> watchedMovies = mongoTemplate.find(query, WatchedMovies.class);
        if (watchedMovies.size() > 10) {
            List<WatchedMovies> moviesToDelete = watchedMovies.subList(10, watchedMovies.size());
            List<ObjectId> idsToDelete = moviesToDelete.stream()
                    .map(WatchedMovies::getId)
                    .collect(Collectors.toList());
            Query deleteQuery = new Query(Criteria.where("id").in(idsToDelete));
            mongoTemplate.remove(deleteQuery, WatchedMovies.class);
        }
        apiResponseDto.setResult(true);
        apiResponseDto.setData(accountId);
        apiResponseDto.setMessage("Delete watched movie until 20 latest successfully!");
        return apiResponseDto;
    }


}
