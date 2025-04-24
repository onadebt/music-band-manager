package cz.muni.fi.tourmanagementservice.repository;

import cz.muni.fi.tourmanagementservice.model.CityVisit;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CityVisitRepository extends JpaRepository<CityVisit, Long> {
    // Find CityVisit by tour id
    List<CityVisit> findByTourId(Long tourId);
}
