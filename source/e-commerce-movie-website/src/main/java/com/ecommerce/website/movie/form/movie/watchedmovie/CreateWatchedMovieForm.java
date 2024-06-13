package com.ecommerce.website.movie.form.movie.watchedmovie;

import lombok.Data;

@Data
public class CreateWatchedMovieForm {
    private Long accountId;
    private Long movieId;
}
