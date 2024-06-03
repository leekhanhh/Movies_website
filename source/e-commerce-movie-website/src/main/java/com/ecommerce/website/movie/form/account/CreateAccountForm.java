package com.ecommerce.website.movie.form.account;

import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.*;

@lombok.Data
public class CreateAccountForm {
    @NotNull
    @ApiModelProperty(value = "Username", required = true, example = "Khanh")
    private String fullname;
    @NotNull
    @ApiModelProperty(value = "Password", required = true, example = "123456")
    @Size(min = 10, message = "Password must be at least 8 characters long")
    private String password;
    @NotEmpty
    @ApiModelProperty(value = "Email", required = true, example = "abc@gmail.com")
    @Email(message = "Email should be valid")
    private String email;
    @ApiModelProperty(value = "Phone", example = "0123456789")
    private String phone;
    @ApiModelProperty(value = "role", example = "0")
    private Integer role;
    @ApiModelProperty(value = "Avatar", example = "http://avatar.jpg")
    private String avatarPath;
    @ApiModelProperty(value = "Payment detail id", example = "1")
    private Long paymentDetailId;
}
