package cz.muni.fi.userservice.service;

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
        return managerRepository.save(manager);
    }

    @Override
    public void deleteById(Long id) {
        managerRepository.deleteById(id);
    }

    @Override
    public Manager findById(Long id) {
        return managerRepository.findById(id).orElse(null);
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
        Manager manager = managerRepository.findById(managerId).orElse(null);
        if (manager == null) {
            throw new IllegalArgumentException("Manager with ID " + managerId + " does not exist");
        }

        manager.setManagedBandIds(bandIds);
        return managerRepository.save(manager);
    }
}
