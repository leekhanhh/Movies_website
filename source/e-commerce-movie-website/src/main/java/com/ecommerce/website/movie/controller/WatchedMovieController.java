package com.ecommerce.website.movie.controller;

import com.ecommerce.website.movie.dto.ApiResponseDto;
import com.ecommerce.website.movie.dto.ResponseListDto;
import com.ecommerce.website.movie.dto.movie.MovieDto;
import com.ecommerce.website.movie.form.movie.watchedmovie.CreateWatchedMovieForm;
import com.ecommerce.website.movie.mapper.MovieMapper;
import com.ecommerce.website.movie.mapper.WatchedMovieMapper;
import com.ecommerce.website.movie.model.Movie;
import com.ecommerce.website.movie.model.WatchedMovies;
import com.ecommerce.website.movie.model.criteria.WatchedMovieCriteria;
import com.ecommerce.website.movie.repository.MovieRepository;
import com.ecommerce.website.movie.repository.WatchedMovieRepository;
import lombok.extern.slf4j.Slf4j;
import org.bson.types.ObjectId;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
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
    @Autowired
    MovieMapper movieMapper;
//
//    @PostMapping(value = "/create-watched-movie", produces = org.springframework.http.MediaType.APPLICATION_JSON_VALUE)
//    @Transactional
//    @ApiIgnore
//    public ApiResponseDto<WatchedMovies> createWatchedMovie(@Valid @RequestBody CreateWatchedMovieForm createWatchedMovieForm, BindingResult bindingResult) {
//        ApiResponseDto<WatchedMovies> apiResponseDto = new ApiResponseDto<>();
//        Optional<Movie> movie = movieRepository.findById(createWatchedMovieForm.getMovieId());
//        if (movie == null) {
//            apiResponseDto.setResult(false);
//            apiResponseDto.setCode(com.ecommerce.website.movie.dto.ErrorCode.MOVIE_NOT_FOUND);
//            apiResponseDto.setMessage("Movie not found!");
//            return apiResponseDto;
//        }
//        WatchedMovies watchedMovies = watchedMovieMapper.fromCreateWatchedMovieForm(createWatchedMovieForm);
//        watchedMovieRepository.save(watchedMovies);
//        apiResponseDto.setResult(true);
//        apiResponseDto.setData(watchedMovies);
//        return apiResponseDto;
//    }

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


    public void markMovieAsWatched(Long accountId, Long movieId) {
        if (accountId == null || movieId == null) {
            return;
        }
        LocalDateTime now = LocalDateTime.now();
        DateTime formattedDateTime = new DateTime(now.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli());
        WatchedMovies watchedMovie = watchedMovieRepository.findByAccountIdAndMovieId(accountId, movieId);
        if (watchedMovie != null) {
            watchedMovie.setCreatedDate(formattedDateTime.toDate());
            watchedMovieRepository.save(watchedMovie);
            return;
        }
        CreateWatchedMovieForm createWatchedMovieForm = new CreateWatchedMovieForm();
        createWatchedMovieForm.setAccountId(accountId);
        createWatchedMovieForm.setMovieId(movieId);
        watchedMovie = watchedMovieMapper.fromCreateWatchedMovieForm(createWatchedMovieForm);
        watchedMovie.setCreatedDate(formattedDateTime.toDate());
        watchedMovieRepository.save(watchedMovie);
    }

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

    @GetMapping(value = "/list-watched-movie-by-accountId", produces = org.springframework.http.MediaType.APPLICATION_JSON_VALUE)
    public ApiResponseDto<ResponseListDto<MovieDto>> listWatchedMovieByAccountId(WatchedMovieCriteria watchedMovieCriteria, Pageable pageable){
        ApiResponseDto<ResponseListDto<MovieDto>> apiResponseDto = new ApiResponseDto<>();
        Query query = new Query(Criteria.where("accountId").is(watchedMovieCriteria.getAccountId()))
                .with(Sort.by(Sort.Direction.DESC, "createdDate"))
                .with(pageable);
        List<WatchedMovies> watchedMoviesOnlyDtoList = mongoTemplate.find(query, WatchedMovies.class);
        Page<WatchedMovies> watchedMoviePage = new PageImpl<>(watchedMoviesOnlyDtoList, pageable, watchedMoviesOnlyDtoList.size());
        List<Movie> movieList = new ArrayList<>();
        for (WatchedMovies watchedMovie : watchedMoviesOnlyDtoList) {
            Optional<Movie> movie = movieRepository.findById(watchedMovie.getMovieId());
            movie.ifPresent(movieList::add);
        }
        ResponseListDto<MovieDto> responseListDto = new ResponseListDto(movieMapper.toClientMovieDtoList(movieList), watchedMoviePage.getTotalElements(), watchedMoviePage.getTotalPages());
        apiResponseDto.setResult(true);
        apiResponseDto.setData(responseListDto);
        return apiResponseDto;
    }
}
