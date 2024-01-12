package com.ecommerce.website.movie.mapper;

import com.ecommerce.website.movie.dto.movie.participant.MovieParticipantDto;
import com.ecommerce.website.movie.model.MovieParticipant;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        uses = MovieParticipantMapper.class)
public interface MovieParticipantMapper {
//    @Mapping(source = "participant", target = "participant", qualifiedByName = "toMovieParticipantDto")
//    @BeanMapping(ignoreByDefault = true)
//    @Named("toMovieParticipantDto")
//    MovieParticipantDto toMovieParticipantDto(MovieParticipant movieParticipant);
//
//    @IterableMapping(elementTargetType = MovieParticipantDto.class, qualifiedByName = "toMovieParticipantDto")
//    @Named("toMovieParticipantDtoList")
//    List<MovieParticipantDto> toMovieParticipantDtoList(List<MovieParticipant> movieParticipantList);
}
