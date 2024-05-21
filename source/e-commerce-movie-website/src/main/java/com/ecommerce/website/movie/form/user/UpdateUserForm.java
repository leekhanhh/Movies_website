package com.ecommerce.website.movie.form.user;

import com.ecommerce.website.movie.validation.Gender;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import javax.validation.constraints.Past;
import javax.validation.constraints.Size;

@Data
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
public class UpdateUserForm {
    @ApiModelProperty(value = "Username")
    @Size(min = 6, max = 50, message = "User name must be at least 6 characters and at most 50 characters")
    String username;
    @ApiModelProperty(value = "Old password")
    @Size(min = 6, max = 50, message = "Old password must be at least 6 characters and at most 50 characters")
    String oldPassword;
    @ApiModelProperty(value = "New password")
    @Size(min = 6, max = 50, message = "New password must be at least 6 characters and at most 50 characters")
    String newPassword;
    @ApiModelProperty(value = "User phone number")
    String phone;
    @ApiModelProperty(value = "User gender")
    @Gender(message ="Gender must be 1 (Male) or 2 (Female)", allowNull = true)
    String gender;
    @ApiModelProperty(value = "User avatar")
    String avatarPath;
    @ApiModelProperty(value = "User date of birth")
    @Past(message = "Date of birth must be in the past")
    String dateOfBirth;
}
