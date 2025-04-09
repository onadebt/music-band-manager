package cz.muni.fi.userservice.service;

import cz.muni.fi.userservice.exception.UserAlreadyExistsException;
import cz.muni.fi.userservice.exception.UserNotFoundException;
import cz.muni.fi.userservice.model.Manager;
import cz.muni.fi.userservice.repository.ManagerRepository;
import cz.muni.fi.userservice.service.interfaces.IManagerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
public class ManagerService implements IManagerService {
    @Autowired
    private ManagerRepository managerRepository;

    @Override
    public Manager save(Manager manager) {
        var existingManager = managerRepository.findByUsername(manager.getUsername());
        if (existingManager.isPresent()) {
            throw new UserAlreadyExistsException(existingManager.get());
        }
        return managerRepository.save(manager);
    }

    @Override
    public void deleteById(Long id) {
        var maybeManager = managerRepository.findById(id);
        if (maybeManager.isEmpty()) {
            throw new UserNotFoundException(id);
        }
        managerRepository.deleteById(maybeManager.get().getId());
    }

    @Override
    public Manager findById(Long id) {
        return managerRepository.findById(id).orElseThrow(() -> new UserNotFoundException(id));
    }

    @Override
    public List<Manager> findAll() {
        return managerRepository.findAll();
    }

    @Override
    public List<Manager> findByManagedBandIds(Set<Long> bandIds) {
        return managerRepository.findByManagedBandIds(bandIds);
    }

    @Override
    public Manager updateManagerBandIds(Long managerId, Set<Long> bandIds) {
        Manager manager = managerRepository.findById(managerId).orElseThrow(() -> new UserNotFoundException(managerId));
        manager.setManagedBandIds(bandIds);
        return managerRepository.save(manager);
    }

    @Override
    public Manager updateManager(Long id, Manager manager) {
        Manager existingManager = managerRepository.findById(manager.getId()).orElseThrow(() -> new UserNotFoundException(manager.getId()));
        existingManager.setUsername(manager.getUsername());
        existingManager.setEmail(manager.getEmail());
        existingManager.setPassword(manager.getPassword());
        existingManager.setManagedBandIds(manager.getManagedBandIds());
        existingManager.setFirstName(manager.getFirstName());
        existingManager.setLastName(manager.getLastName());
        existingManager.setCompanyName(manager.getCompanyName());
        return managerRepository.save(existingManager);
    }
}
