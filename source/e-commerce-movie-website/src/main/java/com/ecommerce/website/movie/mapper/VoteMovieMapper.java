package com.ecommerce.website.movie.mapper;

import com.ecommerce.website.movie.dto.votemovie.VoteMovieDto;
import com.ecommerce.website.movie.form.votemovie.CreateVoteMovieForm;
import com.ecommerce.website.movie.model.VoteMovie;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface VoteMovieMapper {
    @Mapping(source = "movieId", target = "movie.id")
    @Mapping(source = "accountId", target = "account.id")
    VoteMovie fromCreateVoteMovieForm(CreateVoteMovieForm createVoteMovieForm);

    @Mapping(source = "movie.id", target = "movie.id")
    @Mapping(source = "account.id", target = "account.id")
    @Mapping(source = "createdDate", target = "createdDate")
    @Mapping(source = "modifiedDate", target = "modifiedDate")
    @BeanMapping(ignoreByDefault = true)
    @Named("toVoteMovieDto")
    VoteMovieDto toVoteMovieDto(VoteMovie voteMovie);

    @IterableMapping(elementTargetType = VoteMovieDto.class, qualifiedByName = "toVoteMovieDto")
    List<VoteMovieDto> toVoteMovieDtoList(List<VoteMovie> voteMovieList);




}
