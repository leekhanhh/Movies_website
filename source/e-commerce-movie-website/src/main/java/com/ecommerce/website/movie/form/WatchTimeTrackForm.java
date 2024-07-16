package com.ecommerce.website.movie.form;

import lombok.Data;

@Data
public class WatchTimeTrackForm {
    private Long accountId;
    private Long movieId;
    private Long watchedTime;
    private Long duration;
}
