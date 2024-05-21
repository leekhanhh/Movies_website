package com.ecommerce.website.movie.mapper;

import com.ecommerce.website.movie.dto.user.UserDto;
import com.ecommerce.website.movie.form.user.CreateUserForm;
import com.ecommerce.website.movie.form.user.UpdateUserForm;
import com.ecommerce.website.movie.model.User;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface UserMapper {
    @Mapping(target = "dateOfBirth", source = "dateOfBirth")
    @Mapping(target = "gender", source = "gender")
    @BeanMapping(ignoreByDefault = true)
    User formCreateUserFormToEntity(CreateUserForm createUserForm);

    @Mapping(target = "id", source = "id")
    @Mapping(target = "username", source = "account.username")
    @Mapping(target = "email", source = "account.email")
    @Mapping(target = "phone", source = "account.phone")
    @Mapping(target = "avatarPath", source = "account.avatarPath")
    @Mapping(target = "dateOfBirth", source = "dateOfBirth")
    @Mapping(target = "gender", source = "gender")
    @BeanMapping(ignoreByDefault = true)
    @Named("fromEntityToDtoForClient")
    UserDto fromEntityToDtoForClient(User user);

    @Mapping(target = "id", source = "user.id")
    @Mapping(target = "username", source = "account.username")
    @Mapping(target = "email", source = "account.email")
    @Mapping(target = "phone", source = "account.phone")
    @Mapping(target = "avatarPath", source = "account.avatarPath")
    @Mapping(target = "dateOfBirth", source = "dateOfBirth")
    @Mapping(target = "gender", source = "gender")
    @Mapping(target = "role", source = "account.role")
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

    @Mapping(target = "dateOfBirth", source = "dateOfBirth", dateFormat = "dd-MM-yyyy")
    @Mapping(target = "gender", source = "gender")
    void updateUserFormToEntity(UpdateUserForm updateUserForm, @MappingTarget User user);
}
