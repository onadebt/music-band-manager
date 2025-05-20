package cz.muni.fi.musiccatalogservice.facade;

import cz.muni.fi.musiccatalogservice.TestDataFactory;
import cz.muni.fi.musiccatalogservice.dto.AlbumDto;
import cz.muni.fi.musiccatalogservice.mapper.AlbumMapper;
import cz.muni.fi.musiccatalogservice.service.AlbumService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;


@ExtendWith(MockitoExtension.class)
public class AlbumFacadeTest {
    @InjectMocks
    private AlbumFacade albumFacade;

    @Mock
    private AlbumService albumService;

    @Mock
    private AlbumMapper albumMapper;

    @Mock
    private SongFacade songFacade;

    @Test
    void createAlbum_nullAlbumDto_throwsIllegalArgumentException() {
        // Act / Assert
        assertThrows(IllegalArgumentException.class, () -> albumFacade.createAlbum(null));
        verify(albumService, Mockito.times(0)).createAlbum(any());
    }

    @Test
    void createAlbum_validAlbumDto_returnsSavedAlbum() {
        // Arrange
        Mockito.when(albumService.createAlbum(TestDataFactory.TEST_ALBUM_1)).thenReturn(TestDataFactory.TEST_ALBUM_1);
        Mockito.when(albumMapper.toEntity(TestDataFactory.TEST_ALBUM_1_DTO)).thenReturn(TestDataFactory.TEST_ALBUM_1);
        Mockito.when(albumMapper.toDto(TestDataFactory.TEST_ALBUM_1)).thenReturn(TestDataFactory.TEST_ALBUM_1_DTO);
        Mockito.when(songFacade.getSongsByAlbum(TestDataFactory.TEST_ALBUM_1.getId())).thenReturn(List.of());

        // Act
        AlbumDto created = albumFacade.createAlbum(TestDataFactory.TEST_ALBUM_1_DTO);

        // Assert
        verify(albumService, times(1)).createAlbum(TestDataFactory.TEST_ALBUM_1);
        assertEquals(TestDataFactory.TEST_ALBUM_1_DTO, created);
    }

    @Test
    void findById_inputNull_throwsIllegalArgumentException() {
        // Act / Assert
        assertThrows(IllegalArgumentException.class, () -> albumFacade.getAlbumById(null));
        verify(albumService, Mockito.times(0)).getAlbumById(any());
    }

    @Test
    void findById_invalidId_throwsIllegalArgumentException() {
        // Arrange
        Long invalidId = -1L;
        Mockito.when(albumService.getAlbumById(invalidId))
                .thenThrow(new IllegalArgumentException("Invalid album ID: " + invalidId));

        // Act / Assert
        assertThrows(IllegalArgumentException.class, () -> albumFacade.getAlbumById(invalidId));
        verify(albumService, times(1)).getAlbumById(invalidId);
    }

    @Test
    void findById_validId_returnsFoundAlbum() {
        // Arrange
        Mockito.when(albumService.getAlbumById(TestDataFactory.TEST_ALBUM_1.getId())).thenReturn(TestDataFactory.TEST_ALBUM_1);
        Mockito.when(albumMapper.toDto(TestDataFactory.TEST_ALBUM_1)).thenReturn(TestDataFactory.TEST_ALBUM_1_DTO);
        Mockito.when(songFacade.getSongsByAlbum(TestDataFactory.TEST_ALBUM_1.getId())).thenReturn(List.of());

        // Act
        AlbumDto found = albumFacade.getAlbumById(TestDataFactory.TEST_ALBUM_1.getId());

        // Assert
        assertEquals(TestDataFactory.TEST_ALBUM_1_DTO, found);
        verify(albumService, times(1)).getAlbumById(TestDataFactory.TEST_ALBUM_1.getId());
    }

    @Test
    void findByBand_nullBandId_throwsIllegalArgumentException() {
        // Act / Assert
        assertThrows(IllegalArgumentException.class, () -> albumFacade.getAlbumsByBand(null));
        verify(albumService, times(0)).getAlbumsByBand(any());
    }

    @Test
    void findByUsername_invalidBandId_throwsIllegalArgumentException() {
        // Arrange
        Long invalidBandId = -1L;
        Mockito.when(albumService.getAlbumsByBand(invalidBandId))
                .thenThrow(new IllegalArgumentException("Invalid band ID: " + invalidBandId));

        // Act / Assert
        assertThrows(IllegalArgumentException.class, () -> albumFacade.getAlbumsByBand(invalidBandId));
        verify(albumService, times(1)).getAlbumsByBand(invalidBandId);
    }

    @Test
    void findAll_noAlbumStored_returnsEmptyList() {
        // Arrange
        Mockito.when(albumService.getAllAlbums()).thenReturn(List.of());

        // Act
        List<AlbumDto> found = albumFacade.getAllAlbums();

        // Assert
        assertEquals(0, found.size());
        verify(albumService, times(1)).getAllAlbums();
    }

    @Test
    void findAll_twoAlbumsStored_returnsList() {
        // Arrange
        Mockito.when(albumService.getAllAlbums()).thenReturn(List.of(TestDataFactory.TEST_ALBUM_1, TestDataFactory.TEST_ALBUM_2));
        Mockito.when(albumMapper.toDto(TestDataFactory.TEST_ALBUM_1)).thenReturn(TestDataFactory.TEST_ALBUM_1_DTO);
        Mockito.when(albumMapper.toDto(TestDataFactory.TEST_ALBUM_2)).thenReturn(TestDataFactory.TEST_ALBUM_2_DTO);
        Mockito.when(songFacade.getSongsByAlbum(TestDataFactory.TEST_ALBUM_1.getId())).thenReturn(List.of());
        Mockito.when(songFacade.getSongsByAlbum(TestDataFactory.TEST_ALBUM_2.getId())).thenReturn(List.of());

        // Act
        List<AlbumDto> found = albumFacade.getAllAlbums();

        // Assert
        assertEquals(2, found.size());
        verify(albumService, times(1)).getAllAlbums();
        assertTrue(found.contains(TestDataFactory.TEST_ALBUM_1_DTO));
        assertTrue(found.contains(TestDataFactory.TEST_ALBUM_2_DTO));
    }

    @Test
    void getAlbumsByBand_nullArgument_throwsIllegalArgumentException() {
        // Act / Assert
        assertThrows(IllegalArgumentException.class, () -> albumFacade.getAlbumsByBand(null));
        verify(albumService, times(0)).getAlbumsByBand(any());
    }

    @Test
    void getAlbumsByBand_twoAlbumsFound_returnsList() {
        // Arrange
        Long bandId = 1L;
        Mockito.when(albumService.getAlbumsByBand(bandId)).thenReturn(List.of(TestDataFactory.TEST_ALBUM_1, TestDataFactory.TEST_ALBUM_2));
        Mockito.when(albumMapper.toDto(TestDataFactory.TEST_ALBUM_1)).thenReturn(TestDataFactory.TEST_ALBUM_1_DTO);
        Mockito.when(albumMapper.toDto(TestDataFactory.TEST_ALBUM_2)).thenReturn(TestDataFactory.TEST_ALBUM_2_DTO);
        Mockito.when(songFacade.getSongsByAlbum(TestDataFactory.TEST_ALBUM_1.getId())).thenReturn(List.of());
        Mockito.when(songFacade.getSongsByAlbum(TestDataFactory.TEST_ALBUM_2.getId())).thenReturn(List.of());

        // Act
        List<AlbumDto> found = albumFacade.getAlbumsByBand(bandId);

        // Assert
        verify(albumService, times(1)).getAlbumsByBand(bandId);
        assertTrue(found.contains(TestDataFactory.TEST_ALBUM_1_DTO));
        assertTrue(found.contains(TestDataFactory.TEST_ALBUM_2_DTO));
    }

    @Test
    void deleteAlbum_nullId_throwsIllegalArgumentException() {
        // Act / Assert
        assertThrows(IllegalArgumentException.class, () -> albumFacade.deleteAlbum(null));
        verify(albumService, times(0)).deleteAlbum(null);
    }

    @Test
    void deleteAlbum_invalidId_throwsIllegalArgumentException() {
        // Arrange
        Long invalidId = -1L;
        Mockito.doThrow(new IllegalArgumentException("Invalid album ID: " + invalidId))
                .when(albumService).deleteAlbum(invalidId);

        // Act / Assert
        assertThrows(IllegalArgumentException.class, () -> albumFacade.deleteAlbum(invalidId));
        verify(albumService, times(1)).deleteAlbum(invalidId);
    }

    @Test
    void deleteAlbum_validId_callsAlbumServiceDelete() {
        // Arrange
        Long validId = TestDataFactory.TEST_ALBUM_1.getId();

        // Act
        albumFacade.deleteAlbum(validId);

        // Assert
        verify(albumService, times(1)).deleteAlbum(validId);
    }

    @Test
    void updateAlbum_nullBandIds_throwsIllegalArgumentException() {
        // Arrange
        Long validId = 1L;

        // Act / Assert
        assertThrows(IllegalArgumentException.class, () -> albumFacade.updateAlbum(validId, null));
        verify(albumService, times(0)).updateAlbum(any(), any());
    }

    @Test
    void updateAlbum_nullArtisId_throwsIllegalArgumentException() {
        // Arrange
        AlbumDto validAlbum = TestDataFactory.TEST_ALBUM_1_DTO;

        // Act / Assert
        assertThrows(IllegalArgumentException.class, () -> albumFacade.updateAlbum(null, validAlbum));
        verify(albumService, times(0)).updateAlbum(any(), any());
    }
}