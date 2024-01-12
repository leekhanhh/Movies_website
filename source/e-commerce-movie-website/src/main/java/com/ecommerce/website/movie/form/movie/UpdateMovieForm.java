package com.ecommerce.website.movie.form.movie;


import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
public class UpdateMovieForm {
    @NotNull(message = "Movie id can not be null!")
    private Long id;
    @NotEmpty(message = "Movie title can not be null!")
    private String title;
    private String overview;
    private String imagePath;
    @NotNull
    private Double price;
    private Integer status;
    private Long[] categoryListIds;
}
