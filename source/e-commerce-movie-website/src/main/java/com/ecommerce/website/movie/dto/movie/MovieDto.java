package com.ecommerce.website.movie.dto.movie;

import com.ecommerce.website.movie.dto.BaseInfo;
import com.ecommerce.website.movie.dto.category.movie.CategoryMovieDto;
import lombok.Data;

import java.util.List;

@Data
public class MovieDto extends BaseInfo {
    private String title;
    private String overview;
    private Integer voteCount;
    private Double price;
    private String imagePath;
    private List<CategoryMovieDto> categoryMovieList;
}
