package cz.muni.fi.tourmanagementservice.service;

import cz.muni.fi.tourmanagementservice.dto.TourDTO;
import cz.muni.fi.tourmanagementservice.model.Tour;
import cz.muni.fi.tourmanagementservice.repository.TourRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class TourService {

    private final TourRepository tourRepository;

    @Autowired
    public TourService(TourRepository tourRepository) {
        this.tourRepository = tourRepository;
    }

    public List<TourDTO> getAllTours() {
        return tourRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }


    public Optional<TourDTO> getTourById(Long tourId) {
        return tourRepository.findById(tourId)
                .map(this::convertToDTO);
    }


    public List<TourDTO> getToursByBand(Long bandId) {
        return tourRepository.findByBandId(bandId).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }


    @Transactional
    public TourDTO createTour(TourDTO tourDTO) {
        Tour tour = convertToEntity(tourDTO);

        Tour savedTour = tourRepository.save(tour);
        return convertToDTO(savedTour);
    }

    @Transactional
    public TourDTO updateTour(Long id, TourDTO tourDTO) {
        Optional<Tour> existingSong = tourRepository.findById(id);

        if (existingSong.isPresent()) {
            Tour tour = existingSong.get();
            tour.setTourName(tourDTO.getTourName());
            tour.setBandId(tourDTO.getBandId());

            Tour updatedTour = tourRepository.save(tour);
            return convertToDTO(updatedTour);
        }
        return null;
    }

    @Transactional
    public boolean deleteTour(Long id) {
        if (tourRepository.existsById(id)) {
            tourRepository.deleteById(id);
            return true;
        }
        return false;
    }





    private TourDTO convertToDTO(Tour tour) {
        TourDTO tourDTO = new TourDTO();
        tourDTO.setId(tour.getId());
        tourDTO.setBandId(tour.getBandId());
        tourDTO.setTourName(tour.getTourName());

        return tourDTO;
    }

    private Tour convertToEntity(TourDTO tourDTO) {
        Tour tour = new Tour();
        tour.setTourName(tourDTO.getTourName());
        tour.setBandId(tourDTO.getBandId());
        return tour;
    }

}
