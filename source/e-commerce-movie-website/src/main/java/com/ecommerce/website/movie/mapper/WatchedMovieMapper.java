package com.ecommerce.website.movie.mapper;

import com.ecommerce.website.movie.dto.WatchTimeDto;
import com.ecommerce.website.movie.dto.movie.WatchedMovieDto;
import com.ecommerce.website.movie.form.UpdateWatchTimeTrackForm;
import com.ecommerce.website.movie.form.WatchTimeTrackForm;
import com.ecommerce.website.movie.model.WatchedMovies;
import org.mapstruct.*;
import org.mapstruct.Named;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface WatchedMovieMapper {
    @Mapping(source = "movieId", target = "movieId")
    @Mapping(source = "accountId", target = "accountId")
    @Mapping(source = "watchedTime", target = "watchedTime")
    @Mapping(source = "duration", target = "duration")
    @Named("fromCreateWatchedMovieForm")
    @BeanMapping(ignoreByDefault = true)
    WatchedMovies fromCreateWatchedMovieForm(WatchTimeTrackForm watchTimeTrackForm);

    @Mapping(source = "movieId", target = "movieId")
    @Mapping(source = "accountId", target = "accountId")
    @Mapping(source = "watchedTime", target = "watchTime")
    @Mapping(source = "duration", target = "duration")
    @BeanMapping(ignoreByDefault = true)
    @Named("fromEntityToWatchedMovieDto")
    WatchTimeDto toWatchedMovieDto(WatchedMovies watchedMovies);

    @IterableMapping(elementTargetType = WatchTimeDto.class, qualifiedByName = "fromEntityToWatchedMovieDto")
    List<WatchTimeDto> toWatchedMovieDtoList(List<WatchedMovies> watchedMoviesList);

    @Mapping(source = "watchTime", target = "watchedTime")
    @Named("updateWatchedMovie")
    @BeanMapping(ignoreByDefault = true)
    void updateWatchedMovie(UpdateWatchTimeTrackForm updateWatchTimeTrackForm, @MappingTarget WatchedMovies watchedMovies);
}
