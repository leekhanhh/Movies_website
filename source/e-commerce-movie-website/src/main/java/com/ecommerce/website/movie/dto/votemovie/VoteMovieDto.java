package com.ecommerce.website.movie.dto.votemovie;

import com.ecommerce.website.movie.dto.BaseInfo;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
public class VoteMovieDto extends BaseInfo {
    Long movieId;
    Long accountId;
    Long userId;
}
