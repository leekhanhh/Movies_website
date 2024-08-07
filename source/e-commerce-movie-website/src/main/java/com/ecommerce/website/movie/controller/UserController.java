package com.ecommerce.website.movie.controller;

import com.ecommerce.website.movie.constant.Constant;
import com.ecommerce.website.movie.dto.ApiResponseDto;
import com.ecommerce.website.movie.dto.ErrorCode;
import com.ecommerce.website.movie.dto.ResponseListDto;
import com.ecommerce.website.movie.dto.user.UserDto;
import com.ecommerce.website.movie.form.user.CreateUserForm;
import com.ecommerce.website.movie.form.user.UpdateUserForm;
import com.ecommerce.website.movie.mapper.AccountMapper;
import com.ecommerce.website.movie.mapper.UserMapper;
import com.ecommerce.website.movie.model.Account;
import com.ecommerce.website.movie.model.User;
import com.ecommerce.website.movie.model.criteria.UserCriteria;
import com.ecommerce.website.movie.repository.AccountRepository;
import com.ecommerce.website.movie.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import javax.validation.Valid;

@org.springframework.web.bind.annotation.RestController
@org.springframework.web.bind.annotation.RequestMapping("/v1/user")
@CrossOrigin(origins = "*", allowedHeaders = "*")
@Slf4j
public class UserController extends BaseController{
    @Autowired
    PasswordEncoder passwordEncoder;
    @Autowired
    UserRepository userRepository;
    @Autowired
    UserMapper userMapper;
    @Autowired 
    AccountRepository accountRepository;
    @Autowired
    AccountMapper accountMapper;

    @PostMapping(value = "/register", produces = org.springframework.http.MediaType.APPLICATION_JSON_VALUE)
    @Transactional
    public ApiResponseDto<Long> createUser(@Valid @RequestBody CreateUserForm createUserForm, BindingResult bindingResult) {
        ApiResponseDto<Long> apiResponseDto = new ApiResponseDto<>();
        Account existingAccount = accountRepository.findFirstByEmail(createUserForm.getEmail());
        if(existingAccount != null){
            apiResponseDto.setResult(false);
            apiResponseDto.setCode(ErrorCode.ACCOUNT_EMAIL_DUPLICATED);
            apiResponseDto.setMessage("Duplicated Email Error");
            return apiResponseDto;
        }

        Account newAccount = accountMapper.fromCreateAccountForUser(createUserForm);
        newAccount.setPassword(passwordEncoder.encode(createUserForm.getPassword()));
        newAccount.setRole(Constant.ROLE_USER);
        accountRepository.save(newAccount);

        User user = userMapper.formCreateUserFormToEntity(createUserForm);
        user.setAccount(newAccount);
        userRepository.save(user);

        apiResponseDto.setResult(true);
        apiResponseDto.setMessage("User has been saved successfully!");
        apiResponseDto.setData(user.getId());

        return apiResponseDto;
    }


    @PutMapping(value = "/update", produces = org.springframework.http.MediaType.APPLICATION_JSON_VALUE)
    @Transactional
    public ApiResponseDto<Long> updateUser(@Valid @RequestBody UpdateUserForm updateUserForm, BindingResult bindingResult){
        ApiResponseDto<Long> apiResponseDto = new ApiResponseDto<>();
        Account account = accountRepository.findById(updateUserForm.getId()).orElse(null);
        User user = userRepository.findById(account.getId()).orElse(null);
        if (account == null) {
            apiResponseDto.setResult(false);
            apiResponseDto.setCode(ErrorCode.USER_ACCOUNT_NOT_FOUND);
            apiResponseDto.setMessage("Account not found");
            return apiResponseDto;
        }
        if(user == null){
            apiResponseDto.setResult(false);
            apiResponseDto.setCode(ErrorCode.USER_ACCOUNT_NOT_FOUND);
            apiResponseDto.setMessage("User not found");
            return apiResponseDto;
        }
        userMapper.updateUserFormToEntity(updateUserForm, user);
        userRepository.save(user);
        apiResponseDto.setMessage("User has been updated successfully!");
        apiResponseDto.setData(account.getId());
        return apiResponseDto;
    }

    @DeleteMapping(value = "/delete", produces = org.springframework.http.MediaType.APPLICATION_JSON_VALUE)
    @Transactional
    public ApiResponseDto<Long> deleteUser(@Valid @RequestBody Long id){
        ApiResponseDto<Long> apiResponseDto = new ApiResponseDto<>();
        User user = userRepository.findById(id).orElse(null);
        if (user == null) {
            apiResponseDto.setResult(false);
            apiResponseDto.setCode(ErrorCode.USER_ACCOUNT_NOT_FOUND);
            apiResponseDto.setMessage("User not found");
            return apiResponseDto;
        }
        userRepository.deleteById(id);
        accountRepository.deleteById(id);
        apiResponseDto.setMessage("User has been deleted successfully!");
        return apiResponseDto;
    }

    @GetMapping(value = "/list", produces = org.springframework.http.MediaType.APPLICATION_JSON_VALUE)
    public ApiResponseDto<ResponseListDto<UserDto>> listUser(UserCriteria userCriteria, Pageable pageable){
        ApiResponseDto<ResponseListDto<UserDto>> apiResponseDto = new ApiResponseDto<>();
        Page<User> userPage = userRepository.findAll(userCriteria.getSpecification(), pageable);
        ResponseListDto<UserDto> responseListDto = new ResponseListDto(userMapper.fromEntityToDtoForClientList(userPage.getContent()), userPage.getTotalElements(), userPage.getTotalPages());
        apiResponseDto.setData(responseListDto);
        apiResponseDto.setMessage("Get user list successfully!");
        return apiResponseDto;
    }

    @GetMapping(value = "/get/{id}", produces = org.springframework.http.MediaType.APPLICATION_JSON_VALUE)
    public ApiResponseDto<UserDto> getUser(@PathVariable("id") Long id){
        ApiResponseDto<UserDto> apiResponseDto = new ApiResponseDto<>();
        User user = userRepository.findById(id).orElse(null);
        if (user == null) {
            apiResponseDto.setResult(false);
            apiResponseDto.setCode(ErrorCode.USER_ACCOUNT_NOT_FOUND);
            apiResponseDto.setMessage("User not found");
            return apiResponseDto;
        }
        apiResponseDto.setData(userMapper.fromEntityToDtoForClient(user));
        apiResponseDto.setMessage("Get user successfully!");
        return apiResponseDto;
    }

    @GetMapping(value = "/my-profile", produces = org.springframework.http.MediaType.APPLICATION_JSON_VALUE)
    public ApiResponseDto<UserDto> myProfile(){
        ApiResponseDto<UserDto> apiResponseDto = new ApiResponseDto<>();
        User user = userRepository.findById(getCurrentUser()).orElse(null);
        if (user == null) {
            apiResponseDto.setResult(false);
            apiResponseDto.setCode(ErrorCode.USER_ACCOUNT_NOT_FOUND);
            apiResponseDto.setMessage("User not found");
            return apiResponseDto;
        }
        apiResponseDto.setData(userMapper.fromEntityToDtoForClient(user));
        apiResponseDto.setMessage("Get user successfully!");
        return apiResponseDto;
    }
}
