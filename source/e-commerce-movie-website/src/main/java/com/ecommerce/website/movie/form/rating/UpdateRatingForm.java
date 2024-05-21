package com.ecommerce.website.movie.form.rating;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class UpdateRatingForm {
    @ApiModelProperty(value = "evaluation point", notes = "evaluation point can not be null!", example = "5")
    private Double evaluation;
}
