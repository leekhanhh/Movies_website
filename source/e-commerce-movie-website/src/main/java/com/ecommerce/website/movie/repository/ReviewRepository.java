package com.ecommerce.website.movie.repository;

import com.ecommerce.website.movie.model.Review;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

public interface ReviewRepository extends JpaRepository<Review, Long>, JpaSpecificationExecutor<Review> {
    List<Review> findByMovieId(Long movieId);
    List<Review> findByAccountId(Long accountId);
    List<Review> findByAccountIdAndMovieId(Long accountId, Long movieId);
    void deleteByAccountIdAndMovieId(Long accountId, Long movieId);
}
