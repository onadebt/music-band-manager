package cz.muni.fi.userservice.facade;

import cz.muni.fi.userservice.dto.ManagerDto;
import cz.muni.fi.userservice.dto.ManagerUpdateDto;
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

    private final IManagerService managerService;
    private final ManagerMapper managerMapper;

    @Autowired
    public ManagerFacade(IManagerService managerService, ManagerMapper managerMapper) {
        this.managerService = managerService;
        this.managerMapper = managerMapper;
    }

    public ManagerDto register(ManagerDto managerDto) {
        if (managerDto == null) {
            throw new IllegalArgumentException("ManagerDTO cannot be null");
        }
        Manager manager = managerMapper.toEntity(managerDto);
        manager = managerService.save(manager);
        return managerMapper.toDto(manager);
    }

    public ManagerDto update(Long id, ManagerUpdateDto managerUpdateDto) {
        if (id == null) {
            throw new IllegalArgumentException("ID cannot be null");
        }
        if (managerUpdateDto == null) {
            throw new IllegalArgumentException("ManagerDTO cannot be null");
        }

        Manager manager = managerService.findById(id);
        Manager updatedManager = managerMapper.updateManagerFromDto(managerUpdateDto, manager);
        updatedManager = managerService.updateManager(id, updatedManager);
        return managerMapper.toDto(updatedManager);
    }

    public ManagerDto findById(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("ID cannot be null");
        }
        Manager manager = managerService.findById(id);
        if (manager == null) {
            throw new IllegalArgumentException("Manager with ID " + id + " does not exist");
        }
        return managerMapper.toDto(manager);
    }

    public List<ManagerDto> findAll() {
        return managerService.findAll().stream()
                .map(managerMapper::toDto)
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

    public List<ManagerDto> findByBandIds(Set<Long> bandIds) {
        if (bandIds == null) {
            throw new IllegalArgumentException("Band IDs cannot be null or empty");
        }
        List<Manager> managers = managerService.findByManagedBandIds(bandIds);
        return managers.stream()
                .map(managerMapper::toDto)
                .collect(Collectors.toList());
    }

    public ManagerDto updateBandIds(Long managerId, Set<Long> bandIds) {
        if (managerId == null) {
            throw new IllegalArgumentException("Manager ID cannot be null");
        }
        if (bandIds == null) {
            throw new IllegalArgumentException("Band IDs cannot be null");
        }
        Manager manager = managerService.updateManagerBandIds(managerId, bandIds);
        return managerMapper.toDto(manager);
    }
}
