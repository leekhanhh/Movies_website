package com.ecommerce.website.movie.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

@Data
public class BaseInfo {
    @ApiModelProperty(name = "id")
    private Long id;
    @ApiModelProperty(name = "status")
    private Integer status;
    @ApiModelProperty(name = "modifiedDate")
    private Date modifiedDate;
    @ApiModelProperty(name = "createdDate")
    private Date createdDate;
}
