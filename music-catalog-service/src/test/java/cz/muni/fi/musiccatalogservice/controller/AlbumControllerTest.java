package cz.muni.fi.musiccatalogservice.controller;

import cz.muni.fi.musiccatalogservice.TestDataFactory;
import cz.muni.fi.musiccatalogservice.dto.AlbumDto;
import cz.muni.fi.musiccatalogservice.facade.AlbumFacade;
import cz.muni.fi.musiccatalogservice.rest.AlbumController;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;
import java.util.Objects;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;



@ExtendWith(SpringExtension.class)
public class AlbumControllerTest {
    @InjectMocks
    private AlbumController albumController;

    @Mock
    private AlbumFacade albumFacade;

    @Test
    void create_validRequest_returnsCreatedAlbumWithOkStatus() {
        // Arrange
        Mockito.when(albumFacade.createAlbum(TestDataFactory.TEST_ALBUM_1_DTO)).thenReturn(TestDataFactory.TEST_ALBUM_1_DTO);
        // Act
        ResponseEntity<AlbumDto> response = albumController.createAlbum(TestDataFactory.TEST_ALBUM_1_DTO);

        // Assert
        assertThat(response.getStatusCode().value()).isEqualTo(201);  // todo was 200
        assertThat(response.hasBody()).isTrue();
        assertThat(response.getBody()).isEqualTo(TestDataFactory.TEST_ALBUM_1_DTO);
        verify(albumFacade, times(1)).createAlbum(any());
    }

    @Test
    void getAllAlbums_twoAlbumsStored_returnsListWithOkStatus() {
        // Arrange
        Mockito.when(albumFacade.getAllAlbums()).thenReturn(List.of(TestDataFactory.TEST_ALBUM_1_DTO, TestDataFactory.TEST_ALBUM_2_DTO));

        // Act
        ResponseEntity<List<AlbumDto>> response = albumController.getAllAlbums();

        // Assert
        assertThat(response.getStatusCode().value()).isEqualTo(200);
        assertThat(response.hasBody()).isTrue();
        assertThat(Objects.requireNonNull(response.getBody()).contains(TestDataFactory.TEST_ALBUM_1_DTO)).isTrue();
        assertThat(Objects.requireNonNull(response.getBody()).contains(TestDataFactory.TEST_ALBUM_2_DTO)).isTrue();
    }

    @Test
    void getAlbumById_validRequest_returnsAlbumWithOkStatus() {
        // Arrange
        Mockito.when(albumFacade.getAlbumById(TestDataFactory.TEST_ALBUM_1.getId())).thenReturn(TestDataFactory.TEST_ALBUM_1_DTO);

        // Act
        ResponseEntity<AlbumDto> response = albumController.getAlbumById(TestDataFactory.TEST_ALBUM_1.getId());

        // Assert
        assertThat(response.getStatusCode().value()).isEqualTo(200);
        assertThat(response.hasBody()).isTrue();
        assertThat(response.getBody()).isEqualTo(TestDataFactory.TEST_ALBUM_1_DTO);
    }

    @Test
    void deleteAlbum_validId_returnsEmptyEntityWithOkStatus() {
        // Act
        ResponseEntity<Void> response = albumController.deleteAlbum(TestDataFactory.TEST_ALBUM_1.getId());

        // Assert
        assertThat(response.getStatusCode().value()).isEqualTo(204);
        assertThat(response.hasBody()).isFalse();
        verify(albumFacade, times(1)).deleteAlbum(TestDataFactory.TEST_ALBUM_1.getId());
    }

    @Test
    void updateAlbum_validOperation_returnsUpdatedAlbumWithOkStatus() {
        // Arrange
        Mockito.when(albumFacade.updateAlbum(TestDataFactory.TEST_ALBUM_1_DTO.getId(), TestDataFactory.TEST_ALBUM_1_DTO))
                .thenReturn(TestDataFactory.TEST_ALBUM_1_DTO);

        // Act
        ResponseEntity<AlbumDto> response = albumController.updateAlbum(TestDataFactory.TEST_ALBUM_1_DTO.getId(), TestDataFactory.TEST_ALBUM_1_DTO);

        // Assert
        assertThat(response.getStatusCode().value()).isEqualTo(200);
        assertThat(response.hasBody()).isTrue();
        assertThat(response.getBody()).isEqualTo(TestDataFactory.TEST_ALBUM_1_DTO);
    }

    @Test
    void getAlbumsByBandIds_twoAlbumsMatch_returnsListWithOkStatus() {
        // Arrange
        Long bandId = 2L;
        Mockito.when(albumFacade.getAlbumsByBand(bandId)).thenReturn(List.of(TestDataFactory.TEST_ALBUM_1_DTO, TestDataFactory.TEST_ALBUM_2_DTO));

        // Act
        ResponseEntity<List<AlbumDto>> response = albumController.getAlbumsByBand(bandId);

        // Assert
        assertThat(response.getStatusCode().value()).isEqualTo(200);
        assertThat(response.hasBody()).isTrue();
        assertEquals(2, Objects.requireNonNull(response.getBody()).size());
        assertThat(Objects.requireNonNull(response.getBody()).contains(TestDataFactory.TEST_ALBUM_1_DTO)).isTrue();
        assertThat(Objects.requireNonNull(response.getBody()).contains(TestDataFactory.TEST_ALBUM_2_DTO)).isTrue();
    }
}