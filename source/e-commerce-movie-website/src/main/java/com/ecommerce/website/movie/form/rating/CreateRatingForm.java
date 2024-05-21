package com.ecommerce.website.movie.form.rating;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;

@Data
public class CreateRatingForm {
    @NotEmpty
    @ApiModelProperty(value = "movie id", notes = "movie id can not be null!", example = "1")
    private Long movieId;
    @NotEmpty
    @ApiModelProperty(value = "account id", notes = "account id can not be null!", example = "1")
    private Long accountId;
    @NotEmpty
    @ApiModelProperty(value = "evaluation")
    private Double evaluation;
}
