package com.ecommerce.website.movie.mapper;

import com.ecommerce.website.movie.dto.likereview.LikeReviewDto;
import com.ecommerce.website.movie.form.likedreview.CreateLikedReviewForm;
import com.ecommerce.website.movie.form.likedreview.UpdateLikedReviewForm;
import com.ecommerce.website.movie.model.LikedReview;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE, uses = {ReviewMapper.class})
public interface LikedReviewMapper {
    @Mapping(target = "account.id", source = "accountId")
    @Mapping(target = "review.id", source = "reviewId")
    @Mapping(target = "emotion", source = "emotion")
    @BeanMapping(ignoreByDefault = true)
    LikedReview fromCreateLikedReviewToEntity(CreateLikedReviewForm createLikedReviewForm);

    @Mapping(target = "emotion", source = "emotion")
    @BeanMapping(ignoreByDefault = true)
    void updateLikedReviewFromEntity(UpdateLikedReviewForm updateLikedReviewForm, @MappingTarget LikedReview likedReview);

    @Mapping(target = "accountId", source = "account.id")
    @Mapping(target = "review", source = "review", qualifiedByName = "fromReviewEntityToDto")
    @Mapping(target = "emotion", source = "emotion")
    @BeanMapping(ignoreByDefault = true)
    @Named("fromEntityToLikeReviewDto")
    LikeReviewDto fromEntityToLikeReviewDto(LikedReview likedReview);

    @IterableMapping(qualifiedByName = "fromEntityToLikeReviewDto", elementTargetType = LikeReviewDto.class)
    List<LikeReviewDto> fromEntityToLikeReviewDtoList(List<LikedReview> likedReviews);
}
