package cz.muni.fi.userservice.rest;

import cz.muni.fi.userservice.TestDataFactory;
import cz.muni.fi.userservice.dto.ArtistDto;
import cz.muni.fi.userservice.dto.ArtistUpdateDto;
import cz.muni.fi.userservice.facade.ArtistFacade;
import cz.muni.fi.userservice.model.Artist;
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
        ArtistDto artistDto = TestDataFactory.setUpTestArtist1Dto();

        Mockito.when(artistFacade.register(artistDto)).thenReturn(artistDto);
        // Act
        ResponseEntity<ArtistDto> response = artistController.register(artistDto);

        // Assert
        assertThat(response.getStatusCode().value()).isEqualTo(200);
        assertThat(response.hasBody()).isTrue();
        assertThat(response.getBody()).isEqualTo(artistDto);
        verify(artistFacade, times(1)).register(any());
    }

    @Test
    void getAllArtists_twoArtistsStored_returnsListWithOkStatus() {
        // Arrange
        ArtistDto artistDto = TestDataFactory.setUpTestArtist1Dto();
        ArtistDto artistDto2 = TestDataFactory.setUpTestArtist2Dto();
        Mockito.when(artistFacade.findAll()).thenReturn(List.of(artistDto, artistDto2));

        // Act
        ResponseEntity<List<ArtistDto>> response = artistController.getAllArtists();

        // Assert
        assertThat(response.getStatusCode().value()).isEqualTo(200);
        assertThat(response.hasBody()).isTrue();
        assertThat(Objects.requireNonNull(response.getBody()).contains(artistDto)).isTrue();
        assertThat(Objects.requireNonNull(response.getBody()).contains(artistDto2)).isTrue();
    }

    @Test
    void getArtistById_validRequest_returnsArtistWithOkStatus() {
        // Arrange
        Artist artist = TestDataFactory.setUpTestArtist1();
        ArtistDto artistDto = TestDataFactory.setUpTestArtist1Dto();

        Mockito.when(artistFacade.findById(artist.getId())).thenReturn(artistDto);

        // Act
        ResponseEntity<ArtistDto> response = artistController.getArtistById(artist.getId());

        // Assert
        assertThat(response.getStatusCode().value()).isEqualTo(200);
        assertThat(response.hasBody()).isTrue();
        assertThat(response.getBody()).isEqualTo(artistDto);
    }

    @Test
    void deleteArtist_validId_returnsEmptyEntityWithOkStatus() {
        // Act
        Artist artist = TestDataFactory.setUpTestArtist1();
        ResponseEntity<Void> response = artistController.deleteArtist(artist.getId());

        // Assert
        assertThat(response.getStatusCode().value()).isEqualTo(204);
        assertThat(response.hasBody()).isFalse();
        verify(artistFacade, times(1)).deleteById(artist.getId());
    }

    @Test
    void getArtistByUsername_validUsername_returnsArtistWithOkStatus() {
        // Arrange
        ArtistDto artistDto = TestDataFactory.setUpTestArtist1Dto();
        Mockito.when(artistFacade.findByUsername(artistDto.getUsername())).thenReturn(artistDto);

        // Act
        ResponseEntity<ArtistDto> response = artistController.getArtistByUsername(artistDto.getUsername());

        // Assert
        assertThat(response.getStatusCode().value()).isEqualTo(200);
        assertThat(response.hasBody()).isTrue();
        assertThat(response.getBody()).isEqualTo(artistDto);
    }

    @Test
    void updateBands_validOperation_returnsUpdatedArtistWithOkStatus() {
        // Arrange
        ArtistDto artistDto = TestDataFactory.setUpTestArtist1Dto();
        Mockito.when(artistFacade.updateBandIds(artistDto.getId(), artistDto.getBandIds()))
                .thenReturn(artistDto);

        // Act
        ResponseEntity<ArtistDto> response = artistController.updateBands(artistDto.getId(), artistDto.getBandIds());

        // Assert
        assertThat(response.getStatusCode().value()).isEqualTo(200);
        assertThat(response.hasBody()).isTrue();
        assertThat(response.getBody()).isEqualTo(artistDto);
    }

    @Test
    void getArtistsByBandIds_twoArtistsMatch_returnsListWithOkStatus() {
        // Arrange
        ArtistDto artistDto = TestDataFactory.setUpTestArtist1Dto();
        ArtistDto artistDto2 = TestDataFactory.setUpTestArtist2Dto();
        Set<Long> bandIds = Set.of(2L, 3L);
        Mockito.when(artistFacade.findByBandIds(bandIds)).thenReturn(List.of(artistDto, artistDto2));

        // Act
        ResponseEntity<List<ArtistDto>> response = artistController.getArtistsByBandIds(Set.of(2L, 3L));

        // Assert
        assertThat(response.getStatusCode().value()).isEqualTo(200);
        assertThat(response.hasBody()).isTrue();
        assertEquals(2, Objects.requireNonNull(response.getBody()).size());
        assertThat(Objects.requireNonNull(response.getBody()).contains(artistDto)).isTrue();
        assertThat(Objects.requireNonNull(response.getBody()).contains(artistDto2)).isTrue();
    }

    @Test
    void linkArtistToBand_validOperation_returnsUpdatedArtistWithOkStatus() {
        // Arrange
        ArtistDto artistDto = TestDataFactory.setUpTestArtist1Dto();
        Mockito.when(artistFacade.linkArtistToBand(artistDto.getId(), artistDto.getBandIds().iterator().next()))
                .thenReturn(artistDto);

        // Act
        ResponseEntity<ArtistDto> response = artistController.linkArtistToBand(artistDto.getId(), artistDto.getBandIds().iterator().next());

        // Assert
        assertThat(response.getStatusCode().value()).isEqualTo(200);
        assertThat(response.hasBody()).isTrue();
        assertThat(response.getBody()).isEqualTo(artistDto);
    }

    @Test
    void unlinkArtistFromBand_validOperation_returnsUpdatedArtistWithOkStatus() {
        // Arrange
        ArtistDto artistDto = TestDataFactory.setUpTestArtist1Dto();
        Mockito.when(artistFacade.unlinkArtistFromBand(artistDto.getId(), artistDto.getBandIds().iterator().next()))
                .thenReturn(artistDto);

        // Act
        ResponseEntity<ArtistDto> response = artistController.unlinkArtistFromBand(artistDto.getId(), artistDto.getBandIds().iterator().next());

        // Assert
        assertThat(response.getStatusCode().value()).isEqualTo(200);
        assertThat(response.hasBody()).isTrue();
        assertThat(response.getBody()).isEqualTo(artistDto);
    }
}
