package com.ecommerce.website.movie.repository;

import com.ecommerce.website.movie.model.WatchedMovies;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface WatchedMovieRepository extends MongoRepository<WatchedMovies, Long> {
    WatchedMovies findByAccountIdAndMovieId(Long accountId, Long movieId);
    void deleteAllByAccountId(Long accountId);
    List<WatchedMovies> findAllByAccountId(Long accountId);
}