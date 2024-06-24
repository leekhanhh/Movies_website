package com.ecommerce.website.movie.repository;

import com.ecommerce.website.movie.model.LikedReview;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface LikedReviewRepository extends JpaRepository<LikedReview, Long>, JpaSpecificationExecutor<LikedReview> {
    LikedReview findByAccountIdAndReviewId(Long accountId, Long reviewId);
}
