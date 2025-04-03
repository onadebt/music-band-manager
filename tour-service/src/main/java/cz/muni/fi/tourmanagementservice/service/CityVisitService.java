package cz.muni.fi.tourmanagementservice.service;


import cz.muni.fi.tourmanagementservice.dto.CityVisitDTO;
import cz.muni.fi.tourmanagementservice.model.CityVisit;
import cz.muni.fi.tourmanagementservice.repository.CityVisitRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CityVisitService {

    private final CityVisitRepository cityVisitRepository;

    @Autowired
    public CityVisitService(CityVisitRepository cityVisitRepository) {
        this.cityVisitRepository = cityVisitRepository;
    }

    public List<CityVisitDTO> getAllCityVisits() {
        return cityVisitRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public Optional<CityVisitDTO> getCityVisitById(Long cityVisitId) {
        return cityVisitRepository.findById(cityVisitId)
                .map(this::convertToDTO);
    }

    public List<CityVisitDTO> getCityVisitByTour(Long tourId) {
        return cityVisitRepository.findByTourId(tourId).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }



    @Transactional
    public CityVisitDTO createCityVisit(CityVisitDTO cityVisitDTO) {
        CityVisit cityVisit = convertToEntity(cityVisitDTO);

        CityVisit savedCityVisit = cityVisitRepository.save(cityVisit);
        return convertToDTO(savedCityVisit);
    }


    @Transactional
    public CityVisitDTO updateCityVisit(Long id, CityVisitDTO cityVisitDTO) {
        Optional<CityVisit> existingCityVisit = cityVisitRepository.findById(id);

        if (existingCityVisit.isPresent()) {
            CityVisit cityVisit = existingCityVisit.get();
            cityVisit.setCityName(cityVisitDTO.getCityName());
            cityVisit.setDateFrom(cityVisitDTO.getDateFrom());
            cityVisit.setDateTo(cityVisitDTO.getDateTo());

            CityVisit updatedCityVisit = cityVisitRepository.save(cityVisit);
            return convertToDTO(updatedCityVisit);
        }
        return null;
    }

    @Transactional
    public boolean deleteCityVisit(Long id) {
        if (cityVisitRepository.existsById(id)) {
            cityVisitRepository.deleteById(id);
            return true;
        }
        return false;
    }










    private CityVisitDTO convertToDTO(CityVisit cityVisit) {
        CityVisitDTO cityVisitDTO = new CityVisitDTO();
        cityVisitDTO.setId(cityVisit.getId());
        cityVisitDTO.setCityName(cityVisit.getCityName());
        cityVisitDTO.setDateFrom(cityVisit.getDateFrom());
        cityVisitDTO.setDateTo(cityVisit.getDateTo());

        return cityVisitDTO;
    }

    private CityVisit convertToEntity(CityVisitDTO cityVisitDTO) {
        CityVisit cityVisit = new CityVisit();
        cityVisit.setCityName(cityVisitDTO.getCityName());
        cityVisit.setDateFrom(cityVisitDTO.getDateFrom());
        cityVisit.setDateTo(cityVisitDTO.getDateTo());
        return cityVisit;
    }
}
