package com.ecommerce.website.movie.dto.user;

import com.ecommerce.website.movie.dto.BaseInfo;
import com.ecommerce.website.movie.dto.account.AccountDto;
import lombok.Data;
import lombok.experimental.FieldDefaults;


@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
@Data
public class UserDto extends BaseInfo {
    Integer gender;
    String dateOfBirth;
    AccountDto account;
}
