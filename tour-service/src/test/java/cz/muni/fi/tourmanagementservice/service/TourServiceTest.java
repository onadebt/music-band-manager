package cz.muni.fi.tourmanagementservice.service;
import cz.muni.fi.tourmanagementservice.exception.ResourceNotFoundException;
import cz.muni.fi.tourmanagementservice.model.CityVisit;
import cz.muni.fi.tourmanagementservice.model.Tour;
import cz.muni.fi.tourmanagementservice.repository.CityVisitRepository;
import cz.muni.fi.tourmanagementservice.repository.TourRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TourServiceTest {

    @Mock
    private TourRepository tourRepository;

    @Mock
    private CityVisitRepository cityVisitRepository;

    @InjectMocks
    private TourService tourService;

    private Tour tour;
    private CityVisit cityVisit;

    @BeforeEach
    void setUp() {
        tour = new Tour();
        tour.setId(1L);
        tour.setBandId(2L);
        tour.setTourName("World Tour 2025");

        cityVisit = new CityVisit();
        cityVisit.setId(1L);
        cityVisit.setCityName("Prague");
        cityVisit.setDateFrom(LocalDate.of(2025, 5, 15));
        cityVisit.setDateTo(LocalDate.of(2025, 5, 17));

        List<CityVisit> cityVisits = new ArrayList<>();
        cityVisits.add(cityVisit);
        tour.setCityVisits(cityVisits);
    }

    @Test
    void getAllTours_ShouldReturnAllTours() {
        List<Tour> tours = Arrays.asList(tour);
        when(tourRepository.findAll()).thenReturn(tours);

        List<Tour> result = tourService.getAllTours();

        assertEquals(1, result.size());
        assertEquals("World Tour 2025", result.get(0).getTourName());
        verify(tourRepository, times(1)).findAll();
    }

    @Test
    void getTourById_ShouldReturnTour_WhenExists() {
        when(tourRepository.findById(1L)).thenReturn(Optional.of(tour));

        Tour result = tourService.getTourById(1L);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("World Tour 2025", result.getTourName());
        verify(tourRepository, times(1)).findById(1L);
    }

    @Test
    void getTourById_ShouldThrowException_WhenTourDoesNotExist() {
        when(tourRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> tourService.getTourById(99L));
        verify(tourRepository, times(1)).findById(99L);
    }

    @Test
    void getToursByBand_ShouldReturnTours() {
        List<Tour> tours = Arrays.asList(tour);
        when(tourRepository.findByBandId(2L)).thenReturn(tours);

        List<Tour> result = tourService.getToursByBand(2L);

        assertEquals(1, result.size());
        assertEquals(2L, result.get(0).getBandId());
        verify(tourRepository, times(1)).findByBandId(2L);
    }

    @Test
    void createTour_ShouldReturnSavedTour() {
        when(tourRepository.save(any(Tour.class))).thenReturn(tour);

        Tour result = tourService.createTour(tour);

        assertNotNull(result);
        assertEquals("World Tour 2025", result.getTourName());
        verify(tourRepository, times(1)).save(tour);
    }

    @Test
    void updateTour_ShouldUpdateTourFields() {
        Tour updatedTour = new Tour();
        updatedTour.setTourName("Updated Tour Name");
        updatedTour.setBandId(3L);

        List<CityVisit> newCityVisits = new ArrayList<>();
        CityVisit newCityVisit = new CityVisit();
        newCityVisit.setCityName("Berlin");
        newCityVisits.add(newCityVisit);
        updatedTour.setCityVisits(newCityVisits);

        when(tourRepository.findById(1L)).thenReturn(Optional.of(tour));
        when(tourRepository.save(any(Tour.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Tour result = tourService.updateTour(1L, updatedTour);

        assertNotNull(result);
        assertEquals("Updated Tour Name", result.getTourName());
        assertEquals(3L, result.getBandId());
        assertEquals(1, result.getCityVisits().size());
        assertEquals("Berlin", result.getCityVisits().get(0).getCityName());
        verify(tourRepository, times(1)).findById(1L);
        verify(tourRepository, times(1)).save(tour);
    }

    @Test
    void deleteTour_ShouldDeleteTour() {
        when(tourRepository.findById(1L)).thenReturn(Optional.of(tour));
        doNothing().when(tourRepository).deleteById(1L);

        tourService.deleteTour(1L);

        verify(tourRepository, times(1)).findById(1L);
        verify(tourRepository, times(1)).deleteById(1L);
    }

    @Test
    void deleteTour_ShouldThrowException_WhenTourDoesNotExist() {
        when(tourRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> tourService.deleteTour(99L));
        verify(tourRepository, times(1)).findById(99L);
        verify(tourRepository, never()).deleteById(anyLong());
    }

    @Test
    void addCityVisitToTour_ShouldAddCityVisit() {
        CityVisit newCityVisit = new CityVisit();
        newCityVisit.setCityName("Vienna");

        when(tourRepository.findById(1L)).thenReturn(Optional.of(tour));
        when(cityVisitRepository.save(any(CityVisit.class))).thenReturn(newCityVisit);
        when(tourRepository.save(any(Tour.class))).thenReturn(tour);

        tourService.addCityVisitToTour(1L, newCityVisit);

        verify(tourRepository, times(1)).findById(1L);
        verify(cityVisitRepository, times(1)).save(newCityVisit);
        verify(tourRepository, times(1)).save(tour);

        assertTrue(tour.getCityVisits().contains(newCityVisit));
    }

    @Test
    void removeCityVisitFromTour_ShouldRemoveCityVisit() {
        when(tourRepository.findById(1L)).thenReturn(Optional.of(tour));
        when(cityVisitRepository.findById(1L)).thenReturn(Optional.of(cityVisit));

        tourService.removeCityVisitFromTour(1L, 1L);

        verify(tourRepository, times(1)).findById(1L);
        verify(cityVisitRepository, times(1)).findById(1L);
        verify(cityVisitRepository, times(1)).delete(cityVisit);

        assertFalse(tour.getCityVisits().contains(cityVisit));
    }

    @Test
    void removeCityVisitFromTour_ShouldThrowException_WhenTourDoesNotExist() {
        when(tourRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> tourService.removeCityVisitFromTour(99L, 1L));
        verify(tourRepository, times(1)).findById(99L);
        verify(cityVisitRepository, never()).findById(anyLong());
    }

    @Test
    void removeCityVisitFromTour_ShouldThrowException_WhenCityVisitDoesNotExist() {
        when(tourRepository.findById(1L)).thenReturn(Optional.of(tour));
        when(cityVisitRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> tourService.removeCityVisitFromTour(1L, 99L));
        verify(tourRepository, times(1)).findById(1L);
        verify(cityVisitRepository, times(1)).findById(99L);
    }
}