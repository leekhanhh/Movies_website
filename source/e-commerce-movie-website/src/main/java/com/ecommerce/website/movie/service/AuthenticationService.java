package com.ecommerce.website.movie.service;


import com.ecommerce.website.movie.constant.Constant;
import com.ecommerce.website.movie.dto.login.LoginDto;
import com.ecommerce.website.movie.dto.registeruser.RegisterUserDto;
import com.ecommerce.website.movie.form.account.CreateAccountForm;
import com.ecommerce.website.movie.form.user.CreateUserForm;
import com.ecommerce.website.movie.model.Account;
import com.ecommerce.website.movie.repository.AccountRepository;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationService {
    private final AccountRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    private final AuthenticationManager authenticationManager;

    public AuthenticationService(
            AccountRepository userRepository,
            AuthenticationManager authenticationManager,
            PasswordEncoder passwordEncoder
    ) {
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }


    public Account authenticate(LoginDto input) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        input.getEmail(),
                        input.getPassword()
                )
        );

        return userRepository.findByEmail(input.getEmail())
                .orElseThrow();
    }
}
