package cz.muni.fi.userservice.facade.interfaces;

import cz.muni.fi.userservice.dto.ManagerDto;
import cz.muni.fi.userservice.dto.ManagerUpdateDto;
import jakarta.validation.constraints.NotNull;

import java.util.List;
import java.util.Set;

public interface ManagerFacade {
    ManagerDto register(@NotNull ManagerDto managerDto);

    ManagerDto findById(@NotNull Long id);

    List<ManagerDto> findAll();

    void deleteById(@NotNull Long id);

    ManagerDto update(@NotNull Long id, @NotNull ManagerUpdateDto updateDto);

    ManagerDto updateBandIds(@NotNull Long managerId, @NotNull Set<Long> bandIds);

    List<ManagerDto> findByBandIds(@NotNull Set<Long> bandIds);
}
