package com.ecommerce.website.movie.dto;

import lombok.Data;

@Data
public class ApiResponseDto<T> {
    private Boolean result = true;
    private String code = null;
    private T data = null;
    private String message = null;

}
