package cz.muni.fi.tourmanagementservice.facades;


import cz.muni.fi.tourmanagementservice.dto.CityVisitDTO;
import cz.muni.fi.tourmanagementservice.dto.TourDTO;
import cz.muni.fi.tourmanagementservice.mapper.TourMapper;
import cz.muni.fi.tourmanagementservice.model.Tour;
import cz.muni.fi.tourmanagementservice.service.CityVisitService;
import cz.muni.fi.tourmanagementservice.service.TourService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class TourFacade {

    private final TourService tourService;
    private final TourFacade tourFacade;
    private final CityVisitService cityVisitService;
    private final CityVisitFacade cityVisitFacade;
    private final TourMapper tourMapper;

    @Autowired
    public TourFacade(TourService tourService, TourFacade tourFacade, CityVisitService cityVisitService, CityVisitFacade cityVisitFacade, TourMapper tourMapper) {
        this.tourService = tourService;
        this.tourFacade = tourFacade;
        this.cityVisitService = cityVisitService;
        this.cityVisitFacade = cityVisitFacade;
        this.tourMapper = tourMapper;
    }

    public List<TourDTO> getAllTours() {
        return tourService.getAllTours().stream()
                .map(this::enrichTourWithCityVisits)
                .collect(Collectors.toList());
    }

    public List<TourDTO> getToursByBand(Long bandId) {
        return tourService.getToursByBand(bandId).stream()
                .map(this::enrichTourWithCityVisits)
                .collect(Collectors.toList());
    }

    public TourDTO getTourById(Long id) {
        Tour tour = tourService.getTourById(id);
        return enrichTourWithCityVisits(tour);
    }

    public TourDTO createTour(TourDTO tourDTO) {
        Tour tour = tourMapper.toEntity(tourDTO);
        Tour savedTour = tourService.createTour(tour);
        return enrichTourWithCityVisits(savedTour);
    }

    public TourDTO updateTour(Long id, TourDTO tourDTO) {
        Tour updatedTour = tourService.updateTour(id, tourMapper.toEntity(tourDTO));
        return enrichTourWithCityVisits(updatedTour);
    }

    public void deleteTour(Long id) {
        tourService.deleteTour(id);
    }




    private TourDTO enrichTourWithCityVisits(Tour tour) {
        TourDTO tourDTO = tourMapper.toDTO(tour);
        List<CityVisitDTO> cityVisits = cityVisitFacade.getCityVisitsByTour(tour.getId());
        tourDTO.setCityVisits(cityVisits);
        return tourDTO;
    }

}

