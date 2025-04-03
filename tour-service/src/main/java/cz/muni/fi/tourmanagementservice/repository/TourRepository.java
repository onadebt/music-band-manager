package cz.muni.fi.tourmanagementservice.repository;

import cz.muni.fi.tourmanagementservice.model.Tour;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface TourRepository extends JpaRepository<Tour, Long> {
    // Find tour by id
    List<Tour> findByTourId(Long tourId);
    // Find tour by band
    List<Tour> findByBandId(Long bandId);
}