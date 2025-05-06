package cz.muni.fi.userservice.mappers;

import cz.muni.fi.userservice.dto.ManagerDto;
import cz.muni.fi.userservice.dto.ManagerUpdateDto;
import cz.muni.fi.userservice.model.Manager;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring", uses = UserMapper.class)
public interface ManagerMapper {
    Manager toEntity(ManagerDto registerDTO);

    ManagerDto toDto(Manager manager);

    default Manager updateManagerFromDto(ManagerUpdateDto updateDto, @MappingTarget Manager manager) {
        if (updateDto.getUsername() != null) {
            manager.setUsername(updateDto.getUsername());
        }
        if (updateDto.getPassword() != null) {
            manager.setPassword(updateDto.getPassword());
        }
        if (updateDto.getEmail() != null) {
            manager.setEmail(updateDto.getEmail());
        }
        if (updateDto.getFirstName() != null) {
            manager.setFirstName(updateDto.getFirstName());
        }
        if (updateDto.getLastName() != null) {
            manager.setLastName(updateDto.getLastName());
        }
        if (updateDto.getCompanyName() != null) {
            manager.setCompanyName(updateDto.getCompanyName());
        }
        if (updateDto.getManagedBandIds() != null) {
            manager.setManagedBandIds(updateDto.getManagedBandIds());
        }
        return manager;
    }
}
