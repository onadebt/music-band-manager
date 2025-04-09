package cz.muni.fi.userservice.rest;

import cz.muni.fi.userservice.TestDataFactory;
import cz.muni.fi.userservice.dto.ArtistDto;
import cz.muni.fi.userservice.facade.ArtistFacade;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;
import java.util.Objects;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

/**
 * @author Tomáš MAREK
 */
@ExtendWith(SpringExtension.class)
public class ArtistControllerTest {
    @InjectMocks
    private ArtistController artistController;

    @Mock
    private ArtistFacade artistFacade;

    @Test
    void register_validRequest_returnsCreatedArtisWithOkStatus() {
        // Arrange
        Mockito.when(artistFacade.register(TestDataFactory.TEST_ARTIST_1_DTO)).thenReturn(TestDataFactory.TEST_ARTIST_1_DTO);
        // Act
        ResponseEntity<ArtistDto> response = artistController.register(TestDataFactory.TEST_ARTIST_1_DTO);

        // Assert
        assertThat(response.getStatusCode().value()).isEqualTo(200);
        assertThat(response.hasBody()).isTrue();
        assertThat(response.getBody()).isEqualTo(TestDataFactory.TEST_ARTIST_1_DTO);
        verify(artistFacade, times(1)).register(any());
    }

    @Test
    void getAllArtists_twoArtistsStored_returnsListWithOkStatus() {
        // Arrange
        Mockito.when(artistFacade.findAll()).thenReturn(List.of(TestDataFactory.TEST_ARTIST_1_DTO, TestDataFactory.TEST_ARTIST_2_DTO));

        // Act
        ResponseEntity<List<ArtistDto>> response = artistController.getAllArtists();

        // Assert
        assertThat(response.getStatusCode().value()).isEqualTo(200);
        assertThat(response.hasBody()).isTrue();
        assertThat(Objects.requireNonNull(response.getBody()).contains(TestDataFactory.TEST_ARTIST_1_DTO)).isTrue();
        assertThat(Objects.requireNonNull(response.getBody()).contains(TestDataFactory.TEST_ARTIST_2_DTO)).isTrue();
    }

    @Test
    void getArtistById_validRequest_returnsArtistWithOkStatus() {
        // Arrange
        Mockito.when(artistFacade.findById(TestDataFactory.TEST_ARTIST_1.getId())).thenReturn(TestDataFactory.TEST_ARTIST_1_DTO);

        // Act
        ResponseEntity<ArtistDto> response = artistController.getArtistById(TestDataFactory.TEST_ARTIST_1.getId());

        // Assert
        assertThat(response.getStatusCode().value()).isEqualTo(200);
        assertThat(response.hasBody()).isTrue();
        assertThat(response.getBody()).isEqualTo(TestDataFactory.TEST_ARTIST_1_DTO);
    }

    @Test
    void deleteArtist_validId_returnsEmptyEntityWithOkStatus() {
        // Act
        ResponseEntity<Void> response = artistController.deleteArtist(TestDataFactory.TEST_ARTIST_1.getId());

        // Assert
        assertThat(response.getStatusCode().value()).isEqualTo(204);
        assertThat(response.hasBody()).isFalse();
        verify(artistFacade, times(1)).deleteById(TestDataFactory.TEST_ARTIST_1.getId());
    }

    @Test
    void getArtistByUsername_validUsername_returnsArtistWithOkStatus() {
        // Arrange
        Mockito.when(artistFacade.findByUsername(TestDataFactory.TEST_ARTIST_1_DTO.getUsername())).thenReturn(TestDataFactory.TEST_ARTIST_1_DTO);

        // Act
        ResponseEntity<ArtistDto> response = artistController.getArtistByUsername(TestDataFactory.TEST_ARTIST_1_DTO.getUsername());

        // Assert
        assertThat(response.getStatusCode().value()).isEqualTo(200);
        assertThat(response.hasBody()).isTrue();
        assertThat(response.getBody()).isEqualTo(TestDataFactory.TEST_ARTIST_1_DTO);
    }

    @Test
    void updateBands_validOperation_returnsUpdatedArtistWithOkStatus() {
        // Arrange
        Mockito.when(artistFacade.updateBandIds(TestDataFactory.TEST_ARTIST_1_DTO.getId(), TestDataFactory.TEST_ARTIST_1_DTO.getBandIds()))
                .thenReturn(TestDataFactory.TEST_ARTIST_1_DTO);

        // Act
        ResponseEntity<ArtistDto> response = artistController.updateBands(TestDataFactory.TEST_ARTIST_1_DTO.getId(), TestDataFactory.TEST_ARTIST_1_DTO.getBandIds());

        // Assert
        assertThat(response.getStatusCode().value()).isEqualTo(200);
        assertThat(response.hasBody()).isTrue();
        assertThat(response.getBody()).isEqualTo(TestDataFactory.TEST_ARTIST_1_DTO);
    }

    @Test
    void getArtistsByBandIds_twoArtistsMatch_returnsListWithOkStatus() {
        // Arrange
        Set<Long> bandIds = Set.of(2L, 3L);
        Mockito.when(artistFacade.findByBandIds(bandIds)).thenReturn(List.of(TestDataFactory.TEST_ARTIST_1_DTO, TestDataFactory.TEST_ARTIST_2_DTO));

        // Act
        ResponseEntity<List<ArtistDto>> response = artistController.getArtistsByBandIds(Set.of(2L, 3L));

        // Assert
        assertThat(response.getStatusCode().value()).isEqualTo(200);
        assertThat(response.hasBody()).isTrue();
        assertEquals(2, Objects.requireNonNull(response.getBody()).size());
        assertThat(Objects.requireNonNull(response.getBody()).contains(TestDataFactory.TEST_ARTIST_1_DTO)).isTrue();
        assertThat(Objects.requireNonNull(response.getBody()).contains(TestDataFactory.TEST_ARTIST_2_DTO)).isTrue();
    }
}
