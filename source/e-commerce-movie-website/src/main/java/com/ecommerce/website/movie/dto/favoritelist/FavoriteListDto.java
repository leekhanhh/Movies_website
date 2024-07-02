package com.ecommerce.website.movie.dto.favoritelist;

import com.ecommerce.website.movie.dto.BaseInfo;
import com.ecommerce.website.movie.dto.account.AccountDto;
import com.ecommerce.website.movie.dto.movie.MovieDto;
import com.ecommerce.website.movie.model.Account;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Data
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
public class FavoriteListDto extends BaseInfo {
    AccountDto account;
    MovieDto movie;
//    List<Long> movieIds;
}
