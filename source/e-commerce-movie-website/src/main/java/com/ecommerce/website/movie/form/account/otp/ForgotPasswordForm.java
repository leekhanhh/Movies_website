package com.ecommerce.website.movie.form.account.otp;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
@Data
public class ForgotPasswordForm {
    @NotEmpty
    @ApiModelProperty(value = "Email", example = "abc123@gmail.com")
    @Email
    private String email;
    @NotEmpty
    @ApiModelProperty(value = "Password", example = "123456")
    @Size(min = 6, message = "Password must be at least 6 characters long")
    private String password;
}
