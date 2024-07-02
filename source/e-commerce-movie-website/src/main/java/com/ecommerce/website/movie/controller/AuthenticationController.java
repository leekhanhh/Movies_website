package com.ecommerce.website.movie.controller;

import com.ecommerce.website.movie.dto.ApiResponseDto;
import com.ecommerce.website.movie.dto.ErrorCode;
import com.ecommerce.website.movie.dto.login.AuthenticationDto;
import com.ecommerce.website.movie.dto.login.LoginDto;
import com.ecommerce.website.movie.mapper.AccountMapper;
import com.ecommerce.website.movie.mapper.UserMapper;
import com.ecommerce.website.movie.model.Account;
import com.ecommerce.website.movie.repository.AccountRepository;
import com.ecommerce.website.movie.repository.UserRepository;
import com.ecommerce.website.movie.service.JWTService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

@RequestMapping("/auth")
@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
@Slf4j
public class AuthenticationController {
    @Autowired
    JWTService jwtService;
    @Autowired
    AuthenticationManager authenticationManager;
    @Autowired
    AccountRepository accountRepository;
    @Autowired
    AccountMapper accountMapper;
    @Autowired
    UserMapper userMapper;
    @Autowired
    UserRepository userRepository;

    @PostMapping(value = "/login", produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiResponseDto<AuthenticationDto> login(@RequestBody LoginDto loginDto) {
        ApiResponseDto<AuthenticationDto> apiResponseDto = new ApiResponseDto<>();
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginDto.getEmail(), loginDto.getPassword())
            );
            Account user = (Account) authentication.getPrincipal();
            String token = jwtService.buildToken(user);
            AuthenticationDto authenticationDto = new AuthenticationDto();
            authenticationDto.setToken(token);
            authenticationDto.setEmail(user.getEmail());
            authenticationDto.setRole(user.getRole());

            Account account = accountRepository.findByEmail(user.getUsername()).orElseThrow();
            account.setLastLogin(new Date());
            accountRepository.save(account);
            apiResponseDto.setData(authenticationDto);
            apiResponseDto.setMessage("Login successfully!");
        } catch (BadCredentialsException ex) {
            apiResponseDto.setCode(ErrorCode.GENERAL_ERROR_UNAUTHORIZED);
            apiResponseDto.setMessage("Accessed Denied");
            apiResponseDto.setResult(false);
        }
        return apiResponseDto;
    }
}
