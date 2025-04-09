package cz.muni.fi.userservice.facade.interfaces;

import cz.muni.fi.userservice.dto.ManagerDto;
import cz.muni.fi.userservice.dto.ManagerUpdateDto;

import java.util.List;

public interface IManagerFacade {
    ManagerDto register(ManagerDto managerDto);

    ManagerDto findById(Long id);

    List<ManagerDto> findAll();

    void deleteById(Long id);

    ManagerDto update(Long id, ManagerUpdateDto updateDto);
}
