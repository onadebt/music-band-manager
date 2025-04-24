package cz.muni.fi.musiccatalogservice.service;

import cz.muni.fi.musiccatalogservice.exception.ResourceNotFoundException;
import cz.muni.fi.musiccatalogservice.model.Song;
import cz.muni.fi.musiccatalogservice.repository.SongRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class SongServiceTest {

    @Mock
    private SongRepository songRepository;

    @InjectMocks
    private SongService songService;

    private Song testSong;

    @BeforeEach
    void setUp() {
        testSong = new Song();
        testSong.setId(1L);
        testSong.setName("Test Song");
        testSong.setDuration(180);
    }

    @Test
    void deleteSong_WhenSongExists_ShouldDeleteSuccessfully() {
        // Arrange
        when(songRepository.findById(1L)).thenReturn(Optional.of(testSong));

        // Act
        songService.deleteSong(1L);

        // Assert
        verify(songRepository, times(1)).deleteById(1L);
    }

    @Test
    void deleteSong_WhenSongDoesNotExist_ShouldThrowResourceNotFoundException() {
        // Arrange
        when(songRepository.findById(1L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> {
            songService.deleteSong(1L);
        });

        verify(songRepository, never()).deleteById(anyLong());
    }
}