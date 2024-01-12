package com.ecommerce.website.movie.mapper;

import com.ecommerce.website.movie.dto.movie.MovieDto;
import com.ecommerce.website.movie.form.movie.CreateMovieForm;
import com.ecommerce.website.movie.form.movie.UpdateMovieForm;
import com.ecommerce.website.movie.model.Movie;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        uses = CategoryMovieMapper.class)
public interface MovieMapper {
    @Mapping(source = "title", target = "title")
    @Mapping(source = "overview", target = "overview")
    @Mapping(source = "price", target = "price")
    @Mapping(source = "imagePath", target = "imagePath")
    @BeanMapping(ignoreByDefault = true)
    Movie formCreateMovie(CreateMovieForm movieForm);

    @Mapping(source = "id", target = "id")
    @Mapping(source = "title", target = "title")
    @Mapping(source = "overview", target = "overview")
    @Mapping(source = "price", target = "price")
    @Mapping(source = "imagePath", target = "imagePath")
    @Mapping(source = "categoryMovieList", target = "categoryMovieList", qualifiedByName = "toCategoryMovieDtoList")
    @Mapping(source = "status", target = "status")
    @BeanMapping(ignoreByDefault = true)
    @Named("toClientMovieDto")
    MovieDto toClientMovieDto(Movie movie);

    @IterableMapping(elementTargetType = MovieDto.class, qualifiedByName = "toClientMovieDto")
    @Named("toClientMovieDtoList")
    List<MovieDto> toClientMovieDtoList(List<Movie> movieList);

    @Mapping(source = "id", target = "id")
    @Mapping(source = "title", target = "title")
    @Mapping(source = "overview", target = "overview")
    @Mapping(source = "price", target = "price")
    @Mapping(source = "imagePath", target = "imagePath")
    @Mapping(source = "categoryMovieList", target = "categoryMovieList", qualifiedByName = "toCategoryMovieDtoList")
    @Mapping(source = "status", target = "status")
    @Mapping(source = "createdDate", target = "createdDate")
    @Mapping(source = "modifiedDate", target = "modifiedDate")
    @BeanMapping(ignoreByDefault = true)
    @Named("toServerMovieDto")
    MovieDto toServerMovieDto(Movie movie);

    @IterableMapping(elementTargetType = MovieDto.class, qualifiedByName = "toServerMovieDto")
    @Named("toServerMovieDtoList")
    List<MovieDto> toServerMovieDtoList(List<Movie> movieList);

    @Mapping(source = "title", target = "title")
    @Mapping(source = "overview", target = "overview")
    @Mapping(source = "price", target = "price")
    @Mapping(source = "imagePath", target = "imagePath")
    @Mapping(source = "status", target = "status")
    @BeanMapping(ignoreByDefault = true)
    void updateMovie(UpdateMovieForm movieForm, @MappingTarget Movie movie);
}
