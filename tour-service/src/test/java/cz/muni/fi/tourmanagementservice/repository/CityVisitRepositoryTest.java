package cz.muni.fi.tourmanagementservice.repository;

import cz.muni.fi.tourmanagementservice.TestDataFactory;
import cz.muni.fi.tourmanagementservice.model.CityVisit;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
public class CityVisitRepositoryTest {

    @Autowired
    private CityVisitRepository cityVisitRepository;

    @BeforeEach
    public void setUp() {
        cityVisitRepository.deleteAll();
    }

    @Test
    public void findAllByCityName_validCityName_returnsCityVisits() {
        // Given
        CityVisit cityVisit = TestDataFactory.setUpCityVisit1();
        cityVisit.setId(null);
        cityVisitRepository.save(cityVisit);

        // When
        List<CityVisit> result = cityVisitRepository.findAllByCityName(cityVisit.getCityName());

        // Then
        assertEquals(1, result.size());
    }

    @Test
    public void findAllByCityName_invalidCityName_returnsEmptyList() {
        // Given
        CityVisit cityVisit = TestDataFactory.setUpCityVisit1();
        cityVisit.setId(null);
        cityVisitRepository.save(cityVisit);
        String invalidCityName = "Invalid City";

        // When
        List<CityVisit> result = cityVisitRepository.findAllByCityName(invalidCityName);

        // Then
        assertTrue(result.isEmpty());
    }
}
