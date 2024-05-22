package com.ecommerce.website.movie.mapper;

import com.ecommerce.website.movie.dto.account.AccountDto;
import com.ecommerce.website.movie.form.account.CreateAccountForm;
import com.ecommerce.website.movie.form.account.UpdateAccountForm;
import com.ecommerce.website.movie.model.Account;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface AccountMapper {
    @Mapping(target = "email", source = "email")
    @Mapping(target = "username", source = "username")
    @Mapping(target = "phone", source = "phone")
    @Mapping(target = "avatarPath", source = "avatarPath")
    @BeanMapping(ignoreByDefault = true)
    Account fromCreateAccountToDto(CreateAccountForm createAccountForm);

    @Mapping(target = "id", source = "id")
    @Mapping(target = "email", source = "email")
    @Mapping(target = "role", source = "role")
    @Mapping(target = "username", source = "username")
    @Mapping(target = "phone", source = "phone")
    @Mapping(target = "lastLogin", source = "lastLogin")
    @Mapping(target = "createdDate", source = "createdDate")
    @Mapping(target = "modifiedDate", source = "modifiedDate")
    @Mapping(target = "avatarPath", source = "avatarPath")
    @Named("fromAccountEntityToDtoForServer")
    @BeanMapping(ignoreByDefault = true)
    AccountDto fromAccountEntityToDtoForServer(Account account);

    @Mapping(target = "id", source = "id")
    @Mapping(target = "email", source = "email")
    @Mapping(target = "role", source = "role")
    @Mapping(target = "username", source = "username")
    @Mapping(target = "phone", source = "phone")
    @Mapping(target = "avatarPath", source = "avatarPath")
    @Named("fromEntityToDtoForClient")
    @BeanMapping(ignoreByDefault = true)
    AccountDto fromEntityToDtoForClient(Account account);

    @IterableMapping(qualifiedByName = "fromAccountEntityToDtoForServer", elementTargetType = AccountDto.class)
    @Named("fromEntityToDtoListForServer")
    List<AccountDto> fromEntityToDtoListForServer(List<Account> accounts);

    @IterableMapping(qualifiedByName = "fromAccountEntityToDtoForServer", elementTargetType = AccountDto.class)
    @Named("fromEntityToDtoListForClient")
    List<AccountDto> fromEntityToDtoListForClient(List<Account> accounts);

    @Mapping(target = "email", source = "email")
    @Mapping(target = "avatarPath", source = "avatarPath")
    @Mapping(target = "username", source = "username")
    @Mapping(target = "phone", source = "phone")
    @BeanMapping(ignoreByDefault = true)
    @Named("fromUpdateAccountEntityToDto")
    void fromUpdateAccountEntityToDto(UpdateAccountForm updateAccountForm, @MappingTarget Account account);
}
