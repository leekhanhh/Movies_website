package com.ecommerce.website.movie.mapper;

import com.ecommerce.website.movie.dto.participant.ParticipantDto;
import com.ecommerce.website.movie.form.participant.CreateParticipantForm;
import com.ecommerce.website.movie.form.participant.UpdateParticipantForm;
import com.ecommerce.website.movie.model.Participant;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE, nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE, uses = {MovieMapper.class})
public interface ParticipantMapper {
    @Mapping(source = "name", target = "name")
    @Mapping(source = "image", target = "image")
    @Mapping(source = "kind", target = "kind")
    @Mapping(source = "filmCharacter", target = "filmCharacter")
    @BeanMapping(ignoreByDefault = true)
    Participant formCreateParticipant(CreateParticipantForm participantForm);

    @Mapping(source = "name", target = "name")
    @Mapping(source = "image", target = "image")
    @Mapping(source = "kind", target = "kind")
    @Mapping(source = "filmCharacter", target = "filmCharacter")
    @BeanMapping(ignoreByDefault = true)
    void updateParticipant(UpdateParticipantForm participantForm, @MappingTarget Participant participant);

    @Mapping(source = "id", target = "id")
    @Mapping(source = "name", target = "name")
    @Mapping(source = "image", target = "image")
    @Mapping(source = "kind", target = "kind")
    @Mapping(source = "filmCharacter", target = "filmCharacter")
    @Mapping(source = "movie", target = "movie", qualifiedByName = "autoCompleteDtoMovie")
    @BeanMapping(ignoreByDefault = true)
    @Named("toParticipantDto")
    ParticipantDto toParticipantDto(Participant participant);

    @IterableMapping(elementTargetType = ParticipantDto.class, qualifiedByName = "toParticipantDto")
    @Named("toParticipantDtoList")
    List<ParticipantDto> toParticipantDtoList(List<Participant> participantList);
}
