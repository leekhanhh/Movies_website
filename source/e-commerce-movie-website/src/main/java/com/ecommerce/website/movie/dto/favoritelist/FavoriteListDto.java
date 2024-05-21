package com.ecommerce.website.movie.dto.favoritelist;

import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Data
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
public class FavoriteListDto {
    Long id;
    Long accountId;
    Long movieId;
//    List<Long> movieIds;
}
