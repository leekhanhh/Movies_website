package com.ecommerce.website.movie.form.review;

import io.swagger.annotations.ApiModelProperty;
import lombok.experimental.FieldDefaults;

import javax.validation.constraints.NotNull;

@lombok.Data
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
public class CreateReviewFrom {
    @NotNull
    @ApiModelProperty(value = "movie id", notes = "movie id can not be null!", example = "1")
    Long movieId;
    @NotNull
    @ApiModelProperty(value = "account id", notes = "account id can not be null!", example = "1")
    Long accountId;
    @ApiModelProperty(value = "content")
    String content;
}
