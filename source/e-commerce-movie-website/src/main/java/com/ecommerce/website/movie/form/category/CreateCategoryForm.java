package com.ecommerce.website.movie.form.category;

import com.ecommerce.website.movie.validation.CategoryKind;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
public class CreateCategoryForm {
    @NotEmpty
    private String name;
    @NotEmpty
    private String description;
    @CategoryKind
    private Integer kind;
    private Long parentId;
}
