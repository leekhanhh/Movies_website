package com.ecommerce.website.movie.controller;

import com.ecommerce.website.movie.dto.ApiResponseDto;
import com.ecommerce.website.movie.dto.ErrorCode;
import com.ecommerce.website.movie.dto.ResponseListDto;
import com.ecommerce.website.movie.dto.participant.ParticipantDto;
import com.ecommerce.website.movie.form.participant.CreateParticipantForm;
import com.ecommerce.website.movie.form.participant.UpdateParticipantForm;
import com.ecommerce.website.movie.mapper.ParticipantMapper;
import com.ecommerce.website.movie.model.criteria.ParticipantCriteria;
import com.ecommerce.website.movie.model.Movie;
import com.ecommerce.website.movie.model.Participant;
import com.ecommerce.website.movie.repository.MovieRepository;
import com.ecommerce.website.movie.repository.ParticipantRepository;
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
@RequestMapping("/v1/participant")
@CrossOrigin(origins = "*", allowedHeaders = "*")
@Slf4j
public class ParticipantController {
    @Autowired
    ParticipantRepository participantRepository;
    @Autowired
    ParticipantMapper participantMapper;
    @Autowired
    MovieRepository movieRepository;
    
    @PostMapping(value = "/create", produces = MediaType.APPLICATION_JSON_VALUE)
    @Transactional
    public ApiResponseDto<ParticipantDto> createParticipant(@Valid @RequestBody CreateParticipantForm participantForm, BindingResult bindingResult) {
        ApiResponseDto<ParticipantDto> apiResponseDto = new ApiResponseDto<>();

        Movie movie = movieRepository.findById(participantForm.getMovieId()).orElse(null);
        if (movie == null) {
            apiResponseDto.setCode(ErrorCode.MOVIE_NOT_FOUND);
            apiResponseDto.setMessage("Movie not found!");
            return apiResponseDto;
        }

        Participant participant = participantMapper.formCreateParticipant(participantForm);
        participant.setMovie(movie);
        participantRepository.save(participant);

        ParticipantDto participantDto = participantMapper.toParticipantDto(participant);
        apiResponseDto.setData(participantDto);
        apiResponseDto.setMessage("Participant created successfully!");
        return apiResponseDto;
    }

    @PutMapping (value = "/update", produces = MediaType.APPLICATION_JSON_VALUE)
    @Transactional
    public ApiResponseDto<ParticipantDto> updateParticipant(@Valid @RequestBody UpdateParticipantForm participantForm, BindingResult bindingResult) {
        ApiResponseDto<ParticipantDto> apiResponseDto = new ApiResponseDto<>();
        Participant participant = participantRepository.findById(participantForm.getId()).orElse(null);
        if (participant != null) {
            participantMapper.updateParticipant(participantForm, participant);
            participantRepository.save(participant);
            ParticipantDto participantDto = participantMapper.toParticipantDto(participant);
            apiResponseDto.setData(participantDto);
            apiResponseDto.setMessage("Participant updated successfully!");
        } else {
            apiResponseDto.setCode(ErrorCode.PARTICIPANT_NOT_FOUND);
            apiResponseDto.setMessage("Participant not found!");
        }
        return apiResponseDto;
    }

    @DeleteMapping (value = "/delete/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @Transactional
    public ApiResponseDto<Long> deleteParticipant(@PathVariable Long id) {
        ApiResponseDto<Long> apiResponseDto = new ApiResponseDto<>();
        Participant participant = participantRepository.findById(id).orElse(null);
        if (participant != null) {
            participantRepository.deleteById(id);
            apiResponseDto.setData(id);
            apiResponseDto.setMessage("Participant deleted successfully!");
        } else {
            apiResponseDto.setCode(ErrorCode.PARTICIPANT_NOT_FOUND);
            apiResponseDto.setMessage("Participant not found!");
        }
        return apiResponseDto;
    }

    @GetMapping (value = "/list", produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiResponseDto<ResponseListDto<ParticipantDto>> listParticipant(ParticipantCriteria participantCriteria, Pageable pageable) {
        ApiResponseDto<ResponseListDto<ParticipantDto>> apiResponseDto = new ApiResponseDto<>();
        Page<Participant> participantPage = participantRepository.findAll(participantCriteria.getSpecification(), pageable);
        ResponseListDto<ParticipantDto> responseListDto = new ResponseListDto(participantMapper.toParticipantDtoList(participantPage.getContent()), participantPage.getTotalElements(), participantPage.getTotalPages());
        apiResponseDto.setData(responseListDto);
        apiResponseDto.setMessage("Get participant list successfully!");
        return apiResponseDto;
    }

}
