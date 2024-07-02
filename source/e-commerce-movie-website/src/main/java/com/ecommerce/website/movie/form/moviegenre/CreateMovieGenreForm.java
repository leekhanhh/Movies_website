package com.ecommerce.website.movie.form.moviegenre;

import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.NotNull;


public class CreateMovieGenreForm {
    @NotNull
    @ApiModelProperty(value = "id", notes = "id can not be null!", example = "1")
    private Long id;
    @NotNull
    @ApiModelProperty(value = "category id", notes = "category id can not be null!", example = "1")
    private Long categoryId;
    @NotNull
    @ApiModelProperty(value = "movie id", notes = "movie id can not be null!", example = "1")
    private Long movieId;
}
