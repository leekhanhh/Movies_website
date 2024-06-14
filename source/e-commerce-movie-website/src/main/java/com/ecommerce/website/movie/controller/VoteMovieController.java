package com.ecommerce.website.movie.controller;

import com.ecommerce.website.movie.dto.ApiResponseDto;
import com.ecommerce.website.movie.dto.ErrorCode;
import com.ecommerce.website.movie.dto.ResponseListDto;
import com.ecommerce.website.movie.dto.movie.MovieDto;
import com.ecommerce.website.movie.dto.votemovie.VoteMovieDto;
import com.ecommerce.website.movie.mapper.MovieMapper;
import com.ecommerce.website.movie.mapper.VoteMovieMapper;
import com.ecommerce.website.movie.model.Account;
import com.ecommerce.website.movie.model.Movie;
import com.ecommerce.website.movie.model.VoteMovie;
import com.ecommerce.website.movie.model.criteria.VoteMovieCriteria;
import com.ecommerce.website.movie.repository.AccountRepository;
import com.ecommerce.website.movie.repository.MovieRepository;
import com.ecommerce.website.movie.repository.VoteMovieRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import javax.persistence.Convert;
import javax.transaction.Transactional;
import javax.validation.Valid;
import java.util.*;

@org.springframework.web.bind.annotation.RestController
@org.springframework.web.bind.annotation.RequestMapping("/v1/vote-movie")
@CrossOrigin(origins = "*", allowedHeaders = "*")
@Slf4j
public class VoteMovieController {
    @Autowired
    VoteMovieRepository voteMovieRepository;
    @Autowired
    VoteMovieMapper voteMovieMapper;
    @Autowired
    MovieRepository movieRepository;
    @Autowired
    AccountRepository accountRepository;
    @Autowired
    MovieMapper movieMapper;

    @PostMapping(value = "/create-vote", produces = org.springframework.http.MediaType.APPLICATION_JSON_VALUE)
    @Transactional
    public ApiResponseDto<Long> createVoteMovie(@Valid @org.springframework.web.bind.annotation.RequestBody com.ecommerce.website.movie.form.votemovie.CreateVoteMovieForm createVoteMovieForm, org.springframework.validation.BindingResult bindingResult) {
        ApiResponseDto<Long> apiResponseDto = new com.ecommerce.website.movie.dto.ApiResponseDto<>();
        Movie movie = movieRepository.findById(createVoteMovieForm.getMovieId()).orElse(null);
        if (movie == null) {
            apiResponseDto.setResult(false);
            apiResponseDto.setCode(com.ecommerce.website.movie.dto.ErrorCode.MOVIE_NOT_FOUND);
            apiResponseDto.setMessage("Movie not found!");
            return apiResponseDto;
        }
        Account account = accountRepository.findById(createVoteMovieForm.getAccountId()).orElse(null);
        if (account == null) {
            apiResponseDto.setResult(false);
            apiResponseDto.setCode(com.ecommerce.website.movie.dto.ErrorCode.ACCOUNT_NOT_FOUND);
            apiResponseDto.setMessage("Account not found!");
            return apiResponseDto;
        }
        if (voteMovieRepository.findByAccountIdAndMovieId(createVoteMovieForm.getMovieId(), createVoteMovieForm.getAccountId()) != null) {
            apiResponseDto.setResult(false);
            apiResponseDto.setCode(ErrorCode.VOTE_MOVIE_EXIST);
            apiResponseDto.setMessage("Vote movie exist!");
            return apiResponseDto;
        }
        VoteMovie voteMovie = voteMovieMapper.fromCreateVoteMovieForm(createVoteMovieForm);
        voteMovieRepository.save(voteMovie);
        apiResponseDto.setResult(true);
        apiResponseDto.setData(voteMovie.getId());
        return apiResponseDto;
    }

    @DeleteMapping(value = "/delete-vote/{accountId}/{movieId}", produces = org.springframework.http.MediaType.APPLICATION_JSON_VALUE)
    @Transactional
    public ApiResponseDto<Long> deleteVoteMovie(@PathVariable Long accountId, @PathVariable Long movieId) {
        ApiResponseDto<Long> apiResponseDto = new ApiResponseDto<>();
        VoteMovie voteMovie = voteMovieRepository.findByAccountIdAndMovieId(accountId, movieId);
        if (voteMovie == null) {
            apiResponseDto.setResult(false);
            apiResponseDto.setCode(ErrorCode.VOTE_MOVIE_NOT_FOUND);
            apiResponseDto.setMessage("Vote movie not found!");
            return apiResponseDto;
        }
        voteMovieRepository.delete(voteMovie);
        apiResponseDto.setResult(true);
        apiResponseDto.setMessage("Vote movie has been deleted!");
        return apiResponseDto;
    }

    @GetMapping(value = "/list-vote", produces = org.springframework.http.MediaType.APPLICATION_JSON_VALUE)
    public ApiResponseDto<ResponseListDto<VoteMovieDto>> listVoteMovie(VoteMovieCriteria voteMovieCriteria, Pageable pageable){
        ApiResponseDto<ResponseListDto<VoteMovieDto>> apiResponseDto = new ApiResponseDto<>();
        Page<VoteMovie> voteMoviePage = voteMovieRepository.findAll(voteMovieCriteria.getSpecification(), pageable);
        ResponseListDto<VoteMovieDto> responseListDto = new ResponseListDto(voteMovieMapper.toVoteMovieDtoList(voteMoviePage.getContent()), voteMoviePage.getTotalElements(), voteMoviePage.getTotalPages());
        apiResponseDto.setData(responseListDto);
        return apiResponseDto;
    }

    @GetMapping(value="/list-vote-movie", produces = org.springframework.http.MediaType.APPLICATION_JSON_VALUE)
    public ApiResponseDto<ResponseListDto<MovieDto>> listMovie(VoteMovieCriteria voteMovieCriteria, Pageable pageable){
        ApiResponseDto<ResponseListDto<MovieDto>> apiResponseDto = new ApiResponseDto<>();
        Page<VoteMovie> voteMoviePage = voteMovieRepository.findAll(voteMovieCriteria.getSpecification(), pageable);
        List<Movie> movieList = movieRepository.findAll();
        Map<Long,Integer> movieIdMap = new LinkedHashMap<>();
        for (Movie movie : movieList) {
            Integer count = 0;
            for (VoteMovie voteMovie : voteMoviePage.getContent()) {
                if (movie.getId().equals(voteMovie.getMovie().getId())) {
                    count++;
                }
            }
            movieIdMap.put(movie.getId(), count);
        }
        List<Movie> sortedMovieList = new ArrayList<>();
        movieIdMap.entrySet().stream()
                .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                .limit(10)
                .forEachOrdered(x -> {
                    sortedMovieList.add(movieRepository.findById(x.getKey()).orElse(null));
                });

        ResponseListDto<MovieDto> responseListDto = new ResponseListDto(movieMapper.toClientMovieDtoList(sortedMovieList), Long.parseLong(String.valueOf(sortedMovieList.size())), voteMoviePage.getTotalPages());
        apiResponseDto.setData(responseListDto);
        return apiResponseDto;
    }
    @GetMapping(value = "/list-vote/{accountId}", produces = org.springframework.http.MediaType.APPLICATION_JSON_VALUE)
    public ApiResponseDto<ResponseListDto<VoteMovieDto>> getVoteMovie(@PathVariable Long accountId, Pageable pageable){
        ApiResponseDto<ResponseListDto<VoteMovieDto>> apiResponseDto = new ApiResponseDto<>();
        Page<VoteMovie> voteMovieList = voteMovieRepository.findAllByAccountId(accountId, pageable);
        ResponseListDto<VoteMovieDto> responseListDto = new ResponseListDto(voteMovieMapper.toMovieDtoList(voteMovieList.getContent()),voteMovieList.getTotalElements(), voteMovieList.getTotalPages());
        apiResponseDto.setData(responseListDto);
        return apiResponseDto;
    }


}
