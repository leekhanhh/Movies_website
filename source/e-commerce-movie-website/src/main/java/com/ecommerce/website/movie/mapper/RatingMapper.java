package com.ecommerce.website.movie.mapper;

import com.ecommerce.website.movie.dto.rating.RatingDto;
import com.ecommerce.website.movie.form.rating.CreateRatingForm;
import com.ecommerce.website.movie.form.rating.UpdateRatingForm;
import com.ecommerce.website.movie.form.review.UpdateReviewForm;
import com.ecommerce.website.movie.model.Rating;
import org.mapstruct.*;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface RatingMapper {
    @Mapping(source = "movieId", target = "movie.id")
    @Mapping(source = "accountId", target = "account.id")
    @Mapping(source = "evaluation", target = "evaluation")
    Rating fromRatingForm(CreateRatingForm createRatingForm);

    @Mapping(source = "movie.id", target = "movie.id")
    @Mapping(source = "account.id", target = "account.id")
    @Mapping(source = "evaluation", target = "evaluation")
    @Mapping(source = "createdDate", target = "createdDate")
    @Mapping(source = "modifiedDate", target = "modifiedDate")
    @BeanMapping(ignoreByDefault = true)
    @Named("toRatingDto")
    RatingDto toRatingDto(Rating rating);
    @Mapping(source = "movie.id", target = "movie.id")
    @BeanMapping(ignoreByDefault = true)
    @Named("toRatingDtoRecommendedMovieList")
    RatingDto toRatingDtoRecommendedMovieList(Rating rating);

    @IterableMapping(elementTargetType = RatingDto.class, qualifiedByName = "toRatingDto")
    @Named("toRatingDtoList")
    List<RatingDto> toRatingDtoList(List<Rating> ratingList);

    @IterableMapping(elementTargetType = RatingDto.class, qualifiedByName = "toRatingDtoRecommendedMovieList")
    @Named("toRatingDtoRecommendedMovieList")
    HashSet<RatingDto> toRatingDtoRecommendedMovieList(Set<Rating> ratingList);

    @Mapping(source = "evaluation", target = "evaluation")
    @Named("updateRatingFromRatingForm")
    void updateRatingFromRatingForm(UpdateRatingForm updateRatingForm, @MappingTarget Rating rating);

}
