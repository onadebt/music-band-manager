package cz.muni.fi.userservice.service;

import cz.muni.fi.userservice.TestDataFactory;
import cz.muni.fi.userservice.exception.UserNotFoundException;
import cz.muni.fi.userservice.model.Artist;
import cz.muni.fi.userservice.repository.ArtistRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Tomáš MAREK
 */

@ExtendWith(MockitoExtension.class)
public class ArtistServiceImplTest {
    @Mock
    ArtistRepository artistRepository;

    @InjectMocks
    ArtistServiceImpl artistServiceImpl;

    @Test
    void save_artistSaved_returnsSavedArtist() {
        // Arrange
        Artist artist = TestDataFactory.setUpTestArtist1();
        Mockito.when(artistRepository.save(artist)).thenReturn(artist);

        // Act
        Artist saved = artistServiceImpl.save(artist);

        // Assert
        assertEquals(artist, saved);
    }

    @Test
    void findById_artistFound_returnsArtist() {
        // Arrange
        Artist artist = TestDataFactory.setUpTestArtist1();
        Mockito.when(artistRepository.findById(artist.getId())).thenReturn(Optional.of(artist));

        // Act
        Artist found = artistServiceImpl.findById(artist.getId());

        // Assert
        assertEquals(artist, found);
    }

    @Test
    void findById_artistNotFound_throwsUserNotFoundException() {
        // Arrange
        Long invalidId = 42L;
        Mockito.when(artistRepository.findById(invalidId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(UserNotFoundException.class, () -> artistServiceImpl.findById(invalidId));
    }

    @Test
    void findByUsername_artistFound_returnsArtist() {
        // Arrange
        Artist artist = TestDataFactory.setUpTestArtist1();
        Mockito.when(artistRepository.findByUsername(artist.getUsername())).thenReturn(Optional.of(artist));

        // Act
        Artist found = artistServiceImpl.findByUsername(artist.getUsername());

        // Assert
        assertEquals(artist, found);
    }

    @Test
    void findByUsername_artistNotFound_throwsUserNotFoundException() {
        // Arrange
        String invalidUsername = "Invalid username";
        Mockito.when(artistRepository.findByUsername(invalidUsername)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(UserNotFoundException.class, () -> artistServiceImpl.findByUsername(invalidUsername));
    }

    @Test
    void findAll_noArtistsStored_returnsEmptyList() {
        // Arrange
        Mockito.when(artistRepository.findAll()).thenReturn(List.of());

        // Act
        List<Artist> found = artistServiceImpl.findAll();

        // Assert
        assertEquals(0, found.size());
    }

    @Test
    void findAll_twoArtistsStored_returnsList() {
        // Arrange
        Artist artist1 = TestDataFactory.setUpTestArtist1();
        Artist artist2 = TestDataFactory.setUpTestArtist2();
        Mockito.when(artistRepository.findAll()).thenReturn(List.of(artist1, artist2));

        // Act
        List<Artist> found = artistServiceImpl.findAll();

        // Assert
        assertEquals(2, found.size());
        assertTrue(found.contains(artist1));
        assertTrue(found.contains(artist2));
    }

    @Test
    void deleteById_artistPresent_noException() {
        // Arrange
        Artist artist = TestDataFactory.setUpTestArtist1();
        Mockito.when(artistRepository.findById(artist.getId())).thenReturn(Optional.of(artist));

        // Act
        artistServiceImpl.deleteById(artist.getId());

        // Assert
        Mockito.verify(artistRepository, Mockito.times(1)).findById(artist.getId());
        Mockito.verify(artistRepository, Mockito.times(1)).deleteById(artist.getId());
    }

    @Test
    void findByBandIds_noArtistsInBands_returnsEmptyList() {
        // Arrange
        Set<Long> bandIds = Set.of(5L);
        Mockito.when(artistRepository.findByBandIds(bandIds)).thenReturn(List.of());

        // Act
        List<Artist> found = artistServiceImpl.findByBandIds(bandIds);

        // Assert
        assertEquals(0, found.size());
    }

    @Test
    void findByBandIds_artistsInBands_returnsList() {
        // Arrange
        Artist artist1 = TestDataFactory.setUpTestArtist1();
        Artist artist2 = TestDataFactory.setUpTestArtist2();
        Set<Long> bands = Set.of(2L, 3L);
        Mockito.when(artistRepository.findByBandIds(bands)).thenReturn(List.of(artist1, artist2));

        // Act
        List<Artist> found = artistServiceImpl.findByBandIds(bands);

        // Assert
        assertEquals(2, found.size());
        assertTrue(found.contains(artist1));
        assertTrue(found.contains(artist2));
    }

    @Test
    void findByBandIds_emptyBands_returnsEmptyList() {
        // Arrange
        Set<Long> emptyBandIds = Set.of();
        Mockito.when(artistRepository.findByBandIds(emptyBandIds)).thenReturn(List.of());

        // Act
        List<Artist> found = artistServiceImpl.findByBandIds(emptyBandIds);

        // Assert
        assertEquals(0, found.size());
    }

    @Test
    void updateArtistByBandsIds_newBandAdded_returnsUpdatedArtist() {
        // Arrange
        Artist artist = TestDataFactory.setUpTestArtist1();
        Set<Long> newBandIds = Set.of(1L, 2L, 3L, 4L);
        Mockito.when(artistRepository.findById(artist.getId())).thenReturn(Optional.of(artist));
        Mockito.when(artistRepository.save(artist)).thenReturn(artist);

        // Act
        Artist updated = artistServiceImpl.updateArtistByBandIds(artist.getId(), newBandIds);

        // Assert
        assertEquals(artist, updated);
        assertEquals(newBandIds, updated.getBandIds());
    }

    @Test
    void updateArtistByBandsIds_allBandsRemoved_returnsUpdatedArtist() {
        // Arrange
        Artist artist = TestDataFactory.setUpTestArtist1();
        Set<Long> emptyBands = Set.of();
        Mockito.when(artistRepository.findById(artist.getId())).thenReturn(Optional.of(artist));
        Mockito.when(artistRepository.save(artist)).thenReturn(artist);

        // Act
        Artist updated = artistServiceImpl.updateArtistByBandIds(artist.getId(), emptyBands);

        // Assert
        assertEquals(artist, updated);
        assertEquals(emptyBands, updated.getBandIds());
    }

    @Test
    void updateArtistByBandsIds_invalidArtistId_throwsUserNotFoundException() {
        // Arrange
        Long invalidId = 42L;
        Mockito.when(artistRepository.findById(invalidId)).thenReturn(Optional.empty());

        // Act / Assert
        assertThrows(UserNotFoundException.class, () -> artistServiceImpl.updateArtistByBandIds(invalidId, Set.of(1L, 2L, 3L)));
    }

    @Test
    void linkArtistToBand_artistNotFound_throwsUserNotFoundException() {
        // Arrange
        Long invalidId = 42L;
        Long bandId = 1L;
        Mockito.when(artistRepository.findById(invalidId)).thenReturn(Optional.empty());

        // Act / Assert
        assertThrows(UserNotFoundException.class, () -> artistServiceImpl.linkArtistToBand(invalidId, bandId));
    }

    @Test
    void unlinkArtistFromBand_artistNotFound_throwsUserNotFoundException() {
        // Arrange
        Long invalidId = 42L;
        Long bandId = 1L;
        Mockito.when(artistRepository.findById(invalidId)).thenReturn(Optional.empty());

        // Act / Assert
        assertThrows(UserNotFoundException.class, () -> artistServiceImpl.unlinkArtistFromBand(invalidId, bandId));
    }
}
