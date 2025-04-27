package cz.muni.fi.tourmanagementservice.service;


import cz.muni.fi.tourmanagementservice.exception.ResourceNotFoundException;
import cz.muni.fi.tourmanagementservice.model.CityVisit;
import cz.muni.fi.tourmanagementservice.repository.CityVisitRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CityVisitService {

    private final CityVisitRepository cityVisitRepository;

    @Autowired
    public CityVisitService(CityVisitRepository cityVisitRepository) {
        this.cityVisitRepository = cityVisitRepository;
    }

    public List<CityVisit> getAllCityVisits() {
        return cityVisitRepository.findAll();
    }

    public CityVisit getCityVisitById(Long cityVisitId) {
        if (cityVisitId == null)
            throw new IllegalArgumentException("CityVisit ID cannot be null");

        if (cityVisitId < 0)
            throw new IllegalArgumentException("Invalid CityVisit ID: " + cityVisitId);

        return cityVisitRepository.findById(cityVisitId)
                .orElseThrow(() -> new ResourceNotFoundException("CityVisit not found with id: " + cityVisitId));
    }


    @Transactional
    public CityVisit createCityVisit(CityVisit cityVisit) {
        if (cityVisit == null)
            throw new IllegalArgumentException("Album cannot be null");

        return cityVisitRepository.save(cityVisit);
    }


    @Transactional
    public CityVisit updateCityVisit(Long id, CityVisit updatedCityVisit) {
        if (id == null || id < 0)
            throw new IllegalArgumentException("CityVisit ID cannot be null or < 0");

        if (updatedCityVisit == null)
            throw new IllegalArgumentException("UpdatedCityVisit cannot be null");

        CityVisit cityVisit = getCityVisitById(id);

        cityVisit.setCityName(updatedCityVisit.getCityName());
        cityVisit.setDateFrom(updatedCityVisit.getDateFrom());
        cityVisit.setDateTo(updatedCityVisit.getDateTo());
        return cityVisitRepository.save(cityVisit);
    }

    @Transactional
    public void deleteCityVisit(Long id) {
        if (id == null)
            throw new IllegalArgumentException("CityVisit ID cannot be null");

        if (id < 0)
            throw new IllegalArgumentException("Invalid CityVisit ID: " + id);

        getCityVisitById(id);
        cityVisitRepository.deleteById(id);
    }
}
