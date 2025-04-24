package cz.muni.fi.tourmanagementservice.repository;

import cz.muni.fi.tourmanagementservice.model.CityVisit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CityVisitRepository extends JpaRepository<CityVisit, Long> {
//    @Query("SELECT c FROM CityVisit c WHERE c.tour.id = ?1")
//    List<CityVisit> findAllByTourId(Long id);
}
