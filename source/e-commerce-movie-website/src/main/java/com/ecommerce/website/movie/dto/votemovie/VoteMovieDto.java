package com.ecommerce.website.movie.dto.votemovie;

import lombok.Data;

@Data
public class VoteMovieDto {
    Long movieId;
    Long accountId;
    Long userId;
}
