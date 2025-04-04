package cz.muni.fi.userservice.facade;

import cz.muni.fi.userservice.dto.ManagerDTO;
import cz.muni.fi.userservice.facade.interfaces.IManagerFacade;
import cz.muni.fi.userservice.mappers.ManagerMapper;
import cz.muni.fi.userservice.model.Manager;
import cz.muni.fi.userservice.service.interfaces.IManagerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class ManagerFacade implements IManagerFacade {
    @Autowired
    private IManagerService managerService;

    @Autowired
    private ManagerMapper managerMapper;


    public ManagerDTO register(ManagerDTO managerDTO) {
        if (managerDTO == null) {
            throw new IllegalArgumentException("ManagerDTO cannot be null");
        }
        Manager manager = managerMapper.toEntity(managerDTO);
        manager = managerService.save(manager);
        return managerMapper.toDTO(manager);
    }

    public ManagerDTO findById(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("ID cannot be null");
        }
        Manager manager = managerService.findById(id);
        if (manager == null) {
            throw new IllegalArgumentException("Manager with ID " + id + " does not exist");
        }
        return managerMapper.toDTO(manager);
    }

    public List<ManagerDTO> findAll() {
        return managerService.findAll().stream()
                .map(managerMapper::toDTO)
                .collect(Collectors.toList());
    }

    public void deleteById(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("ID cannot be null");
        }
        if (managerService.findById(id) == null) {
            throw new IllegalArgumentException("Manager with ID " + id + " does not exist");
        }
        managerService.deleteById(id);
    }


    public List<ManagerDTO> findByBandIds(Set<Long> bandIds) {
        if (bandIds == null) {
            throw new IllegalArgumentException("Band IDs cannot be null or empty");
        }
        return managerService.findByManagedBandIds(bandIds).stream()
                .map(managerMapper::toDTO)
                .collect(Collectors.toList());
    }

    public ManagerDTO updateBandIds(Long managerId, Set<Long> bandIds) {
        if (managerId == null) {
            throw new IllegalArgumentException("Manager ID cannot be null");
        }
        if (bandIds == null) {
            throw new IllegalArgumentException("Band IDs cannot be null");
        }
        Manager manager = managerService.updateManagerBandIds(managerId, bandIds);
        return managerMapper.toDTO(manager);
    }
}
