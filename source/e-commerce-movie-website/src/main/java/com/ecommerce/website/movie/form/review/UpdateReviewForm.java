package com.ecommerce.website.movie.form.review;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import javax.validation.constraints.NotNull;

@Data
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
public class UpdateReviewForm {
    @NotNull
    @ApiModelProperty(value = "id")
    Long id;
    @ApiModelProperty(value = "content")
    String content;
}
