package com.ecommerce.website.movie.form.account.otp;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
public class OTPForm {
    @NotNull
    @ApiModelProperty(value = "OTP", example = "123456")
    private String otp;
    @Email
    @NotEmpty
    @ApiModelProperty(value = "Email", example = "abc123@gmail.com")
    private String email;
}
