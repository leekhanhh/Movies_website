package com.ecommerce.website.movie.mapper;

import com.ecommerce.website.movie.dto.movie.MovieDto;
import com.ecommerce.website.movie.form.movie.CreateMovieForm;
import com.ecommerce.website.movie.form.movie.UpdateMovieForm;
import com.ecommerce.website.movie.model.Movie;
import com.ecommerce.website.movie.model.MovieGenre;
import com.ecommerce.website.movie.model.Rating;
import com.ecommerce.website.movie.model.VoteMovie;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        uses = {MovieGenreMapper.class, CategoryMapper.class, EpisodeMapper.class, VoteMovieMapper.class})
public interface MovieMapper {
    @Mapping(source = "title", target = "title")
    @Mapping(source = "overview", target = "overview")
    @Mapping(source = "price", target = "price")
    @Mapping(source = "imagePath", target = "imagePath")
    @Mapping(source = "videoPath", target = "videoGridFs")
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
    @Mapping(source = "createdDate", target = "createdDate")
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

    private Integer calculateVoteCount(List<VoteMovie> voteMovies) {
        return (voteMovies == null) ? 0 : voteMovies.size();
    }

    @AfterMapping
    default void setVoteCount(@MappingTarget MovieDto movieDto, Movie movie) {
        movieDto.setVoteCount(calculateVoteCount(movie.getVoteMovies()));
    }

    private Double calculateRatingAverage(List<Rating> ratings) {
        if (ratings == null) {
            return 0.0;
        }
        return ratings.stream().mapToDouble(Rating::getEvaluation).average().orElse(0.0);
    }

    @AfterMapping
    default void setRatingAverage(@MappingTarget MovieDto movieDto, Movie movie) {
        movieDto.setRatingAverage(calculateRatingAverage(movie.getRatings()));
    }
}
