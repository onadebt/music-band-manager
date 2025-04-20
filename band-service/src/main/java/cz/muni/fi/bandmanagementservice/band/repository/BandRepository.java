package cz.muni.fi.bandmanagementservice.band.repository;


import cz.muni.fi.bandmanagementservice.band.model.Band;
import org.springframework.data.jpa.repository.JpaRepository;
/**
 * @author Tomáš MAREK
 */
public interface BandRepository extends JpaRepository<Band, Long> {
}
