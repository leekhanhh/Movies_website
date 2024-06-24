package com.ecommerce.website.movie.controller;

import com.ecommerce.website.movie.dto.ApiResponseDto;
import com.ecommerce.website.movie.dto.ErrorCode;
import com.ecommerce.website.movie.dto.ResponseListDto;
import com.ecommerce.website.movie.dto.movie.EpisodeDto;
import com.ecommerce.website.movie.form.movie.episode.CreateEpisodeForm;
import com.ecommerce.website.movie.form.movie.episode.UpdateEpisodeForm;
import com.ecommerce.website.movie.mapper.EpisodeMapper;
import com.ecommerce.website.movie.model.Movie;
import com.ecommerce.website.movie.model.SubMovie;
import com.ecommerce.website.movie.model.criteria.EpisodeCriteria;
import com.ecommerce.website.movie.repository.MovieRepository;
import com.ecommerce.website.movie.repository.SubmovieRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import javax.validation.Valid;

@RestController
@RequestMapping("/v1/episode")
@CrossOrigin(origins = "*", allowedHeaders = "*")
@Slf4j
public class EpisodeController {
    @Autowired
    SubmovieRepository episodeRepository;
    @Autowired
    EpisodeMapper episodeMapper;
    @Autowired
    MovieRepository movieRepository;

    @PostMapping(value = "/create-episode", produces = MediaType.APPLICATION_JSON_VALUE)
    @Transactional
    public ApiResponseDto<EpisodeDto> createEpisode(@Valid @RequestBody CreateEpisodeForm episodeForm, BindingResult bindingResult) {
        ApiResponseDto<EpisodeDto> apiResponseDto = new ApiResponseDto<>();
        SubMovie episode = episodeRepository.findByEpisodeNumberAndMovieId(episodeForm.getEpisodeNumber(), episodeForm.getMovieId());
        if (episode != null) {
            apiResponseDto.setResult(false);
            apiResponseDto.setCode(ErrorCode.DUPLICATED_EPISODE_NUMBER_ERROR);
            apiResponseDto.setMessage("Duplicated Episode Error");
            return apiResponseDto;
        }

        Movie movie = movieRepository.findById(episodeForm.getMovieId()).orElse(null);
        if (movie == null) {
            apiResponseDto.setResult(false);
            apiResponseDto.setCode(ErrorCode.MOVIE_NOT_FOUND);
            apiResponseDto.setMessage("Movie Not Found Error");
            return apiResponseDto;
        }
        if (!movie.getCategory().getName().trim().toLowerCase().equals("series"))
        {
            apiResponseDto.setResult(false);
            apiResponseDto.setCode(ErrorCode.MOVIE_NOT_SERIES);
            apiResponseDto.setMessage("Movie Not Series Error");
            return apiResponseDto;
        }

        SubMovie subMovie = episodeMapper.formCreateEpisode(episodeForm);
        episodeRepository.save(subMovie);

        apiResponseDto.setResult(true);
        apiResponseDto.setMessage("Success");
        apiResponseDto.setData(episodeMapper.toEpisodeDto(subMovie));
        return apiResponseDto;
    }

    @GetMapping(value = "/get-episode-list-by-movie", produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiResponseDto<ResponseListDto<SubMovie>> getEpisodeListByMovie(EpisodeCriteria episodeCriteria, Pageable pageable) {
        ApiResponseDto<ResponseListDto<SubMovie>> apiResponseDto = new ApiResponseDto<>();
        Page<SubMovie> episodePage = episodeRepository.findAll(episodeCriteria.getSpecification(), pageable);
        pageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), Sort.by(Sort.Direction.ASC, "episodeNumber"));
        ResponseListDto<SubMovie> responseListDto = new ResponseListDto(episodeMapper.toEpisodeList(episodePage.getContent()), episodePage.getTotalElements(), episodePage.getTotalPages());
        apiResponseDto.setResult(true);
        apiResponseDto.setData(responseListDto);
        apiResponseDto.setMessage("Success");
        return apiResponseDto;
    }

    @DeleteMapping(value = "/delete-episode/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @Transactional
    public ApiResponseDto<EpisodeDto> deleteEpisode(@PathVariable Long id) {
        ApiResponseDto<EpisodeDto> apiResponseDto = new ApiResponseDto<>();
        SubMovie episode = episodeRepository.findById(id).orElse(null);
        if (episode == null) {
            apiResponseDto.setResult(false);
            apiResponseDto.setCode(ErrorCode.EPISODE_NOT_FOUND);
            apiResponseDto.setMessage("Episode Not Found Error");
            return apiResponseDto;
        }
        episodeRepository.delete(episode);
        apiResponseDto.setResult(true);
        apiResponseDto.setMessage("Success");
        return apiResponseDto;
    }

    @PutMapping(value = "/update-episode", produces = MediaType.APPLICATION_JSON_VALUE)
    @Transactional
    public ApiResponseDto<EpisodeDto> updateEpisode(@Valid @RequestBody UpdateEpisodeForm updateEpisodeForm, BindingResult bindingResult) {
        ApiResponseDto<EpisodeDto> apiResponseDto = new ApiResponseDto<>();
        SubMovie episode = episodeRepository.findById(updateEpisodeForm.getId()).orElse(null);
        if (episode == null) {
            apiResponseDto.setResult(false);
            apiResponseDto.setCode(ErrorCode.EPISODE_NOT_FOUND);
            apiResponseDto.setMessage("Episode Not Found Error");
            return apiResponseDto;
        }
        episodeMapper.mappingUpdateForm(updateEpisodeForm, episode);
        episodeRepository.save(episode);
        episodeRepository.updateEpisodeMovieId(updateEpisodeForm.getId(), updateEpisodeForm.getMovieId());
        apiResponseDto.setResult(true);
        apiResponseDto.setMessage("Success");
        return apiResponseDto;
    }



}
