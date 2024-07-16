package com.ecommerce.website.movie.form;

import lombok.Data;

@Data
public class UpdateWatchTimeTrackForm {
    private Long accountId;
    private Long movieId;
    private Long watchTime;
}
