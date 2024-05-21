package com.ecommerce.website.movie.dto.review;

import com.ecommerce.website.movie.model.Account;
import com.ecommerce.website.movie.model.User;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
public class ReviewDto {
    Long movieId;
    Account accountId;
    String content;
}
