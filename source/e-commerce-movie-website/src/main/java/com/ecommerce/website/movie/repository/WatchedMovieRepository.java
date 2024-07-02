package com.ecommerce.website.movie.repository;

import com.ecommerce.website.movie.model.WatchedMovies;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface WatchedMovieRepository extends MongoRepository<WatchedMovies, Long> {
    WatchedMovies findByAccountIdAndMovieId(Long accountId, Long movieId);
    void deleteAllByMovieId(Long movieId);
    void deleteAllByAccountId(Long accountId);
}