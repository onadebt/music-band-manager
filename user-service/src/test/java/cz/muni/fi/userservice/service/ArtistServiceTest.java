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

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;

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
    void findById_artistFound_returnsArtist() {
        // Arrange
        Mockito.when(artistRepository.findById(TestDataFactory.TEST_ARTIST_1_ID)).thenReturn(Optional.of(TestDataFactory.TEST_ARTIST_1));

        // Act
        Artist found = artistService.findById(TestDataFactory.TEST_ARTIST_1_ID);

        // Assert
        assertEquals(TestDataFactory.TEST_ARTIST_1, found);
    }


}
