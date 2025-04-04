package cz.muni.fi.userservice.service;

import cz.muni.fi.userservice.TestDataFactory;
import cz.muni.fi.userservice.model.Artist;
import cz.muni.fi.userservice.repository.ArtistRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;

/**
 * @author Tomáš MAREK
 */

@ExtendWith(MockitoExtension.class)
public class ArtistServiceTest {
    @Mock
    ArtistRepository artistRepository;

    @InjectMocks
    ArtistService artistService;

    @Test
    void save_artistSaved_returnsSavedArtist() {
        // Arrange
        Mockito.when(artistRepository.save(TestDataFactory.TEST_ARTIST_1)).thenReturn(TestDataFactory.TEST_ARTIST_1);

        // Act
        Artist saved = artistService.save(TestDataFactory.TEST_ARTIST_1);

        // Assert
        assertEquals(TestDataFactory.TEST_ARTIST_1, saved);
    }

    @Test
    void findById_artistFound_returnsArtist() {
        // Arrange
        Mockito.when(artistRepository.findById(TestDataFactory.TEST_ARTIST_1.getId())).thenReturn(Optional.of(TestDataFactory.TEST_ARTIST_1));

        // Act
        Artist found = artistService.findById(TestDataFactory.TEST_ARTIST_1.getId());

        // Assert
        assertEquals(TestDataFactory.TEST_ARTIST_1, found);
    }

    @Test
    void findById_artistNotFound_returnsNull() {
        // Arrange
        Long invalidId = 42L;
        Mockito.when(artistRepository.findById(invalidId)).thenReturn(Optional.empty());

        // Act
        Artist found = artistService.findById(invalidId);

        // Assert
        assertNull(found);
    }



    @Test
    void findByUsername_artistFound_returnsArtist() {
        // Arrange
        Mockito.when(artistRepository.findByUsername(TestDataFactory.TEST_ARTIST_1.getUsername())).thenReturn(Optional.of(TestDataFactory.TEST_ARTIST_1));

        // Act
        Artist found = artistService.findByUsername(TestDataFactory.TEST_ARTIST_1.getUsername());

        // Assert
        assertEquals(TestDataFactory.TEST_ARTIST_1, found);
    }

    @Test
    void findByUsername_artistNotFound_returnsNull() {
        // Arrange
        String invalidUsername = "Invalid username";
        Mockito.when(artistRepository.findByUsername(invalidUsername)).thenReturn(Optional.empty());

        // Act
        Artist found = artistService.findByUsername(invalidUsername);

        // Assert
        assertNull(found);
    }

    @Test
    void findAll_noArtistsStored_returnsEmptyList() {
        // Arrange
        Mockito.when(artistRepository.findAll()).thenReturn(List.of());

        // Act
        List<Artist> found = artistService.findAll();

        // Assert
        assertEquals(0, found.size());
    }

    @Test
    void findAll_twoArtistsStored_returnsList() {
        // Arrange
        Mockito.when(artistRepository.findAll()).thenReturn(List.of(TestDataFactory.TEST_ARTIST_1, TestDataFactory.TEST_ARTIST_2));

        // Act
        List<Artist> found = artistService.findAll();

        // Assert
        assertEquals(2, found.size());
        assertTrue(found.contains(TestDataFactory.TEST_ARTIST_1));
        assertTrue(found.contains(TestDataFactory.TEST_ARTIST_2));
    }

    @Test
    void deleteById_artistPresent_noReturn() {
        // Act
        artistService.deleteById(TestDataFactory.TEST_ARTIST_1.getId());

        // Assert
        Mockito.verify(artistRepository, Mockito.times(1)).deleteById(TestDataFactory.TEST_ARTIST_1.getId());
    }

    @Test
    void findByBandIds_noArtisInBands_returnsEmptyList() {
        // Arrange
        Set<Long> emptyBandIds = Set.of(5L);
        Mockito.when(artistRepository.findByBandIds(emptyBandIds)).thenReturn(List.of());

        // Act
        List<Artist> found = artistService.findByBandIds(emptyBandIds);

        // Assert
        assertEquals(0, found.size());
    }

    @Test
    void findByBandIds_artistsInBands_returnsList() {
        // Arrange
        Set<Long> bands = Set.of(2L, 3L);
        Mockito.when(artistRepository.findByBandIds(bands)).thenReturn(List.of(TestDataFactory.TEST_ARTIST_1, TestDataFactory.TEST_ARTIST_2));

        // Act
        List<Artist> found = artistService.findByBandIds(bands);

        // Assert
        assertEquals(2, found.size());
        assertTrue(found.contains(TestDataFactory.TEST_ARTIST_1));
        assertTrue(found.contains(TestDataFactory.TEST_ARTIST_2));
    }

    @Test
    void findByBandIds_noArtistsInBands_returnsEmptyList() {
        // Arrange
        Set<Long> idsOfEmptyBands = Set.of();
        Mockito.when(artistRepository.findByBandIds(idsOfEmptyBands)).thenReturn(List.of());

        // Act
        List<Artist> found = artistService.findByBandIds(idsOfEmptyBands);

        // Assert
        assertEquals(0, found.size());
    }

    @Test
    void findByBandIds_emptyBands_returnsEmptyList() {
        // Arrange
        Set<Long> emptyBandIds = Set.of();
        Mockito.when(artistRepository.findByBandIds(emptyBandIds)).thenReturn(List.of());

        // Act
        List<Artist> found = artistService.findByBandIds(emptyBandIds);

        // Assert
        assertEquals(0, found.size());
    }

    @Test
    void updateArtistByBandsIds_newBandAdded_returnsUpdatedArtist() {
        // Arrange
        Mockito.when(artistRepository.findById(TestDataFactory.TEST_ARTIST_1.getId())).thenReturn(Optional.of(TestDataFactory.TEST_ARTIST_1));
        Mockito.when(artistRepository.save(TestDataFactory.TEST_ARTIST_1)).thenReturn(TestDataFactory.TEST_ARTIST_1);
        Set<Long> newBandIds = Set.of(1L, 2L, 3L, 4L);

        // Act
        Artist updated = artistService.updateArtistByBandIds(TestDataFactory.TEST_ARTIST_1.getId(), newBandIds);

        // Assert
        assertEquals(TestDataFactory.TEST_ARTIST_1, updated);
        assertEquals(newBandIds, updated.getBandIds());
    }

    @Test
    void updateArtistByBandsIds_allBandsRemoved_returnsUpdatedArtist() {
        // Arrange
        Mockito.when(artistRepository.findById(TestDataFactory.TEST_ARTIST_1.getId())).thenReturn(Optional.of(TestDataFactory.TEST_ARTIST_1));
        Mockito.when(artistRepository.save(TestDataFactory.TEST_ARTIST_1)).thenReturn(TestDataFactory.TEST_ARTIST_1);
        Set<Long> emptyBands = Set.of();

        // Act
        Artist updated = artistService.updateArtistByBandIds(TestDataFactory.TEST_ARTIST_1.getId(), emptyBands);

        // Assert
        assertEquals(TestDataFactory.TEST_ARTIST_1, updated);
        assertEquals(emptyBands, updated.getBandIds());
    }

    @Test
    void updateArtistByBandsIds_invalidArtistId_throwsIllegalArgumentException() {
        // Arrange
        Long invalidId = 42L;
        Mockito.when(artistRepository.findById(invalidId)).thenReturn(Optional.empty());

        // Act / Assert
        assertThrows(IllegalArgumentException.class, () -> artistService.updateArtistByBandIds(invalidId, Set.of(1L, 2L, 3L)));
    }
}
