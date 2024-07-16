package com.ecommerce.website.movie.controller;

import com.ecommerce.website.movie.dto.ApiResponseDto;
import com.ecommerce.website.movie.dto.ResponseListDto;
import com.ecommerce.website.movie.dto.WatchTimeDto;
import com.ecommerce.website.movie.dto.movie.MovieDto;
import com.ecommerce.website.movie.form.UpdateWatchTimeTrackForm;
import com.ecommerce.website.movie.form.WatchTimeTrackForm;
import com.ecommerce.website.movie.form.movie.watchedmovie.CreateWatchedMovieForm;
import com.ecommerce.website.movie.mapper.MovieMapper;
import com.ecommerce.website.movie.mapper.WatchedMovieMapper;
import com.ecommerce.website.movie.model.Movie;
import com.ecommerce.website.movie.model.WatchedMovies;
import com.ecommerce.website.movie.model.criteria.WatchedMovieCriteria;
import com.ecommerce.website.movie.repository.MovieRepository;
import com.ecommerce.website.movie.repository.WatchedMovieRepository;
import com.ecommerce.website.movie.service.MovieService;
import lombok.extern.slf4j.Slf4j;
import org.bson.types.ObjectId;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.http.MediaType;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.naming.Binding;
import javax.validation.Valid;
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
    @Autowired
    MovieService movieService;

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
        List<Movie> movieList = movieService.rankingMovieToRecommendListFavoriteByGenre(watchedMovieCriteria.getAccountId());
        Page<Movie> moviePage = new PageImpl<>(movieList, pageable, movieList.size());
        List<MovieDto> movieDtoList = movieMapper.toClientMovieDtoList(moviePage.getContent());
        ResponseListDto<MovieDto> responseListDto = new ResponseListDto(movieDtoList, moviePage.getTotalElements(), moviePage.getTotalPages());
        apiResponseDto.setData(responseListDto);
        apiResponseDto.setResult(true);
        apiResponseDto.setMessage("List watched movie by accountId successfully!");
        return apiResponseDto;
    }

    @PostMapping(value = "/save-watch-time", produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiResponseDto<WatchTimeDto> watchedTimeSave(@RequestBody @Valid WatchTimeTrackForm watchTimeTrackForm, BindingResult bindingResult) {
        ApiResponseDto<WatchTimeDto> apiResponseDto = new ApiResponseDto<>();

        if (bindingResult.hasErrors()) {
            apiResponseDto.setResult(false);
            apiResponseDto.setMessage("Invalid input data");
            return apiResponseDto;
        }

        WatchedMovies watchedMovies = watchedMovieRepository.findByAccountIdAndMovieId(watchTimeTrackForm.getAccountId(), watchTimeTrackForm.getMovieId());

        if (watchedMovies != null) {
            watchedMovies.setWatchedTime(watchTimeTrackForm.getWatchedTime());
        } else {
            watchedMovies = watchedMovieMapper.fromCreateWatchedMovieForm(watchTimeTrackForm);
        }

        watchedMovieRepository.save(watchedMovies);

        apiResponseDto.setResult(true);
        apiResponseDto.setData(watchedMovieMapper.toWatchedMovieDto(watchedMovies));
        apiResponseDto.setMessage("Save watched time successfully!");
        return apiResponseDto;
    }

    @PutMapping(value = "/update-watch-time", produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiResponseDto<WatchTimeDto> updateWatchTime(@RequestBody @Valid UpdateWatchTimeTrackForm updateWatchTimeTrackForm, BindingResult bindingResult){
        ApiResponseDto<WatchTimeDto> apiResponseDto = new ApiResponseDto();
        WatchedMovies watchedMovies = watchedMovieRepository.findByAccountIdAndMovieId(updateWatchTimeTrackForm.getAccountId(), updateWatchTimeTrackForm.getMovieId());
        if (watchedMovies == null) {
            apiResponseDto.setResult(false);
            apiResponseDto.setMessage("Watched movie not found!");
            return apiResponseDto;
        }
        watchedMovieMapper.updateWatchedMovie(updateWatchTimeTrackForm, watchedMovies);
        watchedMovieRepository.save(watchedMovies);
        apiResponseDto.setResult(true);
        apiResponseDto.setData(watchedMovieMapper.toWatchedMovieDto(watchedMovies));
        apiResponseDto.setMessage("Update watched time successfully!");
        return apiResponseDto;
    }
}
