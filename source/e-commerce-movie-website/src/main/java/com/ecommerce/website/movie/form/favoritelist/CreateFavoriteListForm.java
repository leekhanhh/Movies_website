package com.ecommerce.website.movie.form.favoritelist;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class CreateFavoriteListForm {
    @ApiModelProperty(value = "movie id", notes = "movie id can not be null!", example = "1")
    @NotNull
    private Long movieId;
    @ApiModelProperty(value = "account id", notes = "account id can not be null!", example = "1")
    @NotNull
    private Long accountId;
}
