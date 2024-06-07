package com.ecommerce.website.movie.mapper;

import com.ecommerce.website.movie.dto.movie.MovieDto;
import com.ecommerce.website.movie.form.movie.CreateMovieForm;
import com.ecommerce.website.movie.form.movie.UpdateMovieForm;
import com.ecommerce.website.movie.model.Movie;
import com.ecommerce.website.movie.model.MovieGenre;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        uses = {MovieGenreMapper.class, CategoryMapper.class, EpisodeMapper.class})
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
    @Mapping(source = "videoGridFs", target = "videoGridFs")
    @Mapping(source = "status", target = "status")
    @Mapping(source = "category", target = "category", qualifiedByName = "toCategoryMovieDto")
    @Mapping(source = "genres", target = "genres", qualifiedByName = "toMovieGenreDtoList")
    @Mapping(source = "subMovies", target = "episodes", qualifiedByName = "toEpisodeList")
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
    @Mapping(source = "category", target = "category", qualifiedByName = "toCategoryDto")
    @Mapping(source = "genres", target = "genres", qualifiedByName = "toMovieGenreDtoList")
    @Mapping(source = "subMovies", target = "episodes", qualifiedByName = "toEpisodeList")
    @Mapping(source = "videoGridFs", target = "videoGridFs")
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
    @Mapping(source = "videoGridFs", target = "videoGridFs")
    @BeanMapping(ignoreByDefault = true)
    void updateMovie(UpdateMovieForm movieForm, @MappingTarget Movie movie);

    @Mapping(source = "id", target = "id")
    @Mapping(source = "title", target = "title")
    @Named("autoCompleteDtoMovie")
    @BeanMapping(ignoreByDefault = true)
    MovieDto autoCompleteDtoMovie(Movie movie);

}
