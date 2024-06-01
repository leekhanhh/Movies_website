package com.ecommerce.website.movie.dto.login;

import lombok.Data;

@Data
public class AuthenticationDto {
    private String token;
    private Long expiresIn;
    private String email;
    private Integer role;
}
