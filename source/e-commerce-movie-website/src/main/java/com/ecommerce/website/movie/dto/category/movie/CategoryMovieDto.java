package com.ecommerce.website.movie.dto.category.movie;

import com.ecommerce.website.movie.dto.category.CategoryDto;
import com.ecommerce.website.movie.dto.movie.MovieDto;
import lombok.Data;

@Data
public class CategoryMovieDto {
    private Long id;
    private CategoryDto category;
    private MovieDto movie;
}
