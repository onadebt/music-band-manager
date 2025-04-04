package cz.muni.fi.tourmanagementservice.facades;


import cz.muni.fi.tourmanagementservice.dto.TourDTO;
import cz.muni.fi.tourmanagementservice.exception.ResourceNotFoundException;
import cz.muni.fi.tourmanagementservice.service.TourService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class TourFacade {

    private final TourService tourService;

    @Autowired
    public TourFacade(TourService tourService) {
        this.tourService = tourService;
    }

    public List<TourDTO> getAllTours() {
        return tourService.getAllTours();
    }

    public List<TourDTO> getToursByBand(Long bandId) {
        return tourService.getToursByBand(bandId);
    }

    public TourDTO getTourById(Long id) {
        return tourService.getTourById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Tour not found with id: " + id));
    }

    public TourDTO createTour(TourDTO tourDTO) {
        return tourService.createTour(tourDTO);
    }

    public TourDTO updateTour(Long id, TourDTO tourDTO) {
        TourDTO updatedTour = tourService.updateTour(id, tourDTO);
        if (updatedTour == null) {
            throw new ResourceNotFoundException("Tour not found with id: " + id);
        }
        return updatedTour;
    }

    public void deleteTour(Long id) {
        boolean deleted = tourService.deleteTour(id);
        if (!deleted) {
            throw new ResourceNotFoundException("Tour not found with id: " + id);
        }
    }
}

