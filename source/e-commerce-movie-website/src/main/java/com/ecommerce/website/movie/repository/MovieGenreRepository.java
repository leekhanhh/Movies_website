package com.ecommerce.website.movie.repository;

import com.ecommerce.website.movie.model.Category;
import com.ecommerce.website.movie.model.Movie;
import com.ecommerce.website.movie.model.MovieGenre;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface MovieGenreRepository extends JpaRepository<MovieGenre, Long>, JpaSpecificationExecutor<MovieGenre> {
    void deleteAllByMovie(Movie movie);
}
