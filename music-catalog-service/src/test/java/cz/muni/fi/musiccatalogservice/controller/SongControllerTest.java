package cz.muni.fi.musiccatalogservice.controller;

import cz.muni.fi.musiccatalogservice.TestDataFactory;
import cz.muni.fi.musiccatalogservice.dto.SongDto;
import cz.muni.fi.musiccatalogservice.facade.SongFacade;
import cz.muni.fi.musiccatalogservice.rest.SongController;
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
public class SongControllerTest {
    @InjectMocks
    private SongController songController;

    @Mock
    private SongFacade songFacade;

    @Test
    void create_validRequest_returnsCreatedSongWithOkStatus() {
        // Arrange
        Mockito.when(songFacade.createSong(TestDataFactory.TEST_SONG_1_DTO)).thenReturn(TestDataFactory.TEST_SONG_1_DTO);
        // Act
        ResponseEntity<SongDto> response = songController.createSong(TestDataFactory.TEST_SONG_1_DTO);

        // Assert
        assertThat(response.getStatusCode().value()).isEqualTo(201);  // todo was 200
        assertThat(response.hasBody()).isTrue();
        assertThat(response.getBody()).isEqualTo(TestDataFactory.TEST_SONG_1_DTO);
        verify(songFacade, times(1)).createSong(any());
    }

    @Test
    void getAllSongs_twoSongsStored_returnsListWithOkStatus() {
        // Arrange
        Mockito.when(songFacade.getAllSongs()).thenReturn(List.of(TestDataFactory.TEST_SONG_1_DTO, TestDataFactory.TEST_SONG_2_DTO));

        // Act
        ResponseEntity<List<SongDto>> response = songController.getAllSongs();

        // Assert
        assertThat(response.getStatusCode().value()).isEqualTo(200);
        assertThat(response.hasBody()).isTrue();
        assertThat(Objects.requireNonNull(response.getBody()).contains(TestDataFactory.TEST_SONG_1_DTO)).isTrue();
        assertThat(Objects.requireNonNull(response.getBody()).contains(TestDataFactory.TEST_SONG_2_DTO)).isTrue();
    }

    @Test
    void getSongById_validRequest_returnsSongWithOkStatus() {
        // Arrange
        Mockito.when(songFacade.getSongById(TestDataFactory.TEST_SONG_1.getId())).thenReturn(TestDataFactory.TEST_SONG_1_DTO);

        // Act
        ResponseEntity<SongDto> response = songController.getSongById(TestDataFactory.TEST_SONG_1.getId());

        // Assert
        assertThat(response.getStatusCode().value()).isEqualTo(200);
        assertThat(response.hasBody()).isTrue();
        assertThat(response.getBody()).isEqualTo(TestDataFactory.TEST_SONG_1_DTO);
    }

    @Test
    void deleteSong_validId_returnsEmptyEntityWithOkStatus() {
        // Act
        ResponseEntity<Void> response = songController.deleteSong(TestDataFactory.TEST_SONG_1.getId());

        // Assert
        assertThat(response.getStatusCode().value()).isEqualTo(204);
        assertThat(response.hasBody()).isFalse();
        verify(songFacade, times(1)).deleteSong(TestDataFactory.TEST_SONG_1.getId());
    }

    @Test
    void getSongsByBandIds_twoSongsMatch_returnsListWithOkStatus() {
        // Arrange
        Long bandId = 2L;
        Mockito.when(songFacade.getSongsByBand(bandId)).thenReturn(List.of(TestDataFactory.TEST_SONG_1_DTO, TestDataFactory.TEST_SONG_2_DTO));

        // Act
        ResponseEntity<List<SongDto>> response = songController.getSongsByBand(bandId);

        // Assert
        assertThat(response.getStatusCode().value()).isEqualTo(200);
        assertThat(response.hasBody()).isTrue();
        assertEquals(2, Objects.requireNonNull(response.getBody()).size());
        assertThat(Objects.requireNonNull(response.getBody()).contains(TestDataFactory.TEST_SONG_1_DTO)).isTrue();
        assertThat(Objects.requireNonNull(response.getBody()).contains(TestDataFactory.TEST_SONG_2_DTO)).isTrue();
    }
}