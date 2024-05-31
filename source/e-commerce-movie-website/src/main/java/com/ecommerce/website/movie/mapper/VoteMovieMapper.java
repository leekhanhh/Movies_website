package com.ecommerce.website.movie.mapper;

import com.ecommerce.website.movie.dto.movie.MovieDto;
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

    @Mapping(source = "account.id", target = "account.id")
    @Mapping(source = "movie.id", target = "movie.id")
    @Mapping(source = "createdDate", target = "createdDate")
    @Mapping(source = "modifiedDate", target = "modifiedDate")
    @BeanMapping(ignoreByDefault = true)
    @Named("toVoteMovieDto")
    VoteMovieDto toVoteMovieDto(VoteMovie voteMovie);

    @Mapping(source = "movie.id", target = "id")
    @Mapping(source = "movie.title", target = "title")
    @Mapping(source = "movie.overview", target = "overview")
    @Mapping(source = "movie.price", target = "price")
    @Mapping(source = "movie.imagePath", target = "imagePath")
    @Mapping(source = "movie.category", target = "category")
    @Mapping(source = "movie.genres", target = "genres")
    @Named("fromVoteMovieToMovieDto")
    MovieDto fromVoteMovieToMovieDto(VoteMovie voteMovie);

    @IterableMapping(elementTargetType = VoteMovieDto.class, qualifiedByName = "toVoteMovieDto")
    List<VoteMovieDto> toVoteMovieDtoList(List<VoteMovie> voteMovieList);

    @IterableMapping(elementTargetType = MovieDto.class, qualifiedByName = "fromVoteMovieToMovieDto")
    List<MovieDto> toMovieDtoList(List<VoteMovie> voteMovieList);

}
