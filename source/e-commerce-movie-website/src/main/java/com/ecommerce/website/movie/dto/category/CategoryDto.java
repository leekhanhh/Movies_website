package com.ecommerce.website.movie.dto.category;

import com.ecommerce.website.movie.dto.BaseInfo;
import lombok.Data;

@Data
public class CategoryDto extends BaseInfo {
    private String name;
    private String description;
    private Integer ordering;
    private Integer kind;
    private Long parentId;
}
