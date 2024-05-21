package com.ecommerce.website.movie.controller;

import com.ecommerce.website.movie.dto.ApiResponseDto;
import com.ecommerce.website.movie.dto.ErrorCode;
import com.ecommerce.website.movie.dto.ResponseListDto;
import com.ecommerce.website.movie.dto.movie.MovieDto;
import com.ecommerce.website.movie.form.movie.CreateMovieForm;
import com.ecommerce.website.movie.form.movie.UpdateMovieForm;
import com.ecommerce.website.movie.mapper.MovieMapper;
import com.ecommerce.website.movie.model.*;
import com.ecommerce.website.movie.model.Criteria.MovieCriteria;
import com.ecommerce.website.movie.repository.CategoryRepository;
import com.ecommerce.website.movie.repository.MovieRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.MediaType;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.Arrays;
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
    CategoryMovieRepository categoryMovieRepository;
    @Autowired
    CategoryMovieMapper categoryMovieMapper;

    @PostMapping(value = "/create", produces = MediaType.APPLICATION_JSON_VALUE)
    @Transactional
    public ApiResponseDto<MovieDto> createMovie(@Valid @RequestBody CreateMovieForm movieForm, BindingResult bindingResult) {
        ApiResponseDto<MovieDto> apiResponseDto = new ApiResponseDto<>();
        Movie movie = movieMapper.formCreateMovie(movieForm);

        List<CategoryMovie> categoryMovieList = new ArrayList<>();
        for (Long categoryId : movieForm.getCategoryListIds()) {
            Category category = categoryRepository.findById(categoryId).orElse(null);
            if (category != null) {
                CategoryMovie categoryMovie = new CategoryMovie();
                categoryMovie.setCategory(category);
                categoryMovie.setMovie(movie);
                categoryMovieList.add(categoryMovie);
            }
        }
        movieRepository.save(movie);
        categoryMovieRepository.saveAll(categoryMovieList);
        MovieDto movieDto = movieMapper.toServerMovieDto(movie);
        movieDto.setCategoryMovieList(categoryMovieMapper.toCategoryMovieDtoList(categoryMovieList));
        apiResponseDto.setMessage("New movie added successfully!");
        apiResponseDto.setData(movieDto);
        return apiResponseDto;
    }

    @DeleteMapping(value = "/delete/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @Transactional
    public ApiResponseDto<MovieDto> deleteMovie(@PathVariable Long id) {
        ApiResponseDto<MovieDto> apiResponseDto = new ApiResponseDto<>();
        Movie movie = movieRepository.findById(id).orElse(null);
        if (movie != null) {
            movieRepository.delete(movie);
            apiResponseDto.setMessage("Movie deleted successfully!");
            apiResponseDto.setData(movieMapper.toServerMovieDto(movie));
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
        Page<Movie> moviePage = movieRepository.findAll(movieCriteria.getSpecification(), pageable);
        ResponseListDto<MovieDto> responseListDto = new ResponseListDto(movieMapper.toServerMovieDtoList(moviePage.getContent()), moviePage.getTotalElements(), moviePage.getTotalPages());
        apiResponseDto.setData(responseListDto);
        apiResponseDto.setMessage("Get movie list successfully!");
        return apiResponseDto;
    }

    @GetMapping(value = "/list-client", produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiResponseDto<ResponseListDto<MovieDto>> listClientMovie(MovieCriteria movieCriteria, Pageable pageable) {
        ApiResponseDto<ResponseListDto<MovieDto>> apiResponseDto = new ApiResponseDto<>();
        Page<Movie> moviePage = movieRepository.findAll(movieCriteria.getSpecification(), pageable);
        ResponseListDto<MovieDto> responseListDto = new ResponseListDto(movieMapper.toClientMovieDtoList(moviePage.getContent()), moviePage.getTotalElements(), moviePage.getTotalPages());
        apiResponseDto.setData(responseListDto);
        apiResponseDto.setMessage("Get movie list successfully!");
        return apiResponseDto;
    }

    @PutMapping(value = "/update", produces = MediaType.APPLICATION_JSON_VALUE)
    @Transactional
    public ApiResponseDto<MovieDto> updateMovie(@Valid @RequestBody UpdateMovieForm movieForm, BindingResult bindingResult) {
        ApiResponseDto<MovieDto> apiResponseDto = new ApiResponseDto<>();
        Movie movie = movieRepository.findById(movieForm.getId()).orElse(null);
        if (movie == null) {
            apiResponseDto.setResult(false);
            apiResponseDto.setCode(ErrorCode.MOVIE_NOT_FOUND);
            apiResponseDto.setMessage("Movie not found");
            return apiResponseDto;
        }
        if (!movie.getTitle().equals(movieForm.getTitle())) {
            movie.setTitle(movieForm.getTitle());
        }
        List<CategoryMovie> categoryMovieList = categoryMovieRepository.findAllByMovieId(movie.getId());
        Long[] currentCategoryListIds = new Long[categoryMovieList.size()];
        for (CategoryMovie categoryMovie : categoryMovieList) {
            currentCategoryListIds[categoryMovieList.indexOf(categoryMovie)] = categoryMovie.getCategory().getId();
        }
        if (!Arrays.equals(currentCategoryListIds, movieForm.getCategoryListIds())) {
            categoryMovieRepository.deleteAll(categoryMovieList);
            categoryMovieList = new ArrayList<>();
            for (Long categoryId : movieForm.getCategoryListIds()) {
                Category category = categoryRepository.findById(categoryId).orElse(null);
                if (category != null) {
                    CategoryMovie categoryMovie = new CategoryMovie();
                    categoryMovie.setCategory(category);
                    categoryMovie.setMovie(movie);
                    categoryMovieList.add(categoryMovie);

                }
            }
            categoryMovieRepository.saveAll(categoryMovieList);
        }

        movieMapper.updateMovie(movieForm, movie);
        movieRepository.save(movie);
        movie.setCategoryMovieList(categoryMovieList);
        apiResponseDto.setData(movieMapper.toServerMovieDto(movie));
        apiResponseDto.setMessage("Update movie successfully!");
        return apiResponseDto;
    }
}
