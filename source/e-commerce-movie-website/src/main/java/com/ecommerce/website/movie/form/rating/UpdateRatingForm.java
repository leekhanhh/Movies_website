package com.ecommerce.website.movie.form.rating;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class UpdateRatingForm {
    @ApiModelProperty(value = "Rating id", notes = "Rating id can not be null!", example = "1")
    private Long id;
    @ApiModelProperty(value = "movie id", notes = "movie id can not be null!", example = "1")
    private Long movieId;
    @ApiModelProperty(value = "account id", notes = "account id can not be null!", example = "1")
    private Long accountId;
    @ApiModelProperty(value = "evaluation point", notes = "evaluation point can not be null!", example = "5")
    private Double evaluation;
}
