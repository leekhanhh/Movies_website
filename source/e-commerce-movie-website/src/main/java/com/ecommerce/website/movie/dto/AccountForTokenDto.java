package com.ecommerce.website.movie.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class AccountForTokenDto {
    @ApiModelProperty(name = "id")
    private Long id;
    @ApiModelProperty(name = "kind")
    private int kind;
    @ApiModelProperty(name = "email")
    private String email;
    @ApiModelProperty(name = "fullName")
    private String fullName;
}
