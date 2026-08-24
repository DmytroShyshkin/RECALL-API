package com.dmytro.language_learning_api.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.dmytro.language_learning_api.dto.UsersDTO;
import com.dmytro.language_learning_api.model.Users;

@Mapper(componentModel = "spring")
public interface UsersMapper {

    UsersDTO toDto(Users users);

    @Mapping(target = "emailVerified", ignore = true)
    @Mapping(target = "role", ignore = true)
    @Mapping(target = "verificationToken", ignore = true)
    Users fromDto(UsersDTO usersDTO);

    List<UsersDTO> toDto(List<Users> users);
    List<Users> fromDto(List<UsersDTO> usersDTO);
}
