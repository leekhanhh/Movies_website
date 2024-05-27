package com.ecommerce.website.movie.repository;

import com.ecommerce.website.movie.model.Rating;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface RatingRepository extends JpaRepository<Rating, Long>, JpaSpecificationExecutor<Rating> {
    @Query("SELECT r FROM Rating r WHERE r.movie.id = :movieId")
    List<Rating> findByMovieId(Long movieId);

    @Query("SELECT r FROM Rating r WHERE r.account.id = :accountId")
    List<Rating> findByAccountId(Long accountId);

    @Query("SELECT r FROM Rating r WHERE r.account.id = :accountId AND r.movie.id = :movieId")
    Rating findByAccountIdAndMovieId(Long accountId, Long movieId);

    @Query("SELECT AVG(r.evaluation) FROM Rating r WHERE r.movie.id = :movieId")
    Double getAverageRatingByMovieId(Long movieId);
}
