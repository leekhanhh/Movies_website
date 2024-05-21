package com.ecommerce.website.movie.form.user;

import com.ecommerce.website.movie.validation.Gender;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Past;
import javax.validation.constraints.Size;

@Data
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
public class CreateUserForm {
    @NotEmpty(message = "User name can not be empty")
    @Size(min = 6, max = 50, message = "User name must be at least 6 characters and at most 50 characters")
    @ApiModelProperty(value = "Username", required = true)
    String username;
    @NotEmpty(message = "Email can not be empty")
    @Email(message = "Email is not valid")
    @ApiModelProperty(value = "Email", required = true)
    String email;
    @NotEmpty(message = "Password can not be empty")
    @Size(min = 6, max = 50, message = "Password must be at least 6 characters and at most 50 characters")
    @ApiModelProperty(value = "Password", required = true)
    String password;
    @ApiModelProperty(value = "User phone number")
    String phone;
    @ApiModelProperty(value = "User avatar")
    String avatarPath;
    @ApiModelProperty(value = "User login check")
    @NotEmpty
    Boolean isVerify;
    @ApiModelProperty(value = "User date of birth", required = true)
    @Past(message = "Date of birth must be in the past")
    String dateOfBirth;
    @ApiModelProperty(value = "User gender")
    @Gender(message = "Gender must be 1 (Male) or 2 (Female)", allowNull = true)
    Integer gender;
}
