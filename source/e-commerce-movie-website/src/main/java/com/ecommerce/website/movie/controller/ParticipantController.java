package com.ecommerce.website.movie.controller;

import com.ecommerce.website.movie.dto.ApiResponseDto;
import com.ecommerce.website.movie.dto.ErrorCode;
import com.ecommerce.website.movie.dto.participant.ParticipantDto;
import com.ecommerce.website.movie.form.participant.CreateParticipantForm;
import com.ecommerce.website.movie.form.participant.UpdateParticipantForm;
import com.ecommerce.website.movie.mapper.ParticipantMapper;
import com.ecommerce.website.movie.model.MovieParticipant;
import com.ecommerce.website.movie.model.Participant;
import com.ecommerce.website.movie.repository.ParticipantRepository;
import lombok.extern.slf4j.Slf4j;
import org.mapstruct.Mapping;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/v1/participant")
@CrossOrigin(origins = "*", allowedHeaders = "*")
@Slf4j
public class ParticipantController {
    @Autowired
    ParticipantRepository participantRepository;
    @Autowired
    ParticipantMapper participantMapper;
    
    @PostMapping(value = "/create", produces = MediaType.APPLICATION_JSON_VALUE)
    @Transactional
    public ApiResponseDto<ParticipantDto> createParticipant(@Valid @RequestBody CreateParticipantForm participantForm, BindingResult bindingResult) {
        ApiResponseDto<ParticipantDto> apiResponseDto = new ApiResponseDto<>();
        Participant participant = participantMapper.formCreateParticipant(participantForm);

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
    public ApiResponseDto<ParticipantDto> deleteParticipant(@PathVariable Long id) {
        ApiResponseDto<ParticipantDto> apiResponseDto = new ApiResponseDto<>();
        Participant participant = participantRepository.findById(id).orElse(null);
        if (participant != null) {
            participantRepository.delete(participant);
            apiResponseDto.setData(participantMapper.toParticipantDto(participant));
            apiResponseDto.setMessage("Participant deleted successfully!");
        } else {
            apiResponseDto.setCode(ErrorCode.PARTICIPANT_NOT_FOUND);
            apiResponseDto.setMessage("Participant not found!");
        }
        return apiResponseDto;
    }


}
