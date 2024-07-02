package com.ecommerce.website.movie.mapper;

import com.ecommerce.website.movie.dto.moviegenre.MovieGenreDto;
import com.ecommerce.website.movie.model.MovieGenre;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface MovieGenreMapper {
    @Mapping(source = "movie.id", target = "movieId")
    @Mapping(source = "category.id", target = "categoryId")
    @Mapping(source = "category.name", target = "categoryName")
    @Named("toMovieGenreDto")
    MovieGenreDto toMovieGenreDto(MovieGenre movieGenre);

    @IterableMapping(elementTargetType = MovieGenreDto.class, qualifiedByName = "toMovieGenreDto")
    @Named("toMovieGenreDtoList")
    List<MovieGenreDto> toMovieGenreDtoList(List<MovieGenre> movieGenreList);

}
