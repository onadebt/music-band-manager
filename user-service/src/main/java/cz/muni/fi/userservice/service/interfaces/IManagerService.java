package cz.muni.fi.userservice.service.interfaces;

import cz.muni.fi.userservice.model.Artist;
import cz.muni.fi.userservice.model.Manager;

import java.util.List;
import java.util.Set;

public interface IManagerService {
    Manager save(Manager manager);

    void deleteById(Long id);

    Manager findById(Long id);

    List<Manager> findAll();

    List<Manager> findByManagedBandIds(Set<Long> bandIds);

    Manager updateManagerByBandIds(Long managerId, Set<Long> bandIds);

}
