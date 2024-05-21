package com.ecommerce.website.movie.form.review;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
public class UpdateReviewForm {
    @ApiModelProperty(value = "content")
    String content;
}
