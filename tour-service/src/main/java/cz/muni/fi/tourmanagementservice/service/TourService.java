package cz.muni.fi.tourmanagementservice.service;

import cz.muni.fi.tourmanagementservice.exception.ResourceNotFoundException;
import cz.muni.fi.tourmanagementservice.model.Tour;
import cz.muni.fi.tourmanagementservice.repository.TourRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class TourService {

    private final TourRepository tourRepository;

    @Autowired
    public TourService(TourRepository tourRepository) {
        this.tourRepository = tourRepository;
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

        if (updatedTour.getBandId() != null)
            tour.setBandId(updatedTour.getBandId());

        return tourRepository.save(tour);
    }

    @Transactional
    public void deleteTour(Long id) {
        getTourById(id);
        tourRepository.deleteById(id);
    }
}
