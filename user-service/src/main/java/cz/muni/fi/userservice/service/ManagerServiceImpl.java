package cz.muni.fi.userservice.service;

import cz.muni.fi.userservice.exception.UserAlreadyExistsException;
import cz.muni.fi.userservice.exception.UserNotFoundException;
import cz.muni.fi.userservice.model.Manager;
import cz.muni.fi.userservice.repository.ManagerRepository;
import cz.muni.fi.userservice.service.interfaces.ManagerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;

@Transactional
@Service
public class ManagerServiceImpl implements ManagerService {

    private final ManagerRepository managerRepository;
    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @Autowired
    public ManagerServiceImpl(ManagerRepository managerRepository) {
        this.managerRepository = managerRepository;
    }

    public Manager save(Manager manager) {
        var existingManager = managerRepository.findByUsername(manager.getUsername());
        if (existingManager.isPresent()) {
            throw new UserAlreadyExistsException(existingManager.get());
        }

        String hashedPassword = passwordEncoder.encode(manager.getPassword());
        manager.setPassword(hashedPassword);
        return managerRepository.save(manager);
    }

    public void deleteById(Long id) {
        var maybeManager = managerRepository.findById(id);
        if (maybeManager.isEmpty()) {
            throw new UserNotFoundException(id);
        }
        managerRepository.deleteById(maybeManager.get().getId());
    }

    @Transactional(readOnly = true)
    public Manager findById(Long id) {
        return managerRepository.findById(id).orElseThrow(() -> new UserNotFoundException(id));
    }

    @Transactional(readOnly = true)
    public List<Manager> findAll() {
        return managerRepository.findAll();
    }

    @Transactional(readOnly = true)
    public List<Manager> findByManagedBandIds(Set<Long> bandIds) {
        return managerRepository.findByManagedBandIds(bandIds);
    }

    public Manager updateManagerBandIds(Long managerId, Set<Long> bandIds) {
        Manager manager = managerRepository.findById(managerId).orElseThrow(() -> new UserNotFoundException(managerId));
        manager.setManagedBandIds(bandIds);
        return managerRepository.save(manager);
    }

    public Manager updateManager(Long id, Manager manager) {
        Manager existingManager = managerRepository.findById(manager.getId()).orElseThrow(() -> new UserNotFoundException(manager.getId()));
        existingManager.setUsername(manager.getUsername());
        existingManager.setEmail(manager.getEmail());
        existingManager.setPassword(passwordEncoder.encode(manager.getPassword()));
        existingManager.setManagedBandIds(manager.getManagedBandIds());
        existingManager.setFirstName(manager.getFirstName());
        existingManager.setLastName(manager.getLastName());
        existingManager.setCompanyName(manager.getCompanyName());
        return managerRepository.save(existingManager);
    }
}
