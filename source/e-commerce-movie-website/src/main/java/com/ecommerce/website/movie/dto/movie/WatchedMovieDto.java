package com.ecommerce.website.movie.dto.movie;

import com.ecommerce.website.movie.dto.BaseInfo;
import lombok.Data;

@Data
public class WatchedMovieDto extends BaseInfo {
    Long accountId;
    Long movieId;
}
