package com.ecommerce.website.movie.dto.movie;

import com.ecommerce.website.movie.dto.BaseInfo;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
public class MovieDto extends BaseInfo {
    private String title;
    private String overview;
    private Double price;
    private String imagePath;
}
