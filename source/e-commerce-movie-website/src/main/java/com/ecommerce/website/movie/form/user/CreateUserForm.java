package com.ecommerce.website.movie.form.user;

import com.ecommerce.website.movie.constant.Constant;
import com.ecommerce.website.movie.form.account.CreateAccountForm;
import com.ecommerce.website.movie.validation.Gender;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import javax.validation.constraints.NotNull;
import java.util.Date;

@Data
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
public class CreateUserForm {
    @NotNull
    @ApiModelProperty(value = "The account which its id is mapped with this user", required = true)
    private Long accountId;
    @ApiModelProperty(value = "User date of birth", required = true)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
    String dateOfBirth;
    @ApiModelProperty(value = "User gender")
    @Gender(message = "Gender must be 1 (Male) or 0 (Female)", allowNull = true)
    Integer gender;
}
