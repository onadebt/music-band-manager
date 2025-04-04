package cz.muni.fi.userservice.mappers;

import cz.muni.fi.userservice.dto.UserDTO;
import cz.muni.fi.userservice.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface UserMapper {
    void updateUserFromDTO(UserDTO userDTO, @MappingTarget User user);
}
