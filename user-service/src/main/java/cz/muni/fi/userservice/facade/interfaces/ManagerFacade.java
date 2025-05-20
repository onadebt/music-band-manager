package cz.muni.fi.userservice.facade.interfaces;

import cz.muni.fi.userservice.dto.ManagerDto;
import cz.muni.fi.userservice.dto.ManagerUpdateDto;

import java.util.List;
import java.util.Set;

public interface ManagerFacade {
    ManagerDto register(ManagerDto managerDto);

    ManagerDto findById(Long id);

    List<ManagerDto> findAll();

    void deleteById(Long id);

    ManagerDto update(Long id, ManagerUpdateDto updateDto);

    ManagerDto updateBandIds(Long managerId, Set<Long> bandIds);

    List<ManagerDto> findByBandIds(Set<Long> bandIds);
}
