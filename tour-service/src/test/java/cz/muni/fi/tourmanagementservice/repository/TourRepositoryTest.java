package cz.muni.fi.tourmanagementservice.repository;

import cz.muni.fi.tourmanagementservice.TestDataFactory;
import cz.muni.fi.tourmanagementservice.model.Tour;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
public class TourRepositoryTest {
    @Autowired
    private TourRepository tourRepository;

    @BeforeEach
    public void setUp() {
        tourRepository.deleteAll();
    }

    @Test
    void findAllByBandId_validBandId_returnsTours() {
        // Given
        Tour tour = TestDataFactory.setUpTour1();
        tour.setId(null);
        tourRepository.save(tour);

        // When
        List<Tour> result = tourRepository.findAllByBandId(tour.getBandId());

        // Then
        assertEquals(1, result.size());
    }

    @Test
    void findAllByBandId_invalidBandId_returnsEmptyList() {
        // Given
        Tour tour = TestDataFactory.setUpTour1();
        tour.setId(null);
        tourRepository.save(tour);
        Long invalidBandId = -999L;

        // When
        List<Tour> result = tourRepository.findAllByBandId(invalidBandId);

        // Then
        assertEquals(0, result.size());
    }
}
