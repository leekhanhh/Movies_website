package com.ecommerce.website.movie.form.user;

import com.ecommerce.website.movie.constant.Constant;
import com.ecommerce.website.movie.validation.Gender;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import java.util.Date;

@Data
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
public class UpdateUserForm {
    @ApiModelProperty(value = "User date of birth", required = true)
    @JsonFormat(pattern = Constant.DATE_FORMAT)
    String dateOfBirth;
    @ApiModelProperty(value = "User gender")
    @Gender(message = "Gender must be 1 (Male) or 0 (Female)", allowNull = true)
    Integer gender;
}
