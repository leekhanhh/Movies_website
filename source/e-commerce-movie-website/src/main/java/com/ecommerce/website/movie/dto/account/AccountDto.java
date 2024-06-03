package com.ecommerce.website.movie.dto.account;

import com.ecommerce.website.movie.dto.BaseInfo;
import com.ecommerce.website.movie.model.PaymentDetail;
import lombok.Data;

import java.util.Date;
@Data
public class AccountDto extends BaseInfo {
    String email;
    String fullname;
    String phone;
    String avatarPath;
    Integer role;
    Date lastLogin;
//    PaymentDetailDto paymentDetail;
}
