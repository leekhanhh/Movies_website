package com.ecommerce.website.movie.dto.likereview;

import lombok.Data;

@Data
public class LikeReviewDto{
    private Long id;
    private Long accountId;
    private Long reviewId;
    private Integer emotion;
}
