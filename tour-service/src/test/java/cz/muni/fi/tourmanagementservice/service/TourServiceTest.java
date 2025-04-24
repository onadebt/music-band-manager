package cz.muni.fi.tourmanagementservice.service;

import cz.muni.fi.tourmanagementservice.exception.ResourceNotFoundException;
import cz.muni.fi.tourmanagementservice.model.Tour;
import cz.muni.fi.tourmanagementservice.repository.CityVisitRepository;
import cz.muni.fi.tourmanagementservice.repository.TourRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TourServiceTest {

    @Mock
    private TourRepository tourRepository;

    @Mock
    private CityVisitRepository cityVisitRepository;

    @InjectMocks
    private TourService tourService;

    private Tour testTour;

    @BeforeEach
    void setUp() {
        testTour = new Tour();
        testTour.setId(1L);
        testTour.setTourName("Test Tour");
    }

    @Test
    void deleteTour_WhenTourExists_ShouldDeleteSuccessfully() {
        // Arrange
        when(tourRepository.findById(1L)).thenReturn(Optional.of(testTour));

        // Act
        tourService.deleteTour(1L);

        // Assert
        verify(tourRepository, times(1)).deleteById(1L);
    }

    @Test
    void deleteTour_WhenTourDoesNotExist_ShouldThrowResourceNotFoundException() {
        // Arrange
        when(tourRepository.findById(1L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> {
            tourService.deleteTour(1L);
        });

        verify(tourRepository, never()).deleteById(anyLong());
    }
}