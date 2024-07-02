package com.ecommerce.website.movie.form.moviegenre;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class UpdateMovieGenreForm {
    @NotNull
    @ApiModelProperty(value = "movie id", notes = "movie id can not be null!", example = "1")
    private Long movieId;
    @NotNull
    @ApiModelProperty(value = "category id", notes = "category id can not be null!", example = "1")
    private Long categoryId;
}
