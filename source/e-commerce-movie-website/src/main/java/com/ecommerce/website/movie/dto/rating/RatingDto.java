package com.ecommerce.website.movie.dto.rating;

import com.ecommerce.website.movie.model.Account;
import lombok.Data;

@Data
public class RatingDto {
    Long accountId;
    Long movieId;
    Double evaluation;
}
