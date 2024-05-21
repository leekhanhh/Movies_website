package com.ecommerce.website.movie.dto.votemovie;

import com.ecommerce.website.movie.dto.BaseInfo;
import com.ecommerce.website.movie.dto.account.AccountDto;
import com.ecommerce.website.movie.dto.movie.MovieDto;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
public class VoteMovieDto extends BaseInfo {
    MovieDto movie;
    AccountDto account;
}
