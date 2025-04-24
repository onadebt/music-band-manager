package cz.muni.fi.tourmanagementservice.facades;


import cz.muni.fi.tourmanagementservice.dto.CityVisitDTO;
import cz.muni.fi.tourmanagementservice.mapper.CityVisitMapper;
import cz.muni.fi.tourmanagementservice.mapper.TourMapper;
import cz.muni.fi.tourmanagementservice.model.CityVisit;
import cz.muni.fi.tourmanagementservice.model.Tour;
import cz.muni.fi.tourmanagementservice.service.CityVisitService;
import cz.muni.fi.tourmanagementservice.service.TourService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CityVisitFacade {

    private final CityVisitService cityVisitService;
    private final CityVisitMapper cityVisitMapper;

    @Autowired
    public CityVisitFacade(CityVisitService cityVisitService, CityVisitMapper cityVisitMapper) {
        this.cityVisitService = cityVisitService;
        this.cityVisitMapper = cityVisitMapper;
    }

    public List<CityVisitDTO> getAllCityVisits() {
        return cityVisitService.getAllCityVisits().stream()
                .map(cityVisitMapper::toDTO)
                .collect(Collectors.toList());
    }

    public CityVisitDTO getCityVisitById(Long id) {
        CityVisit cityVisit = cityVisitService.getCityVisitById(id);
        return cityVisitMapper.toDTO(cityVisit);
    }

    public CityVisitDTO createCityVisit(CityVisitDTO cityVisitDTO) {
        CityVisit cityVisit = cityVisitMapper.toEntity(cityVisitDTO);

        CityVisit savedCityVisit = cityVisitService.createCityVisit(cityVisit);
        return cityVisitMapper.toDTO(savedCityVisit);
    }


    public CityVisitDTO updateCityVisit(Long id, CityVisitDTO cityVisitDTO) {
        CityVisit cityVisit = cityVisitService.getCityVisitById(id);
        cityVisitMapper.updateEntityFromDto(cityVisitDTO, cityVisit);

        CityVisit updatedCityVisit = cityVisitService.createCityVisit(cityVisit);
        return cityVisitMapper.toDTO(updatedCityVisit);
    }


    public void deleteCityVisit(Long id) {
        cityVisitService.deleteCityVisit(id);
    }
}

