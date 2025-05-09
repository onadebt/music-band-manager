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
        if (tourId == null)
            throw new IllegalArgumentException("Tour ID cannot be null");

        if (tourId < 0)
            throw new IllegalArgumentException("Invalid tour ID: " + tourId);

        return tourRepository.findById(tourId)
                .orElseThrow(() -> new ResourceNotFoundException("Tour not found with id: " + tourId));
    }


    public List<Tour> getToursByBand(Long bandId) {
        if (bandId == null)
            throw new IllegalArgumentException("Band ID cannot be null");

        if (bandId < 0)
            throw new IllegalArgumentException("Invalid band ID: " + bandId);

        return tourRepository.findAllByBandId(bandId);
    }


    @Transactional
    public Tour createTour(Tour tour) {
        if (tour == null)
            throw new IllegalArgumentException("Album cannot be null");

        return tourRepository.save(tour);
    }

    @Transactional
    public Tour updateTour(Long id, Tour updatedTour) {
        if (id == null || id < 0)
            throw new IllegalArgumentException("Tour ID cannot be null or < 0");

        if (updatedTour == null)
            throw new IllegalArgumentException("UpdatedTour cannot be null");

        Tour tour = getTourById(id);
        tour.setTourName(updatedTour.getTourName());
        tour.setCityVisits(updatedTour.getCityVisits());
        tour.setBandId(updatedTour.getBandId());
        return tourRepository.save(tour);
    }

    @Transactional
    public void deleteTour(Long tourId) {
        if (tourId == null)
            throw new IllegalArgumentException("Tour ID cannot be null");

        if (tourId < 0)
            throw new IllegalArgumentException("Invalid tour ID: " + tourId);

        getTourById(tourId);
        tourRepository.deleteById(tourId);
    }

    public void addCityVisitToTour(Long tourId, CityVisit cityVisit) {
        if (tourId == null || tourId < 0)
            throw new IllegalArgumentException("Tour ID cannot be null or < 0");

        if (cityVisit == null)
            throw new IllegalArgumentException("UpdatedTour cannot be null");

        Tour tour = getTourById(tourId);
        CityVisit savedCityVisit = cityVisitRepository.save(cityVisit);
        tour.addCityVisit(savedCityVisit);
        tourRepository.save(tour);
    }

    public void removeCityVisitFromTour(Long tourId, Long cityVisitId) {
        if (tourId == null || tourId < 0)
            throw new IllegalArgumentException("Tour ID cannot be null or < 0");

        if (cityVisitId == null || cityVisitId < 0)
            throw new IllegalArgumentException("CityVisit ID cannot be null or < 0");

        Tour tour = getTourById(tourId);
        CityVisit cityVisit = cityVisitRepository.findById(cityVisitId)
                .orElseThrow(() -> new ResourceNotFoundException("City visit not found with id: " + cityVisitId));
        tour.removeCityVisit(cityVisit);
        cityVisitRepository.delete(cityVisit);
    }
}
