package com.ecommerce.website.movie.controller;

import com.ecommerce.website.movie.model.Account;
import com.ecommerce.website.movie.repository.AccountRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

@Slf4j
public class BaseController {
    @Autowired
    AccountRepository accountRepository;

    public Long getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (!(authentication instanceof AnonymousAuthenticationToken)) {
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            Account account = accountRepository.findByEmail(userDetails.getUsername()).orElse(null);
            if (account != null) {
                return account.getId();
            } else {
                log.error("User not found");
                return null;
            }
        }
        return null;
    }
}
