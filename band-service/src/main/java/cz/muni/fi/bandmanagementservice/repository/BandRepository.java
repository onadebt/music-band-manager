package cz.muni.fi.bandmanagementservice.repository;


import cz.muni.fi.bandmanagementservice.model.Band;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * @author Tomáš MAREK
 */
@Repository
public interface BandRepository extends JpaRepository<Band, Long> {
    Optional<Band> findByName(String name);
}
