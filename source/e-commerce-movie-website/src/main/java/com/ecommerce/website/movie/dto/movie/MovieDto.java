package com.ecommerce.website.movie.dto.movie;

import com.ecommerce.website.movie.dto.BaseInfo;
import com.ecommerce.website.movie.dto.category.CategoryDto;
import lombok.Data;

@Data
public class MovieDto extends BaseInfo {
    private String title;
    private String overview;
    private Double price;
    private String imagePath;
    private CategoryDto category;
}
