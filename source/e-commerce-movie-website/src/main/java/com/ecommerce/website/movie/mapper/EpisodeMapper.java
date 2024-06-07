package com.ecommerce.website.movie.mapper;

import com.ecommerce.website.movie.dto.movie.EpisodeDto;
import com.ecommerce.website.movie.form.movie.episode.CreateEpisodeForm;
import com.ecommerce.website.movie.form.movie.episode.UpdateEpisodeForm;
import com.ecommerce.website.movie.model.SubMovie;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface EpisodeMapper {
    @Mapping(source = "title", target = "title")
    @Mapping(source = "description", target = "description")
    @Mapping(source = "url", target = "url")
    @Mapping(source = "episodeNumber", target = "episodeNumber")
    @Mapping(source = "movieId", target = "movie.id")
    @BeanMapping(ignoreByDefault = true)
    @Named("formCreateEpisode")
    SubMovie formCreateEpisode(CreateEpisodeForm episodeForm);

    @Mapping(source = "title", target = "title")
    @Mapping(source = "description", target = "description")
    @Mapping(source = "url", target = "url")
    @Mapping(source = "episodeNumber", target = "episodeNumber")
    @Mapping(source = "movie.id", target = "movieId")
    @BeanMapping(ignoreByDefault = true)
    @Named("toEpisodeDto")
    EpisodeDto toEpisodeDto(SubMovie episode);

    @IterableMapping(elementTargetType = EpisodeDto.class, qualifiedByName = "toEpisodeDto")
    @Named("toEpisodeList")
    List<EpisodeDto> toEpisodeList(List<SubMovie> episodeList);

    @Mapping(source = "title", target = "title")
    @Mapping(source = "description", target = "description")
    @Mapping(source = "url", target = "url")
    @Mapping(source = "episodeNumber", target = "episodeNumber")
    @BeanMapping(ignoreByDefault = true)
    void mappingUpdateForm(UpdateEpisodeForm updateEpisodeForm, @MappingTarget SubMovie episode);
}
