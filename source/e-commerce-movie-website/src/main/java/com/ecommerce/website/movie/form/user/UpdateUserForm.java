package com.ecommerce.website.movie.form.user;

import com.ecommerce.website.movie.constant.Constant;
import com.ecommerce.website.movie.validation.Gender;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Date;

@Data
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
public class UpdateUserForm {
    @NotNull
    @ApiModelProperty(value = "User id", required = true)
    private Long id;
    @NotEmpty
    @ApiModelProperty(value = "Username", required = true, example = "Khanh")
    private String fullname;
    @NotEmpty
    @ApiModelProperty(value = "Email", required = true, example = "abc@gmail.com")
    @Email(message = "Email should be valid")
    private String email;
    @ApiModelProperty(value = "Phone", example = "0123456789")
    private String phone;
    @ApiModelProperty(value = "Avatar", example = "http://avatar.jpg")
    private String avatarPath;
    @ApiModelProperty(value = "User date of birth", required = true)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Constant.DATE_TIME_FORMAT, timezone = "UTC")
    Date dateOfBirth;
    @ApiModelProperty(value = "User gender")
    @Gender(message = "Gender must be 1 (Male) or 0 (Female)", allowNull = true)
    Integer gender;
}
