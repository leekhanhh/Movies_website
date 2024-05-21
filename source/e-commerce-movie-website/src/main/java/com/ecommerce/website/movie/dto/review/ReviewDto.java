package com.ecommerce.website.movie.dto.review;

import com.ecommerce.website.movie.dto.BaseInfo;
import com.ecommerce.website.movie.dto.account.AccountDto;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
public class ReviewDto extends BaseInfo {
    Long movieId;
    AccountDto account;
    String content;
}
