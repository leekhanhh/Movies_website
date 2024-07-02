package com.ecommerce.website.movie.form.likedreview;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class CreateLikedReviewForm {
    @ApiModelProperty(value = "Account ID", example = "1")
    private Long accountId;
    @ApiModelProperty(value = "Review ID", example = "1")
    private Long reviewId;
    @ApiModelProperty(value = "Emotion", example = "1")
    private Integer emotion;
}
