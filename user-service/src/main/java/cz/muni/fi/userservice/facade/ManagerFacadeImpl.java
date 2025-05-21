package cz.muni.fi.userservice.facade;

import cz.muni.fi.userservice.dto.ManagerDto;
import cz.muni.fi.userservice.dto.ManagerUpdateDto;
import cz.muni.fi.userservice.facade.interfaces.ManagerFacade;
import cz.muni.fi.userservice.mapper.ManagerMapper;
import cz.muni.fi.userservice.model.Manager;
import cz.muni.fi.userservice.service.interfaces.ManagerService;
import jakarta.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Transactional
@Validated
@Service
public class ManagerFacadeImpl implements ManagerFacade {

    private final ManagerService managerService;
    private final ManagerMapper managerMapper;

    @Autowired
    public ManagerFacadeImpl(ManagerService managerService, ManagerMapper managerMapper) {
        this.managerService = managerService;
        this.managerMapper = managerMapper;
    }

    public ManagerDto register(@NotNull ManagerDto managerDto) {
        Manager manager = managerMapper.toEntity(managerDto);
        manager = managerService.save(manager);
        return managerMapper.toDto(manager);
    }

    public ManagerDto update(@NotNull Long id, @NotNull ManagerUpdateDto managerUpdateDto) {
        Manager manager = managerService.findById(id);
        Manager updatedManager = managerMapper.updateManagerFromDto(managerUpdateDto, manager);
        updatedManager = managerService.updateManager(id, updatedManager);
        return managerMapper.toDto(updatedManager);
    }

    public ManagerDto findById(@NotNull Long id) {
        Manager manager = managerService.findById(id);
        return managerMapper.toDto(manager);
    }

    public List<ManagerDto> findAll() {
        return managerService.findAll().stream()
                .map(managerMapper::toDto)
                .collect(Collectors.toList());
    }

    public void deleteById(@NotNull Long id) {
        managerService.deleteById(id);
    }

    public List<ManagerDto> findByBandIds(@NotNull Set<Long> bandIds) {
        List<Manager> managers = managerService.findByManagedBandIds(bandIds);
        return managers.stream()
                .map(managerMapper::toDto)
                .collect(Collectors.toList());
    }

    public ManagerDto updateBandIds(@NotNull Long managerId, @NotNull Set<Long> bandIds) {
        Manager manager = managerService.updateManagerBandIds(managerId, bandIds);
        return managerMapper.toDto(manager);
    }
}
