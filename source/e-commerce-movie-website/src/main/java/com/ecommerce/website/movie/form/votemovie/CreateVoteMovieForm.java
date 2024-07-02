package com.ecommerce.website.movie.form.votemovie;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import javax.validation.constraints.NotNull;

@Data
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
public class CreateVoteMovieForm {
    @ApiModelProperty(value = "movie id", notes = "movie id can not be null!", example = "1")
    @NotNull
    Long movieId;
    @ApiModelProperty(value = "account id", notes = "account id can not be null!", example = "1")
    @NotNull
    Long accountId;
}
