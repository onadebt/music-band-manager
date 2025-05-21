package cz.muni.fi.userservice.mapper;

import cz.muni.fi.userservice.dto.UserDto;
import cz.muni.fi.userservice.dto.UserUpdateDto;
import cz.muni.fi.userservice.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface UserMapper {
    void updateUserFromDto(UserDto userDTO, @MappingTarget User user);
    void updateUserFromDto(UserUpdateDto userUpdateDto, @MappingTarget User user);
}
