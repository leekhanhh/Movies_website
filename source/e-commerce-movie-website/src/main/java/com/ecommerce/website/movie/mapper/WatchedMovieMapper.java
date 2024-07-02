package com.ecommerce.website.movie.mapper;

import com.ecommerce.website.movie.dto.movie.WatchedMovieDto;
import com.ecommerce.website.movie.form.movie.watchedmovie.CreateWatchedMovieForm;
import com.ecommerce.website.movie.model.Movie;
import com.ecommerce.website.movie.model.WatchedMovies;
import org.mapstruct.*;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        uses = {MovieMapper.class, MovieGenreMapper.class})
public interface WatchedMovieMapper {
    @Mapping(source = "movieId", target = "movieId")
    @Mapping(source = "accountId", target = "accountId")
    WatchedMovies fromCreateWatchedMovieForm(CreateWatchedMovieForm createWatchedMovieForm);

    @Mapping(source = "movieId", target = "movieId")
    @Mapping(source = "accountId", target = "accountId")
    @Named("toWatchedMovieDto")
    @BeanMapping(ignoreByDefault = true)
    WatchedMovieDto toWatchedMovieDto(WatchedMovies watchedMovies);

    @IterableMapping(qualifiedByName = "toWatchedMovieDto", elementTargetType = WatchedMovieDto.class)
    Iterable<WatchedMovieDto> toWatchedMovieDtoList(Iterable<WatchedMovies> watchedMovies);
}
