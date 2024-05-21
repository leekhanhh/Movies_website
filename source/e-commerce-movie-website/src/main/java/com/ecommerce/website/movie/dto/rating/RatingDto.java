package com.ecommerce.website.movie.dto.rating;

import com.ecommerce.website.movie.dto.account.AccountDto;
import com.ecommerce.website.movie.dto.movie.MovieDto;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
public class RatingDto {
    AccountDto account;
    MovieDto movie;
    Double evaluation;
}
