package com.ecommerce.website.movie.dto.rating;

import com.ecommerce.website.movie.model.Account;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
public class RatingDto {
    Long accountId;
    Long movieId;
    Double evaluation;
}
