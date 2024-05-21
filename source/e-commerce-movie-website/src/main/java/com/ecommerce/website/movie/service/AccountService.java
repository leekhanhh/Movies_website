package com.ecommerce.website.movie.service;

import com.ecommerce.website.movie.form.account.CreateAccountForm;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class AccountService {
    private final BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
    public void createAccount(CreateAccountForm createAccountForm) {
        String password = createAccountForm.getPassword();
        String encodedPassword = bCryptPasswordEncoder.encode(password);
        createAccountForm.setPassword(encodedPassword);
    }
}
