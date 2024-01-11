package com.ecommerce.website.movie.form.category;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
public class UpdateCategoryForm {
    @NotNull
    private Long id;
    @NotEmpty
    private String name;
    private String description;
    private Integer ordering;
}
