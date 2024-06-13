package com.ecommerce.website.movie.controller;

import com.ecommerce.website.movie.constant.Constant;
import com.ecommerce.website.movie.dto.ApiResponseDto;
import com.ecommerce.website.movie.dto.ErrorCode;
import com.ecommerce.website.movie.dto.ResponseListDto;
import com.ecommerce.website.movie.dto.movie.MovieDto;
import com.ecommerce.website.movie.form.movie.CreateMovieForm;
import com.ecommerce.website.movie.form.movie.UpdateMovieForm;
import com.ecommerce.website.movie.mapper.MovieGenreMapper;
import com.ecommerce.website.movie.mapper.MovieMapper;
import com.ecommerce.website.movie.model.*;
import com.ecommerce.website.movie.model.criteria.MovieCriteria;
import com.ecommerce.website.movie.repository.CategoryRepository;
import com.ecommerce.website.movie.repository.WatchedMovieRepository;
import com.ecommerce.website.movie.repository.MovieGenreRepository;
import com.ecommerce.website.movie.repository.MovieRepository;
import com.ecommerce.website.movie.service.MovieService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.MediaType;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/v1/movie")
@CrossOrigin(origins = "*", allowedHeaders = "*")
@Slf4j
public class MovieController {
    @Autowired
    MovieRepository movieRepository;
    @Autowired
    MovieMapper movieMapper;
    @Autowired
    CategoryRepository categoryRepository;
    @Autowired
    MovieGenreRepository movieGenreRepository;
    @Autowired
    MovieService movieService;
    @Autowired
    WatchedMovieRepository loadVideoRepository;
    @Autowired
    GridFsTemplate gridFsTemplate;
    @Autowired
    MovieGenreMapper movieGenreMapper;

    @PostMapping(value = "/create", produces = MediaType.APPLICATION_JSON_VALUE)
    @Transactional
    public ApiResponseDto<Long> createMovie(@Valid @RequestBody CreateMovieForm movieForm,BindingResult bindingResult) {
        ApiResponseDto<Long> apiResponseDto = new ApiResponseDto<>();
        Category category = categoryRepository.findById(movieForm.getCategoryId()).orElse(null);
        if (category == null || !category.getKind().equals(Constant.CATEGORY_KIND_MOVIE_TYPE)) {
            apiResponseDto.setResult(false);
            apiResponseDto.setCode(ErrorCode.CATEGORY_NOT_FOUND);
            apiResponseDto.setMessage("Category not found or not movie type!");
            return apiResponseDto;
        }
        Movie movie = movieMapper.formCreateMovie(movieForm);
        movie.setCategory(category);
        movieRepository.save(movie);
        List<MovieGenre> genresMovieList = new ArrayList<>();
        for (Long genreId : movieForm.getGenreIds()) {
            Category genreCategory = categoryRepository.findById(genreId).orElse(null);
            if (genreCategory != null && genreCategory.getKind().equals(Constant.CATEGORY_KIND_MOVIE_GENRE)) {
                MovieGenre movieGenre = new MovieGenre();
                movieGenre.setMovie(movie);
                movieGenre.setCategory(genreCategory);
                genresMovieList.add(movieGenre);
            }
        }
        if(!genresMovieList.isEmpty()){
            movieGenreRepository.saveAll(genresMovieList);
        }
        apiResponseDto.setData(movie.getId());
        apiResponseDto.setMessage("Create movie successfully!");
        return apiResponseDto;
    }

    @DeleteMapping(value = "/delete/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @Transactional
    public ApiResponseDto<MovieDto> deleteMovie(@PathVariable Long id) {
        ApiResponseDto<MovieDto> apiResponseDto = new ApiResponseDto<>();
        Movie movie = movieRepository.findById(id).orElse(null);
        if (movie != null) {
            movieRepository.delete(movie);
            movieService.deleteVideoS3ByLink(movie.getVideoGridFs());
            apiResponseDto.setMessage("Movie deleted successfully!");
        } else {
            apiResponseDto.setMessage("Movie not found!");
            apiResponseDto.setCode(ErrorCode.MOVIE_NOT_FOUND);
        }
        return apiResponseDto;
    }

    @GetMapping(value = "/get-server/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiResponseDto<MovieDto> getMovie(@PathVariable Long id) {
        ApiResponseDto<MovieDto> apiResponseDto = new ApiResponseDto<>();
        Movie movie = movieRepository.findById(id).orElse(null);
        if (movie != null) {
            apiResponseDto.setMessage("Movie found!");
            apiResponseDto.setData(movieMapper.toServerMovieDto(movie));
        } else {
            apiResponseDto.setMessage("Movie not found!");
            apiResponseDto.setCode(ErrorCode.MOVIE_NOT_FOUND);
        }
        return apiResponseDto;
    }

    @GetMapping (value = "/get-client/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiResponseDto<MovieDto> getClientMovie(@PathVariable Long id) {
        ApiResponseDto<MovieDto> apiResponseDto = new ApiResponseDto<>();
        Movie movie = movieRepository.findById(id).orElse(null);
        if (movie != null) {
            apiResponseDto.setMessage("Movie found!");
            apiResponseDto.setData(movieMapper.toClientMovieDto(movie));
        } else {
            apiResponseDto.setMessage("Movie not found!");
            apiResponseDto.setCode(ErrorCode.MOVIE_NOT_FOUND);
        }
        return apiResponseDto;
    }


    @GetMapping(value = "/list-server", produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiResponseDto<ResponseListDto<MovieDto>> listMovie(MovieCriteria movieCriteria, Pageable pageable) {
        ApiResponseDto<ResponseListDto<MovieDto>> apiResponseDto = new ApiResponseDto<>();
        pageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), Sort.by(Sort.Direction.DESC, "createdDate"));
        Page<Movie> moviePage = movieRepository.findAll(movieCriteria.getSpecification(), pageable);
        ResponseListDto<MovieDto> responseListDto = new ResponseListDto(movieMapper.toServerMovieDtoList(moviePage.getContent()), moviePage.getTotalElements(), moviePage.getTotalPages());
        apiResponseDto.setData(responseListDto);
        apiResponseDto.setMessage("Get movie list successfully!");
        return apiResponseDto;
    }

    @GetMapping(value = "/list-client", produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiResponseDto<ResponseListDto<MovieDto>> listClientMovie(MovieCriteria movieCriteria, Pageable pageable) {
        ApiResponseDto<ResponseListDto<MovieDto>> apiResponseDto = new ApiResponseDto<>();
        pageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), Sort.by(Sort.Direction.DESC, "createdDate"));
        Page<Movie> moviePage = movieRepository.findAll(movieCriteria.getSpecification(), pageable);
        ResponseListDto<MovieDto> responseListDto = new ResponseListDto(movieMapper.toClientMovieDtoList(moviePage.getContent()), moviePage.getTotalElements(), moviePage.getTotalPages());
        apiResponseDto.setData(responseListDto);
        apiResponseDto.setMessage("Get movie list successfully!");
        return apiResponseDto;
    }

    @PutMapping(value = "/update", produces = MediaType.APPLICATION_JSON_VALUE)
    @Transactional
    public ApiResponseDto<Long> updateMovie(@Valid @RequestBody UpdateMovieForm movieForm, BindingResult bindingResult) {
        ApiResponseDto<Long> apiResponseDto = new ApiResponseDto<>();
        Movie existedMovie = movieRepository.findById(movieForm.getId()).orElse(null);
        if (existedMovie == null) {
            apiResponseDto.setResult(false);
            apiResponseDto.setCode(ErrorCode.MOVIE_NOT_FOUND);
            apiResponseDto.setMessage("Movie not found");
            return apiResponseDto;
        }
        if (!existedMovie.getTitle().equals(movieForm.getTitle())) {
            existedMovie.setTitle(movieForm.getTitle());
        }
        if (!existedMovie.getOverview().equals(movieForm.getOverview())) {
            existedMovie.setOverview(movieForm.getOverview());
        }
        if(!existedMovie.getImagePath().equals(movieForm.getImagePath())){
            existedMovie.setImagePath(movieForm.getImagePath());
        }
        if (!existedMovie.getPrice().equals(movieForm.getPrice())) {
            existedMovie.setPrice(movieForm.getPrice());
        }

        Category category = categoryRepository.findById(movieForm.getCategoryId()).orElse(null);
        if(category != null && category.getKind().equals(Constant.CATEGORY_KIND_MOVIE_TYPE)){
            existedMovie.setCategory(category);
        }

        movieGenreRepository.deleteAllByMovieId(movieForm.getId());
        List<MovieGenre> genresMovieList = new ArrayList<>();
        for (Long genreId : movieForm.getGenreListIds()) {
            Category genreCategory = categoryRepository.findById(genreId).orElse(null);
            if (genreCategory != null && genreCategory.getKind().equals(Constant.CATEGORY_KIND_MOVIE_GENRE)) {
                MovieGenre movieGenre = new MovieGenre();
                movieGenre.setMovie(existedMovie);
                movieGenre.setCategory(genreCategory);
                genresMovieList.add(movieGenre);
            }
        }
        if(!genresMovieList.isEmpty()){
            movieGenreRepository.saveAll(genresMovieList);
        }
        movieMapper.updateMovie(movieForm, existedMovie);
        movieRepository.save(existedMovie);
        apiResponseDto.setMessage("Movie has been updated successfully!");
        apiResponseDto.setData(existedMovie.getId());
        return apiResponseDto;
    }

    @GetMapping(value = "/search-movie", produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiResponseDto<ResponseListDto<MovieDto>> searchMovie(
            @RequestParam(required = false) String title,
            MovieCriteria movieCriteria,
            Pageable pageable) {
        ApiResponseDto<ResponseListDto<MovieDto>> apiResponseDto = new ApiResponseDto<>();

        Page<Movie> moviePage = movieRepository.findAll(movieCriteria.getSpecification(), pageable);

        ResponseListDto<MovieDto> responseListDto = new ResponseListDto(
                movieMapper.toClientMovieDtoList(moviePage.getContent()),
                moviePage.getTotalElements(),
                moviePage.getTotalPages());

        apiResponseDto.setData(responseListDto);
        apiResponseDto.setMessage("Search movie successfully!");
        return apiResponseDto;
    }

}

