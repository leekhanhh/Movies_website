package com.ecommerce.website.movie.form.category;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
public class UpdateCategoryForm {
    @NotNull
    @ApiModelProperty(value = "category id", notes = "category id can not be null!", example = "1")
    private Long id;
    @NotEmpty
    @ApiModelProperty(value = "name", notes = "name can not be null!", example = "Action")
    private String name;
    @ApiModelProperty(value = "description")
    private String description;
    @ApiModelProperty(value = "ordering",notes = "ordering type can be descending or ascending!")
    private Integer ordering;
}
