package cz.muni.fi.bandmanagementservice.repository;


import cz.muni.fi.bandmanagementservice.model.Band;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author Tomáš MAREK
 */
@Repository
public interface BandRepository extends JpaRepository<Band, Long> {
}
