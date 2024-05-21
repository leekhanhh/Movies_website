package com.ecommerce.website.movie.form.movie;


import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
public class UpdateMovieForm {
    @NotNull(message = "Movie id can not be null!")
    @ApiModelProperty(value = "movie id", notes = "movie id can not be null!", example = "1")
    private Long id;
    @NotEmpty(message = "Movie title can not be null!")
    @ApiModelProperty(value = "title", notes = "title can not be null!", example = "The Shawshank Redemption")
    private String title;
    @ApiModelProperty(value = "overview", notes = "overview can not be null!", example="Two imprisoned")
    private String overview;
    @ApiModelProperty(value = "image path", notes = "image path can not be null!", example = "https://www.google.com")
    private String imagePath;
    @NotNull
    @ApiModelProperty(value = "price", notes = "price can not be null!", example = "10.0")
    private Double price;
    @ApiModelProperty(value = "status", notes = "status can not be null!", example = "1")
    private Integer status;
    @ApiModelProperty(value = "category list ids", notes = "category list ids can not be null!", example = "[1,2]")
    private Long[] categoryListIds;
}
