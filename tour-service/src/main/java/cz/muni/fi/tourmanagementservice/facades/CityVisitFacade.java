package cz.muni.fi.tourmanagementservice.facades;


import cz.muni.fi.tourmanagementservice.dto.CityVisitDTO;
import cz.muni.fi.tourmanagementservice.exception.ResourceNotFoundException;
import cz.muni.fi.tourmanagementservice.service.CityVisitService;
import cz.muni.fi.tourmanagementservice.service.CityVisitService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class CityVisitFacade {

    private final CityVisitService cityVisitService;

    @Autowired
    public CityVisitFacade(CityVisitService cityVisitService) {
        this.cityVisitService = cityVisitService;
    }

    public List<CityVisitDTO> getAllSongs() {
        return cityVisitService.getAllCityVisits();
    }

    public List<CityVisitDTO> getCityVisitsByTour(Long tourId) {
        return cityVisitService.getCityVisitByTour(tourId);
    }

    public CityVisitDTO getCityVisitById(Long id) {
        return cityVisitService.getCityVisitById(id)
                .orElseThrow(() -> new ResourceNotFoundException("CityVisit not found with id: " + id));
    }

    public CityVisitDTO createCityVisit(CityVisitDTO tourDTO) {
        return cityVisitService.createCityVisit(tourDTO);
    }

    public CityVisitDTO updateCityVisit(Long id, CityVisitDTO tourDTO) {
        CityVisitDTO updatedCityVisit = cityVisitService.updateCityVisit(id, tourDTO);
        if (updatedCityVisit == null) {
            throw new ResourceNotFoundException("CityVisit not found with id: " + id);
        }
        return updatedCityVisit;
    }

    public void deleteCityVisit(Long id) {
        boolean deleted = cityVisitService.deleteCityVisit(id);
        if (!deleted) {
            throw new ResourceNotFoundException("CityVisit not found with id: " + id);
        }
    }
}

