package cz.muni.fi.userservice.facade.interfaces;

import cz.muni.fi.userservice.dto.ManagerDTO;

import java.util.List;

public interface IManagerFacade {
    ManagerDTO register(ManagerDTO managerDTO);

    ManagerDTO findById(Long id);

    List<ManagerDTO> findAll();

    void deleteById(Long id);
}
