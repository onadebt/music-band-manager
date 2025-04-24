package cz.muni.fi.tourmanagementservice.facades;


import cz.muni.fi.tourmanagementservice.dto.CityVisitDTO;
import cz.muni.fi.tourmanagementservice.dto.TourDTO;
import cz.muni.fi.tourmanagementservice.mapper.CityVisitMapper;
import cz.muni.fi.tourmanagementservice.mapper.TourMapper;
import cz.muni.fi.tourmanagementservice.model.Tour;
import cz.muni.fi.tourmanagementservice.service.TourService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TourFacade {

    private final TourService tourService;
    private final TourMapper tourMapper;
    private final CityVisitMapper cityVisitMapper;

    @Autowired
    public TourFacade(TourService tourService, TourMapper tourMapper, CityVisitMapper cityVisitMapper) {
        this.tourService = tourService;
        this.tourMapper = tourMapper;
        this.cityVisitMapper = cityVisitMapper;
    }

    public List<TourDTO> getAllTours() {
        return tourService.getAllTours().stream()
                .map(tourMapper::toDTO)
                .collect(Collectors.toList());
    }

    public List<TourDTO> getToursByBand(Long bandId) {
        return tourService.getToursByBand(bandId).stream()
                .map(tourMapper::toDTO)
                .collect(Collectors.toList());
    }

    public TourDTO getTourById(Long id) {
        Tour tour = tourService.getTourById(id);
        return tourMapper.toDTO(tour);
    }

    public TourDTO createTour(TourDTO tourDTO) {
        Tour tour = tourMapper.toEntity(tourDTO);
        System.out.println(tour.toString());
        Tour savedTour = tourService.createTour(tour);
        return tourMapper.toDTO(savedTour);
    }

    public TourDTO updateTour(Long id, TourDTO tourDTO) {
        Tour updatedTour = tourService.updateTour(id, tourMapper.toEntity(tourDTO));
        return tourMapper.toDTO(updatedTour);
    }

    public void deleteTour(Long id) {
        tourService.deleteTour(id);
    }

    public void addCityVisitToTour(Long tourId, CityVisitDTO cityVisitDTO) {
        tourService.addCityVisitToTour(tourId, cityVisitMapper.toEntity(cityVisitDTO));
    }

    public void removeCityVisitFromTour(Long tourId, Long cityVisitId) {
        tourService.removeCityVisitFromTour(tourId, cityVisitId);
    }
}

