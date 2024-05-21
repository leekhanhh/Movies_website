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
    @NotEmpty
    @ApiModelProperty(value = "The account which its id is mapped with this user", required = true)
    Long accountId;
    @ApiModelProperty(value = "User date of birth", required = true)
    @Past(message = "Date of birth must be in the past")
    String dateOfBirth;
    @ApiModelProperty(value = "User gender")
    @Gender(message = "Gender must be 1 (Male) or 2 (Female)", allowNull = true)
    Integer gender;
}
