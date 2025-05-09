package cz.muni.fi.userservice.repository;

import cz.muni.fi.userservice.TestDataFactory;
import cz.muni.fi.userservice.model.Artist;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
public class ArtistRepositoryTest {

    @Autowired
    private ArtistRepository artistRepository;

    @BeforeEach
    void setUp() {
        artistRepository.deleteAll();
    }

    @Test
    void findByUsername_existingUsername_returnsArtist() {
        // Arrange
        Artist artist = TestDataFactory.setUpTestArtist1();
        artist.setId(null);
        artistRepository.save(artist);

        // Act
        Optional<Artist> foundArtist = artistRepository.findByUsername(artist.getUsername());

        // Assert
        assertTrue(foundArtist.isPresent());
        assertEquals(artist.getUsername(), foundArtist.get().getUsername());
    }

    @Test
    void findByUsername_nonExistingUsername_returnsEmpty() {
        // Arrange
        Optional<Artist> foundArtist = artistRepository.findByUsername("doesNotExist");

        // Act & Assert
        assertFalse(foundArtist.isPresent());
    }

    @Test
    void findByBandIds_existingBandIds_returnsArtists() {
        // Arrange
        Artist artist1 = TestDataFactory.setUpTestArtist1();
        artist1.setId(null);
        artist1.setBandIds(Set.of(1L, 2L));
        artistRepository.save(artist1);

        Artist artist2 = TestDataFactory.setUpTestArtist2();
        artist2.setId(null);
        artist2.setBandIds(Set.of(2L, 3L));
        artistRepository.save(artist2);

        // Act
        List<Artist> found = artistRepository.findByBandIds(Set.of(1L, 2L));

        // Assert
        assertEquals(2, found.size());
        assertTrue(found.contains(artist1));
        assertTrue(found.contains(artist2));
    }

    @Test
    void findByBandIds_nonExistingBandId_returnsEmpty() {
        // Arrange
        List<Artist> found = artistRepository.findByBandIds(Set.of(999L));

        // Act & Assert
        assertTrue(found.isEmpty());
    }
}
