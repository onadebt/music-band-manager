package cz.muni.fi.tourmanagementservice.facade;


import cz.muni.fi.tourmanagementservice.dto.CityVisitDto;
import cz.muni.fi.tourmanagementservice.mapper.CityVisitMapper;
import cz.muni.fi.tourmanagementservice.model.CityVisit;
import cz.muni.fi.tourmanagementservice.service.CityVisitService;
import org.springframework.beans.factory.annotation.Autowired;
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

    public List<CityVisitDto> getAllCityVisits() {
        return cityVisitService.getAllCityVisits().stream()
                .map(cityVisitMapper::toDTO)
                .collect(Collectors.toList());
    }

    public CityVisitDto getCityVisitById(Long id) {
        CityVisit cityVisit = cityVisitService.getCityVisitById(id);
        return cityVisitMapper.toDTO(cityVisit);
    }

    public CityVisitDto createCityVisit(CityVisitDto cityVisitDTO) {
        CityVisit cityVisit = cityVisitMapper.toEntity(cityVisitDTO);

        CityVisit savedCityVisit = cityVisitService.createCityVisit(cityVisit);
        return cityVisitMapper.toDTO(savedCityVisit);
    }


    public CityVisitDto updateCityVisit(Long id, CityVisitDto cityVisitDTO) {
        CityVisit cityVisit = cityVisitService.getCityVisitById(id);
        cityVisitMapper.updateEntityFromDto(cityVisitDTO, cityVisit);

        CityVisit updatedCityVisit = cityVisitService.createCityVisit(cityVisit);
        return cityVisitMapper.toDTO(updatedCityVisit);
    }


    public void deleteCityVisit(Long id) {
        cityVisitService.deleteCityVisit(id);
    }
}

