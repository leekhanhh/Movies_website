package com.ecommerce.website.movie.controller;

import com.ecommerce.website.movie.constant.Constant;
import com.ecommerce.website.movie.dto.ApiResponseDto;
import com.ecommerce.website.movie.dto.ErrorCode;
import com.ecommerce.website.movie.dto.ResponseListDto;
import com.ecommerce.website.movie.dto.account.AccountDto;
import com.ecommerce.website.movie.form.account.CreateAccountForm;
import com.ecommerce.website.movie.form.account.UpdateAccountForm;
import com.ecommerce.website.movie.mapper.AccountMapper;
import com.ecommerce.website.movie.model.Account;
import com.ecommerce.website.movie.model.User;
import com.ecommerce.website.movie.model.criteria.AccountCriteria;
import com.ecommerce.website.movie.repository.AccountRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import javax.validation.Valid;

import org.springframework.data.domain.Pageable;

import java.util.List;

@RestController
@RequestMapping("/v1/account")
@CrossOrigin(origins = "*", allowedHeaders = "*")
@Slf4j
public class AccountController extends BaseController{
    @Autowired
    AccountMapper accountMapper;
    @Autowired
    AccountRepository accountRepository;
    @Autowired
    PasswordEncoder passwordEncoder;

    @PostMapping(value = "/create-admin", produces = MediaType.APPLICATION_JSON_VALUE)
    @Transactional
    public ApiResponseDto<Long> createAccount(@Valid @RequestBody CreateAccountForm createAccountForm, BindingResult bindingResult) {
        ApiResponseDto<Long> apiResponseDto = new ApiResponseDto<>();
        Account account = accountRepository.findFirstByEmail(createAccountForm.getEmail());
        if (account != null) {
            apiResponseDto.setResult(false);
            apiResponseDto.setCode(ErrorCode.ACCOUNT_EMAIL_DUPLICATED);
            apiResponseDto.setMessage("Duplicated Email Error");
            return apiResponseDto;
        }
        account = accountMapper.fromCreateAccountToDto(createAccountForm);
        account.setPassword(passwordEncoder.encode(createAccountForm.getPassword()));
        account.setRole(Constant.ROLE_ADMIN);
        accountRepository.save(account);
        apiResponseDto.setMessage("Account has been saved successfully!");
        apiResponseDto.setData(account.getId());
        return apiResponseDto;
    }

    @PutMapping(value = "/update-admin", produces = MediaType.APPLICATION_JSON_VALUE)
    @Transactional
    public ApiResponseDto<Long> updateAccount(@Valid @RequestBody UpdateAccountForm updateAccountForm, BindingResult bindingResult) {
        ApiResponseDto<Long> apiResponseDto = new ApiResponseDto<>();
        Account account = accountRepository.findById(updateAccountForm.getId()).orElse(null);
        if (account == null) {
            apiResponseDto.setResult(false);
            apiResponseDto.setCode(ErrorCode.ACCOUNT_EMAIL_NOT_FOUND);
            apiResponseDto.setMessage("Email not found");
            return apiResponseDto;
        }
        accountMapper.fromUpdateAccountEntityToDto(updateAccountForm, account);
        accountRepository.save(account);
        apiResponseDto.setMessage("Account has been updated successfully!");
        return apiResponseDto;
    }

    @DeleteMapping(value = "/delete-admin/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @Transactional
    public ApiResponseDto<Long> deleteAccount(@PathVariable Long id) {
        ApiResponseDto<Long> apiResponseDto = new ApiResponseDto<>();
        Account account = accountRepository.findById(id).orElse(null);
        if (account == null) {
            apiResponseDto.setResult(false);
            apiResponseDto.setCode(ErrorCode.ACCOUNT_NOT_FOUND);
            apiResponseDto.setMessage("Account not found");
            return apiResponseDto;
        }
        accountRepository.deleteById(id);
        apiResponseDto.setMessage("Account has been deleted successfully!");
        return apiResponseDto;
    }

    @GetMapping(value = "/list-admin", produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiResponseDto<ResponseListDto<AccountDto>> listAccount(AccountCriteria accountCriteria, Pageable pageable) {
        ApiResponseDto<ResponseListDto<AccountDto>> apiResponseDto = new ApiResponseDto<>();
        Page<Account> accountPage = accountRepository.findAll(accountCriteria.getSpecification(), pageable);
        ResponseListDto<AccountDto> responseListDto = new ResponseListDto(accountMapper.fromEntityToDtoListForServer(accountPage.getContent()), accountPage.getTotalElements(), accountPage.getTotalPages());
        apiResponseDto.setData(responseListDto);
        apiResponseDto.setMessage("Get account list successfully!");
        return apiResponseDto;
    }

    @GetMapping(value = "/get-admin/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiResponseDto<AccountDto> getAccount(@PathVariable Long id) {
        ApiResponseDto<AccountDto> apiResponseDto = new ApiResponseDto<>();
        Account account = accountRepository.findById(id).orElse(null);
        if (account != null) {
            apiResponseDto.setMessage("Account found!");
            apiResponseDto.setData(accountMapper.fromAccountEntityToDtoForServer(account));
        } else {
            apiResponseDto.setMessage("Account not found!");
            apiResponseDto.setCode(ErrorCode.ACCOUNT_NOT_FOUND);
        }
        return apiResponseDto;
    }

    @GetMapping(value = "/my-profile", produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiResponseDto<AccountDto> getMyProfile() {
        ApiResponseDto<AccountDto> apiResponseDto = new ApiResponseDto<>();
        Account account = accountRepository.findById(getCurrentUser()).orElse(null);
        if (account == null) {
            apiResponseDto.setResult(false);
            apiResponseDto.setCode(ErrorCode.USER_ACCOUNT_NOT_FOUND);
            apiResponseDto.setMessage("User not found");
            return apiResponseDto;
        }
        apiResponseDto.setData(accountMapper.fromEntityToDtoForClient(account));
        apiResponseDto.setMessage("Get account successfully!");
        return apiResponseDto;
    }
}
