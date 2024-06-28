package com.ecommerce.website.movie.dto.likereview;

import com.ecommerce.website.movie.dto.review.ReviewDto;
import lombok.Data;

@Data
public class LikeReviewDto{
    private Long id;
    private Long accountId;
    private ReviewDto review;
    private Integer emotion;
}
