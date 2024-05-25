package com.ecommerce.website.movie.form.movie;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
public class CreateMovieForm {
    @ApiModelProperty(value = "movie title", notes = "movie title can not be null!", example = "The Shawshank Redemption")
    @NotEmpty(message = "Movie title can not be null!")
    private String title;
    @NotEmpty
    @ApiModelProperty(value = "movie overview", notes = "movie overview can not be null!", example="Two imprisoned")
    private String overview;
    @NotNull(message = "Image can not be empty")
    @ApiModelProperty(value = "image path", notes = "image path can not be null!", example = "https://www.google.com")
    private String imagePath;
    @NotNull
    @ApiModelProperty(value = "price", notes = "price can not be null!", example = "10.0")
    private Double price;
    @NotNull
    @ApiModelProperty(value = "category id", notes = "category id can not be null!", example = "1")
    private Long categoryId;
    @NotNull
    @ApiModelProperty(value = "genre ids", notes = "genre ids can not be null!", example = "[1,2]")
    private Long[] genreIds;
}
