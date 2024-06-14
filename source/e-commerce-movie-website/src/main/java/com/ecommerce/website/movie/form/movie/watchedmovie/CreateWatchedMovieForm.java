package com.ecommerce.website.movie.form.movie.watchedmovie;

import lombok.Data;

import java.util.Date;

@Data
public class CreateWatchedMovieForm {
    private Long accountId;
    private Long movieId;
    private Date watchedDate;
}
