package cz.muni.fi.userservice.repository;


import cz.muni.fi.userservice.model.Manager;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
public interface ManagerRepository extends JpaRepository<Manager, Long> {
    Optional<Manager> findByUsername(String username);

    @Query("SELECT m FROM Manager m JOIN m.managedBandIds mb WHERE mb IN :bandIds")
    List<Manager> findByManagedBandIds (Set<Long> bandIds);
}
