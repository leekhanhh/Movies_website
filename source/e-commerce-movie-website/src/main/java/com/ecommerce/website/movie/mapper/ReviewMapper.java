package com.ecommerce.website.movie.mapper;

import com.ecommerce.website.movie.dto.review.ReviewDto;
import com.ecommerce.website.movie.form.review.CreateReviewFrom;
import com.ecommerce.website.movie.form.review.UpdateReviewForm;
import com.ecommerce.website.movie.model.Review;
import org.mapstruct.*;

import java.util.List;
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface ReviewMapper {
    @Mapping(source = "movieId", target = "movie.id")
    @Mapping(source = "accountId", target = "account.id")
    @Mapping(source = "content", target = "content")
    @Named("fromCreateReviewEntity")
    Review fromCreateReviewEntity(CreateReviewFrom createReviewEntity);

    @Mapping(source = "id", target = "id")
    @Mapping(source = "account.id", target = "account.id")
    @Mapping(source = "content", target = "content")
    @Mapping(source = "createdDate", target = "createdDate")
    @Mapping(source = "modifiedDate", target = "modifiedDate")
    @BeanMapping(ignoreByDefault = true)
    @Named("fromReviewEntityToDto")
    ReviewDto fromReviewEntityToDto(Review review);


    @Mapping(source = "movie.id", target = "movie.id")
    @Mapping(source = "content", target = "content")
    @BeanMapping(ignoreByDefault = true)
    @Named("fromReviewEntityToDtoByAccount")
    ReviewDto fromReviewEntityToDtoByAccount(Review review);

    @IterableMapping(elementTargetType = ReviewDto.class, qualifiedByName = "fromReviewEntityToDtoByAccount")
    List<ReviewDto> fromReviewEntityListToDtoListByAccount(List<Review> reviewList);

    @IterableMapping(elementTargetType = ReviewDto.class, qualifiedByName = "fromReviewEntityToDto")
    List<ReviewDto> fromReviewEntityListToDtoList(List<Review> reviewList);

    @Mapping(source = "content", target = "content")
    @BeanMapping(ignoreByDefault = true)
    @Named("updateReviewEntity")
    void updateReviewEntity(UpdateReviewForm updateReviewForm, @MappingTarget Review review);

    @Mapping(source = "id", target = "id")
    @BeanMapping(ignoreByDefault = true)
    @Named("fromReviewEntityJustIdToDto")
    ReviewDto fromReviewEntityJustIdToDto(Review review);
}
