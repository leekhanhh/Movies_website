package com.ecommerce.website.movie.form.rating;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
public class CreateRatingForm {
    @NotNull
    @ApiModelProperty(value = "movie id", notes = "movie id can not be null!", example = "1")
    private Long movieId;
    @NotNull
    @ApiModelProperty(value = "account id", notes = "account id can not be null!", example = "1")
    private Long accountId;
    @NotNull
    @ApiModelProperty(value = "evaluation")
    private Double evaluation;
}
