package cz.muni.fi.userservice.mappers;

import cz.muni.fi.userservice.dto.ManagerDTO;
import cz.muni.fi.userservice.model.Manager;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = UserMapper.class)
public interface ManagerMapper {
    Manager toEntity(ManagerDTO registerDTO);

    ManagerDTO toDTO(Manager manager);
}
