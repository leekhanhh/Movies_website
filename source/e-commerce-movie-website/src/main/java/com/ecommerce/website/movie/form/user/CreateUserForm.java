package com.ecommerce.website.movie.form.user;

import com.ecommerce.website.movie.constant.Constant;
import com.ecommerce.website.movie.dto.account.AccountDto;
import com.ecommerce.website.movie.validation.Gender;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.util.Date;

@Data
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
public class CreateUserForm {
    @NotEmpty
    @ApiModelProperty(value = "Username", required = true, example = "Khanh")
    private String fullname;
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
    @NotEmpty
    @ApiModelProperty(value = "User password", required = true)
    String password;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Constant.DATE_TIME_FORMAT, timezone = "UTC")
    @ApiModelProperty(value = "User date of birth", required = true)
    Date dateOfBirth;
    @ApiModelProperty(value = "User gender")
    @Gender(message = "Gender must be 1 (Male) or 0 (Female)", allowNull = true)
    Integer gender;
}
