package com.ecommerce.website.movie.controller;

import com.ecommerce.website.movie.dto.ApiResponseDto;
import com.ecommerce.website.movie.dto.ErrorCode;
import com.ecommerce.website.movie.dto.ResponseListDto;
import com.ecommerce.website.movie.dto.favoritelist.FavoriteListDto;
import com.ecommerce.website.movie.dto.movie.MovieDto;
import com.ecommerce.website.movie.form.favoritelist.CreateFavoriteListForm;
import com.ecommerce.website.movie.mapper.FavoriteListMapper;
import com.ecommerce.website.movie.model.Account;
import com.ecommerce.website.movie.model.FavoriteList;
import com.ecommerce.website.movie.model.Movie;
import com.ecommerce.website.movie.model.criteria.FavoriteListCriteria;
import com.ecommerce.website.movie.repository.AccountRepository;
import com.ecommerce.website.movie.repository.FavoriteListRepository;
import com.ecommerce.website.movie.repository.MovieRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import javax.validation.Valid;

@RestController
@RequestMapping("/v1/favorite-list")
@CrossOrigin(origins = "*", allowedHeaders = "*")
@Slf4j
public class FavoriteListController {
    @Autowired
    FavoriteListRepository favoriteListRepository;
    @Autowired
    FavoriteListMapper favoriteListMapper;
    @Autowired
    AccountRepository accountRepository;
    @Autowired
    MovieRepository movieRepository;

    @PostMapping(value = "/create", produces = MediaType.APPLICATION_JSON_VALUE)
    @Transactional
    public ApiResponseDto<Long> createFavoriteList(@Valid @RequestBody CreateFavoriteListForm favoriteListForm, BindingResult bindingResult) {
        ApiResponseDto<Long> apiResponseDto = new ApiResponseDto<>();
        Account account = accountRepository.findById(favoriteListForm.getAccountId()).orElse(null);
        if (account == null) {
            apiResponseDto.setResult(false);
            apiResponseDto.setCode(ErrorCode.ACCOUNT_NOT_FOUND);
            apiResponseDto.setMessage("Account not found!");
            return apiResponseDto;
        }
        Movie movie = movieRepository.findById(favoriteListForm.getMovieId()).orElse(null);
        if (movie == null) {
            apiResponseDto.setResult(false);
            apiResponseDto.setCode(ErrorCode.MOVIE_NOT_FOUND);
            apiResponseDto.setMessage("Movie not found!");
            return apiResponseDto;
        }
        if (favoriteListRepository.findByAccountIdAndMovieId(favoriteListForm.getAccountId(), favoriteListForm.getMovieId()) != null) {
            apiResponseDto.setResult(false);
            apiResponseDto.setCode(ErrorCode.FAVORITE_ITEM_ALREADY_EXIST);
            apiResponseDto.setMessage("Favorite item already exist!");
            return apiResponseDto;
        }

        FavoriteList favoriteList = favoriteListMapper.fromCreateFavoriteItem(favoriteListForm);
        favoriteList.setAccount(account);
        favoriteList.setMovie(movie);
        favoriteListRepository.save(favoriteList);
        return apiResponseDto;
    }

    @DeleteMapping(value = "/delete/{accountId}/{movieId}", produces = MediaType.APPLICATION_JSON_VALUE)
    @Transactional
    public ApiResponseDto<Long> deleteFavoriteList(@PathVariable Long movieId, @PathVariable Long accountId) {
        ApiResponseDto<Long> apiResponseDto = new ApiResponseDto<>();

        FavoriteList favoriteList = favoriteListRepository.findByAccountIdAndMovieId(accountId, movieId);

        if (favoriteList == null) {
            apiResponseDto.setResult(false);
            apiResponseDto.setCode(ErrorCode.FAVORITE_ITEM_NOT_FOUND);
            apiResponseDto.setMessage("Favorite item not found!");
            return apiResponseDto;
        }

        favoriteListRepository.delete(favoriteList);

        apiResponseDto.setResult(true);
        apiResponseDto.setMessage("Favorite item deleted successfully!");
        return apiResponseDto;
    }


    @GetMapping(value = "/list", produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiResponseDto<ResponseListDto<FavoriteListDto>> listFavoriteItem(FavoriteListCriteria favoriteListCriteria, Pageable pageable) {
        ApiResponseDto<ResponseListDto<FavoriteListDto>> apiResponseDto = new ApiResponseDto<>();
        Page<FavoriteList> favoriteListPage = favoriteListRepository.findAll(favoriteListCriteria.getSpecification(), pageable);
        ResponseListDto<FavoriteListDto> responseListDto = new ResponseListDto(favoriteListMapper.toDtoList(favoriteListPage.getContent()), favoriteListPage.getTotalElements(), favoriteListPage.getTotalPages());
        apiResponseDto.setData(responseListDto);
        return apiResponseDto;
    }

    @GetMapping(value = "/list-movie", produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiResponseDto<ResponseListDto<MovieDto>> listMovie(FavoriteListCriteria favoriteListCriteria, Pageable pageable) {
        ApiResponseDto<ResponseListDto<MovieDto>> apiResponseDto = new ApiResponseDto<>();
        Page<FavoriteList> favoriteListPage = favoriteListRepository.findAll(favoriteListCriteria.getSpecification(), pageable);
        ResponseListDto<MovieDto> responseListDto = new ResponseListDto(favoriteListMapper.toMovieDtoList(favoriteListPage.getContent()), favoriteListPage.getTotalElements(), favoriteListPage.getTotalPages());
        apiResponseDto.setData(responseListDto);
        return apiResponseDto;
    }
}
