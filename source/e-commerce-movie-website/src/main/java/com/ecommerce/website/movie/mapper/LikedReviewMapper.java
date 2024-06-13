package com.ecommerce.website.movie.mapper;

import com.ecommerce.website.movie.form.likedreview.CreateLikedReviewForm;
import com.ecommerce.website.movie.form.likedreview.UpdateLikedReviewForm;
import com.ecommerce.website.movie.model.LikedReview;
import org.mapstruct.*;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface LikedReviewMapper {
    @Mapping(target = "account.id", source = "accountId")
    @Mapping(target = "review.id", source = "reviewId")
    @BeanMapping(ignoreByDefault = true)
    LikedReview fromCreateLikedReviewToEntity(CreateLikedReviewForm createLikedReviewForm);

    @Mapping(target = "emotion", source = "emotion")
    @BeanMapping(ignoreByDefault = true)
    void updateLikedReviewFromEntity(UpdateLikedReviewForm updateLikedReviewForm, @MappingTarget LikedReview likedReview);
}
