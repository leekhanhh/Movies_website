package com.ecommerce.website.movie.dto.user;

import com.ecommerce.website.movie.dto.BaseInfo;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.util.Date;

@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
@Data
public class UserDto extends BaseInfo {
    Boolean isVerify;
    String email;
    String username;
    String phone;
    String avatarPath;
    Integer gender;
    Date dateOfBirth;
}
