package cz.muni.fi.tourmanagementservice.service;

import cz.muni.fi.tourmanagementservice.exception.ResourceNotFoundException;
import cz.muni.fi.tourmanagementservice.model.CityVisit;
import cz.muni.fi.tourmanagementservice.model.Tour;
import cz.muni.fi.tourmanagementservice.repository.CityVisitRepository;
import cz.muni.fi.tourmanagementservice.repository.TourRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class TourService {

    private final TourRepository tourRepository;
    private final CityVisitRepository cityVisitRepository;

    @Autowired
    public TourService(TourRepository tourRepository, CityVisitRepository cityVisitRepository) {
        this.tourRepository = tourRepository;
        this.cityVisitRepository = cityVisitRepository;
    }

    public List<Tour> getAllTours() {
        return tourRepository.findAll();
    }


    public Tour getTourById(Long tourId) {
        return tourRepository.findById(tourId)
                .orElseThrow(() -> new ResourceNotFoundException("Tour not found with id: " + tourId));
    }


    public List<Tour> getToursByBand(Long bandId) {
        return tourRepository.findByBandId(bandId);
    }


    @Transactional
    public Tour createTour(Tour tour) {
        return tourRepository.save(tour);
    }

    @Transactional
    public Tour updateTour(Long id, Tour updatedTour) {
        Tour tour = getTourById(id);
        tour.setTourName(updatedTour.getTourName());
        tour.setCityVisits(updatedTour.getCityVisits());
        tour.setBandId(updatedTour.getBandId());
        return tourRepository.save(tour);
    }

    @Transactional
    public void deleteTour(Long id) {
        getTourById(id);
        tourRepository.deleteById(id);
    }

    public void addCityVisitToTour(Long tourId, CityVisit cityVisit) {
        Tour tour = getTourById(tourId);
        CityVisit savedCityVisit = cityVisitRepository.save(cityVisit);
        tour.addCityVisit(savedCityVisit);
        tourRepository.save(tour);
    }

    public void removeCityVisitFromTour(Long tourId, Long cityVisitId) {
        Tour tour = getTourById(tourId);
        CityVisit cityVisit = cityVisitRepository.findById(cityVisitId)
                .orElseThrow(() -> new ResourceNotFoundException("City visit not found with id: " + cityVisitId));
        tour.removeCityVisit(cityVisit);
        cityVisitRepository.delete(cityVisit);
    }
}
