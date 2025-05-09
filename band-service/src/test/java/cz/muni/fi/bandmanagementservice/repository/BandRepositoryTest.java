package cz.muni.fi.bandmanagementservice.repository;

import cz.muni.fi.bandmanagementservice.TestDataFactory;
import cz.muni.fi.bandmanagementservice.model.Band;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;

import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
public class BandRepositoryTest {

    @Autowired
    BandRepository bandRepository;

    @BeforeEach
    void setUp() {
        bandRepository.deleteAll();
    }

    @Test
    void findByName_bandFound_returnsBand() {
        // Arrange
        Band band = TestDataFactory.setUpBand1();
        band.setId(null);
        band = bandRepository.save(band);

        // Act
        var foundBand = bandRepository.findByName(band.getName());

        // Assert
        assertTrue(foundBand.isPresent());
    }

    @Test
    void findByName_bandNotFound_returnsEmpty() {
        // Arrange
        Band band = TestDataFactory.setUpBand1();
        band.setId(null);
        bandRepository.save(band);
        String nonExistentBandName = "NonExistentBand";

        // Act
        var foundBand = bandRepository.findByName(nonExistentBandName);

        // Assert
        assertTrue(foundBand.isEmpty());
    }
}
