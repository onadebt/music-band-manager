package cz.muni.fi.musiccatalogservice.service;

import cz.muni.fi.musiccatalogservice.TestDataFactory;
import cz.muni.fi.musiccatalogservice.model.Song;
import cz.muni.fi.musiccatalogservice.repository.SongRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(MockitoExtension.class)
public class SongServiceTest {

    @Mock
    SongRepository songRepository;

    @InjectMocks
    SongService songService;

    @Test
    void getAllSongs_noSongsStored_returnsEmptyList() {
        // Arrange
        Mockito.when(songRepository.findAll()).thenReturn(List.of());

        // Act
        List<Song> found = songService.getAllSongs();

        // Assert
        assertEquals(0, found.size());
    }

    @Test
    void getAllSongs_twoSongsStored_returnsList() {
        // Arrange
        Mockito.when(songRepository.findAll()).thenReturn(List.of(TestDataFactory.TEST_SONG_1, TestDataFactory.TEST_SONG_2));

        // Act
        List<Song> found = songService.getAllSongs();

        // Assert
        assertEquals(2, found.size());
        assertTrue(found.contains(TestDataFactory.TEST_SONG_1));
        assertTrue(found.contains(TestDataFactory.TEST_SONG_2));
    }

    @Test
    void deleteSong_songPresent_noException() {
        // Arrange
        Mockito.when(songRepository.findById(TestDataFactory.TEST_SONG_1.getId()))
                .thenReturn(Optional.of(TestDataFactory.TEST_SONG_1));

        // Act
        songService.deleteSong(TestDataFactory.TEST_SONG_1.getId());

        // Assert
        Mockito.verify(songRepository, Mockito.times(1)).findById(TestDataFactory.TEST_SONG_1.getId());
        Mockito.verify(songRepository, Mockito.times(1)).deleteById(TestDataFactory.TEST_SONG_1.getId());
    }

    @Test
    void getSongsByAlbum_noSongInAlbums_returnsEmptyList() {
        // Arrange
        Long emptySongId = 5L;
        Mockito.when(songRepository.findByAlbumId(emptySongId)).thenReturn(List.of());

        // Act
        List<Song> found = songService.getSongsByAlbum(emptySongId);

        // Assert
        assertEquals(0, found.size());
    }


    @Test
    void getSongsByAlbum_songsInAlbums_returnsList() {
        // Arrange
        Long albumId = 2L;
        Mockito.when(songRepository.findByAlbumId(albumId)).thenReturn(List.of(TestDataFactory.TEST_SONG_1, TestDataFactory.TEST_SONG_2));

        // Act
        List<Song> found = songService.getSongsByAlbum(albumId);

        // Assert
        assertEquals(2, found.size());
        assertTrue(found.contains(TestDataFactory.TEST_SONG_1));
        assertTrue(found.contains(TestDataFactory.TEST_SONG_2));
    }

}