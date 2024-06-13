package com.ecommerce.website.movie.repository;

import com.ecommerce.website.movie.model.WatchedMovies;
import org.springframework.data.mongodb.repository.MongoRepository;


public interface WatchedMovieRepository extends MongoRepository<WatchedMovies, Long> {
}