package com.ecommerce.website.movie.dto.moviegenre;

import lombok.Data;

@Data
public class MovieGenreDto {
    Long movieId;
    Long categoryId;
    String categoryName;
}
