package com.ecommerce.website.movie.controller;

import com.ecommerce.website.movie.constant.Constant;
import com.ecommerce.website.movie.dto.ApiResponseDto;
import com.ecommerce.website.movie.dto.ErrorCode;
import com.ecommerce.website.movie.dto.ResponseListDto;
import com.ecommerce.website.movie.dto.account.AccountDto;
import com.ecommerce.website.movie.form.account.CreateAccountForm;
import com.ecommerce.website.movie.form.account.UpdateAccountForm;
import com.ecommerce.website.movie.form.account.otp.ForgotPasswordForm;
import com.ecommerce.website.movie.form.account.otp.GetOTPForm;
import com.ecommerce.website.movie.form.account.otp.OTPForm;
import com.ecommerce.website.movie.mapper.AccountMapper;
import com.ecommerce.website.movie.model.Account;
import com.ecommerce.website.movie.model.criteria.AccountCriteria;
import com.ecommerce.website.movie.repository.AccountRepository;
import com.ecommerce.website.movie.service.EmailService;
import com.ecommerce.website.movie.service.OTPService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.mail.MessagingException;
import javax.transaction.Transactional;
import javax.validation.Valid;

import org.springframework.data.domain.Pageable;
import springfox.documentation.annotations.ApiIgnore;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

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
    @Autowired
    EmailService emailService;
    @Autowired
    OTPService otpService;

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

    @GetMapping(value = "/search-by-email", produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiResponseDto<ResponseListDto<AccountDto>> searchByEmail(@RequestParam("email") String email, AccountCriteria accountCriteria, Pageable pageable) {
        ApiResponseDto<ResponseListDto<AccountDto>> apiResponseDto = new ApiResponseDto<>();

        accountCriteria.setEmail(email);
        Page<Account> accountsPage = accountRepository.findAll(accountCriteria.getSpecification(), pageable);

        if (!accountsPage.isEmpty()) {
            List<AccountDto> accountDtos = accountsPage.getContent().stream()
                    .map(accountMapper::fromAccountEntityToDtoForServer)
                    .collect(Collectors.toList());
            ResponseListDto responseListDto = new ResponseListDto(accountDtos, accountsPage.getTotalElements(), accountsPage.getTotalPages());
            apiResponseDto.setMessage("Accounts found!");
            apiResponseDto.setData(responseListDto);
        } else {
            apiResponseDto.setMessage("Accounts not found!");
            apiResponseDto.setCode(ErrorCode.ACCOUNT_NOT_FOUND);
        }
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

    @PostMapping(value="/send-otp", produces = MediaType.APPLICATION_JSON_VALUE)
    @Transactional
    @ApiIgnore
    public ApiResponseDto<Boolean> sendOtp(@RequestBody @Valid GetOTPForm getOTPForm) throws MessagingException {
        ApiResponseDto<Boolean> apiResponseDto = new ApiResponseDto<>();
        Account account = accountRepository.findFirstByEmail(getOTPForm.getEmail());
        if (account == null) {
            apiResponseDto.setResult(false);
            apiResponseDto.setCode(ErrorCode.ACCOUNT_EMAIL_NOT_FOUND);
            apiResponseDto.setMessage("Email not found");
            return apiResponseDto;
        }
        if (account.getResetPasswordExpired() != null && account.getResetPasswordExpired().getTime() + 300000 > System.currentTimeMillis()) {
            apiResponseDto.setResult(false);
            apiResponseDto.setCode(ErrorCode.ACCOUNT_OTP_NOT_EXPIRED);
            apiResponseDto.setMessage("OTP has not expired yet. Please check your email!");
            return apiResponseDto;
        }

        String otp = otpService.generate(6);
        account.setResetPassword(otp);
        account.setResetPasswordExpired(new Date(System.currentTimeMillis() + 300000));
        accountRepository.save(account);
        emailService.sendOtpEmail(getOTPForm.getEmail(), otp, 5);
        apiResponseDto.setMessage("OTP has been sent successfully!");
        return apiResponseDto;
    }

    @PutMapping(value="/check-OTP", produces = MediaType.APPLICATION_JSON_VALUE)
    @Transactional
    @ApiIgnore
    public ApiResponseDto<Boolean> checkOtp(@Valid @RequestBody OTPForm otpForm, BindingResult bindingResult) {
        ApiResponseDto<Boolean> apiResponseDto = new ApiResponseDto<>();
        Account account = accountRepository.findFirstByEmail(otpForm.getEmail());
        if (account == null) {
            apiResponseDto.setResult(false);
            apiResponseDto.setCode(ErrorCode.ACCOUNT_EMAIL_NOT_FOUND);
            apiResponseDto.setMessage("Email not found");
            return apiResponseDto;
        }
        if (account.getResetPasswordExpired() == null || account.getResetPasswordExpired().getTime() < System.currentTimeMillis()) {
            account.setResetPassword(null);
            account.setResetPasswordExpired(null);
            accountRepository.save(account);
            apiResponseDto.setResult(false);
            apiResponseDto.setCode(ErrorCode.ACCOUNT_OTP_EXPIRED);
            apiResponseDto.setMessage("OTP has expired. Please request a new OTP!");
            return apiResponseDto;
        }
        if (!otpService.validate(otpForm.getOtp(), account.getResetPassword())) {
            apiResponseDto.setResult(false);
            apiResponseDto.setCode(ErrorCode.ACCOUNT_OTP_INVALID);
            apiResponseDto.setMessage("Invalid OTP. Please check again!");
            return apiResponseDto;
        }
        account.setResetPassword(null);
        account.setResetPasswordExpired(null);
        accountRepository.save(account);
        apiResponseDto.setMessage("OTP is valid!");
        return apiResponseDto;
    }

    @PutMapping(value="/reset-password", produces = MediaType.APPLICATION_JSON_VALUE)
    @Transactional
    @ApiIgnore
    public ApiResponseDto<Boolean> resetPassword(@Valid @RequestBody ForgotPasswordForm forgotPasswordForm, BindingResult bindingResult) {
        ApiResponseDto<Boolean> apiResponseDto = new ApiResponseDto<>();
        Account account = accountRepository.findFirstByEmail(forgotPasswordForm.getEmail());
        if (account == null) {
            apiResponseDto.setResult(false);
            apiResponseDto.setCode(ErrorCode.ACCOUNT_EMAIL_NOT_FOUND);
            apiResponseDto.setMessage("Email not found");
            return apiResponseDto;
        }
        account.setPassword(passwordEncoder.encode(forgotPasswordForm.getPassword()));
        accountRepository.save(account);
        apiResponseDto.setMessage("Password has been reset successfully!");
        return apiResponseDto;
    }
}
