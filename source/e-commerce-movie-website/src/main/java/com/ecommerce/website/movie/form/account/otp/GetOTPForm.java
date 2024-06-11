package com.ecommerce.website.movie.form.account.otp;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;

@Data
public class GetOTPForm {
    @ApiModelProperty(value = "Email", example = "abc123@gmail.com")
    @Email
    @NotEmpty
    private String Email;
}
