package com.ecommerce.website.movie.dto;

import lombok.Data;

@Data
public class WatchTimeDto extends BaseInfo{
    private Long accountId;
    private Long movieId;
    private Long watchTime;
    private Long duration;
}
