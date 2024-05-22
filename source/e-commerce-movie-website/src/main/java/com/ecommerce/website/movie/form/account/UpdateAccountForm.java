package com.ecommerce.website.movie.form.account;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Id;
import javax.validation.constraints.*;

@Data
public class UpdateAccountForm {
    @Id
    @NotNull
    @ApiModelProperty(value = "Id", required = true, example = "1")
    private Long id;
    @NotNull
    @ApiModelProperty(value = "Username", required = true, example = "Khanh")
    private String username;
    @NotEmpty
    @ApiModelProperty(value = "Email", required = true, example = "abc@gmail.com")
    @Email(message = "Email should be valid")
    private String email;
    @ApiModelProperty(value = "Phone", example = "0123456789")
    private String phone;
    @ApiModelProperty(value = "Avatar", example = "http://avatar.jpg")
    private String avatarPath;
    @ApiModelProperty(value = "Payment detail id", example = "1")
    private Long paymentDetailId;
}
