package com.ecommerce.website.movie.mapper;

import com.ecommerce.website.movie.dto.user.UserDto;
import com.ecommerce.website.movie.form.account.CreateAccountForm;
import com.ecommerce.website.movie.form.user.CreateUserForm;
import com.ecommerce.website.movie.form.user.UpdateUserForm;
import com.ecommerce.website.movie.model.User;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        uses = {AccountMapper.class})
public interface UserMapper {
    @Mapping(source = "username", target = "account.username")
    @Mapping(source = "email", target = "account.email")
    @Mapping(source = "phone", target = "account.phone")
    @Mapping(target = "dateOfBirth", source = "dateOfBirth")
    @Mapping(target = "gender", source = "gender")
    @BeanMapping(ignoreByDefault = true)
    User  formCreateUserFormToEntity(CreateUserForm createUserForm);

    @Mapping(target = "id", source = "id")
    @Mapping(target = "account.username", source = "account.username")
    @Mapping(target = "account.email", source = "account.email")
    @Mapping(target = "account.phone", source = "account.phone")
    @Mapping(target = "account.avatarPath", source = "account.avatarPath")
    @Mapping(target = "dateOfBirth", source = "dateOfBirth")
    @Mapping(target = "gender", source = "gender")
    @BeanMapping(ignoreByDefault = true)
    @Named("fromEntityToDtoForClient")
    UserDto fromEntityToDtoForClient(User user);

    @Mapping(target = "id", source = "user.id")
    @Mapping(target = "account.username", source = "account.username")
    @Mapping(target = "account.email", source = "account.email")
    @Mapping(target = "account.phone", source = "account.phone")
    @Mapping(target = "account.avatarPath", source = "account.avatarPath")
    @Mapping(target = "dateOfBirth", source = "dateOfBirth")
    @Mapping(target = "gender", source = "gender")
    @Mapping(target = "account.role", source = "account.role")
    @Mapping(target = "createdDate", source = "createdDate")
    @Mapping(target = "modifiedDate", source = "modifiedDate")
    @Mapping(target = "status", source = "status")
    @BeanMapping(ignoreByDefault = true)
    @Named("fromEntityToDtoForServer")
    UserDto fromEntityToDtoForServer(User user);

    @IterableMapping(qualifiedByName = "fromEntityToDtoForClient", elementTargetType = UserDto.class)
    @Named("fromEntityToDtoForClientList")
    List<UserDto> fromEntityToDtoForClientList(List<User> users);

    @IterableMapping(qualifiedByName = "fromEntityToDtoForServer", elementTargetType = UserDto.class)
    @Named("fromEntityToDtoForServerList")
    List<UserDto> fromEntityToDtoForServerList(List<User> users);

    @Mapping(target = "dateOfBirth", source = "dateOfBirth")
    @Mapping(target = "gender", source = "gender")
    void updateUserFormToEntity(UpdateUserForm updateUserForm, @MappingTarget User user);
}
