package com.ecommerce.website.movie.dto.user;

import com.ecommerce.website.movie.dto.BaseInfo;
import com.ecommerce.website.movie.dto.account.AccountDto;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.util.Date;

@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
@Data
public class UserDto extends BaseInfo {
    Integer gender;
    Date dateOfBirth;
    AccountDto account;
}
