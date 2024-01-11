package com.ecommerce.website.movie.form.movie;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
public class CreateMovieForm {
    @NotEmpty(message = "Movie title can not be null!")
    private String title;
    @NotEmpty
    private String overview;
    @NotNull(message = "Image can not be empty")
    private String imagePath;
    @NotNull
    private Double price;
    @NotNull
    private Long[] categoryListIds;
}
