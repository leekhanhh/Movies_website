package com.ecommerce.website.movie.form.account;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Data
public class UpdateAccountForm {
    @NotNull
    @ApiModelProperty(value = "Username", required = true, example = "Khanh")
    private String username;
    @NotNull
    @ApiModelProperty(value = "Password", required = true, example = "123456")
    @Size(min = 10, message = "Password must be at least 8 characters long")
    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$",
            message = "Password must contain at least one uppercase letter, one lowercase letter, one number, and one special character")
    private String password;
    @NotEmpty
    @ApiModelProperty(value = "Email", required = true, example = "abc@gmail.com")
    private String email;
    @ApiModelProperty(value = "Phone", example = "0123456789")
    private String phone;
    @ApiModelProperty(value = "Avatar", example = "http://avatar.jpg")
    private String avatarPath;
    @ApiModelProperty(value = "Payment detail id", example = "1")
    private Long paymentDetailId;
}
