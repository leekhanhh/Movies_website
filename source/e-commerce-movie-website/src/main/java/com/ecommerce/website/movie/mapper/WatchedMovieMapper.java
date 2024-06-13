package com.ecommerce.website.movie.mapper;

import com.ecommerce.website.movie.dto.movie.WatchedMovieDto;
import com.ecommerce.website.movie.form.movie.watchedmovie.CreateWatchedMovieForm;
import com.ecommerce.website.movie.model.WatchedMovies;
import org.mapstruct.*;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE, uses = {MovieMapper.class, MovieGenreMapper.class})
public interface WatchedMovieMapper {
    @Mapping(source = "movieId", target = "movie.id")
    @Mapping(source = "accountId", target = "accountId")
    WatchedMovies fromCreateWatchedMovieForm(CreateWatchedMovieForm createWatchedMovieForm);

    @Mapping(source = "movie.id", target = "movie.id")
    @Mapping(source = "accountId", target = "accountId")
    @Mapping(source = "movie.genres", target = "movie.genres", qualifiedByName = "toMovieGenreDto")
    @Mapping(source = "movie.title", target = "movie.title")
    @Mapping(source = "movie.category", target = "movie.category")
    @Mapping(source = "movie.videoGridFs", target = "movie.videoGridFs")
    @Mapping(source = "movie.imagePath", target = "movie.imagePath")
    @Mapping(source = "createdDate", target = "createdDate", dateFormat = "dd-MM-yyyy HH:mm:ss")
    @Named("toWatchedMovieDto")
    @BeanMapping(ignoreByDefault = true)
    WatchedMovieDto toWatchedMovieDto(WatchedMovies watchedMovies);

    @IterableMapping(qualifiedByName = "toWatchedMovieDto", elementTargetType = WatchedMovieDto.class)
    Iterable<WatchedMovieDto> toWatchedMovieDtoList(Iterable<WatchedMovies> watchedMovies);
}
