package com.ecommerce.website.movie.form.movie;


import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
public class UpdateMovieForm {
    @NotEmpty(message = "Movie title can not be null!")
    private String title;
    private String overview;
    private String imagePath;
    @NotNull
    private Double price;
    private Long[] categoryListIds;
}
