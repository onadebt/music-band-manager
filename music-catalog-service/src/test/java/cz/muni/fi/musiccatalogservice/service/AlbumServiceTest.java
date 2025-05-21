package cz.muni.fi.musiccatalogservice.service;

import cz.muni.fi.musiccatalogservice.TestDataFactory;
import cz.muni.fi.musiccatalogservice.model.Album;
import cz.muni.fi.musiccatalogservice.repository.AlbumRepository;
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
public class AlbumServiceTest {

    @Mock
    AlbumRepository albumRepository;

    @InjectMocks
    AlbumService albumService;

    @Test
    void getAllAlbums_noAlbumsStored_returnsEmptyList() {
        // Arrange
        Mockito.when(albumRepository.findAll()).thenReturn(List.of());

        // Act
        List<Album> found = albumService.getAllAlbums();

        // Assert
        assertEquals(0, found.size());
    }

    @Test
    void getAllAlbums_twoAlbumsStored_returnsList() {
        // Arrange
        Mockito.when(albumRepository.findAll()).thenReturn(List.of(TestDataFactory.TEST_ALBUM_1, TestDataFactory.TEST_ALBUM_2));

        // Act
        List<Album> found = albumService.getAllAlbums();

        // Assert
        assertEquals(2, found.size());
        assertTrue(found.contains(TestDataFactory.TEST_ALBUM_1));
        assertTrue(found.contains(TestDataFactory.TEST_ALBUM_2));
    }

    @Test
    void deleteAlbum_albumPresent_noException() {
        // Arrange
        Mockito.when(albumRepository.findById(TestDataFactory.TEST_ALBUM_1.getId()))
                .thenReturn(Optional.of(TestDataFactory.TEST_ALBUM_1));

        // Act
        albumService.deleteAlbum(TestDataFactory.TEST_ALBUM_1.getId());

        // Assert
        Mockito.verify(albumRepository, Mockito.times(1)).findById(TestDataFactory.TEST_ALBUM_1.getId());
        Mockito.verify(albumRepository, Mockito.times(1)).deleteById(TestDataFactory.TEST_ALBUM_1.getId());
    }

    @Test
    void getAlbumsByBand_noAlbumInBand_returnsEmptyList() {
        // Arrange
        Long emptyBandId = 5L;
        Mockito.when(albumRepository.findByBandId(emptyBandId)).thenReturn(List.of());

        // Act
        List<Album> found = albumService.getAlbumsByBand(emptyBandId);

        // Assert
        assertEquals(0, found.size());
    }


    @Test
    void getAlbumsByBand_albumsInBands_returnsList() {
        // Arrange
        Long bandId = 2L;
        Mockito.when(albumRepository.findByBandId(bandId)).thenReturn(List.of(TestDataFactory.TEST_ALBUM_1, TestDataFactory.TEST_ALBUM_2));

        // Act
        List<Album> found = albumService.getAlbumsByBand(bandId);

        // Assert
        assertEquals(2, found.size());
        assertTrue(found.contains(TestDataFactory.TEST_ALBUM_1));
        assertTrue(found.contains(TestDataFactory.TEST_ALBUM_2));
    }

}