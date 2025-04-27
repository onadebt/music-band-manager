package cz.muni.fi.musiccatalogservice.facade;

import cz.muni.fi.musiccatalogservice.TestDataFactory;
import cz.muni.fi.musiccatalogservice.dto.SongDTO;
import cz.muni.fi.musiccatalogservice.mapper.SongMapper;
import cz.muni.fi.musiccatalogservice.service.SongService;
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
public class SongFacadeTest {
    @InjectMocks
    private SongFacade songFacade;

    @Mock
    private SongService songService;

    @Mock
    private AlbumService albumService;

    @Mock
    private SongMapper songMapper;

    @Test
    void createSong_nullSongDto_throwsIllegalArgumentException() {
        // Act / Assert
        assertThrows(IllegalArgumentException.class, () -> songFacade.createSong(null));
        verify(songService, Mockito.times(0)).createSong(any());
    }

    @Test
    void createSong_validSongDto_returnsSavedSong() {
        // Arrange
        Mockito.when(songService.createSong(TestDataFactory.TEST_SONG_1)).thenReturn(TestDataFactory.TEST_SONG_1);
        Mockito.when(songMapper.toEntity(TestDataFactory.TEST_SONG_1_DTO)).thenReturn(TestDataFactory.TEST_SONG_1);
        Mockito.when(songMapper.toDTO(TestDataFactory.TEST_SONG_1)).thenReturn(TestDataFactory.TEST_SONG_1_DTO);

        // Act
        SongDTO created = songFacade.createSong(TestDataFactory.TEST_SONG_1_DTO);

        // Assert
        verify(songService, times(1)).createSong(TestDataFactory.TEST_SONG_1);
        assertEquals(TestDataFactory.TEST_SONG_1_DTO, created);
    }

    @Test
    void findById_inputNull_throwsIllegalArgumentException() {
        // Act / Assert
        assertThrows(IllegalArgumentException.class, () -> songFacade.getSongById(null));
        verify(songService, Mockito.times(0)).getSongById(any());
    }

    @Test
    void findById_invalidId_throwsIllegalArgumentException() {
        // Arrange
        Long invalidId = -1L;
        Mockito.when(songService.getSongById(invalidId)).thenReturn(null);

        // Act / Assert
        assertThrows(IllegalArgumentException.class, () -> songFacade.getSongById(invalidId));
    }

    @Test
    void findById_validId_returnsFoundSong() {
        // Arrange
        Mockito.when(songService.getSongById(TestDataFactory.TEST_SONG_1.getId())).thenReturn(TestDataFactory.TEST_SONG_1);
        Mockito.when(songMapper.toDTO(TestDataFactory.TEST_SONG_1)).thenReturn(TestDataFactory.TEST_SONG_1_DTO);

        // Act
        SongDTO found = songFacade.getSongById(TestDataFactory.TEST_SONG_1.getId());

        // Assert
        assertEquals(TestDataFactory.TEST_SONG_1_DTO, found);
        verify(songService, times(1)).getSongById(TestDataFactory.TEST_SONG_1.getId());
    }

    @Test
    void findByBand_nullBandId_throwsIllegalArgumentException() {
        // Act / Assert
        assertThrows(IllegalArgumentException.class, () -> songFacade.getSongsByBand(null));
        verify(songService, times(0)).getSongsByBand(any());
    }

    @Test
    void findByUsername_invalidBandId_throwsIllegalArgumentException() {
        // Arrange
        Long invalidBandId = -1L;
        Mockito.when(songService.getSongsByBand(invalidBandId)).thenReturn(null);

        // Act / Assert
        assertThrows(IllegalArgumentException.class, () -> songFacade.getSongsByBand(invalidBandId));
    }

    @Test
    void findAll_noSongStored_returnsEmptyList() {
        // Arrange
        Mockito.when(songService.getAllSongs()).thenReturn(List.of());

        // Act
        List<SongDTO> found = songFacade.getAllSongs();

        // Assert
        assertEquals(0, found.size());
        verify(songService, times(1)).getAllSongs();
    }

    @Test
    void findAll_twoSongsStored_returnsList() {
        // Arrange
        Mockito.when(songService.getAllSongs()).thenReturn(List.of(TestDataFactory.TEST_SONG_1, TestDataFactory.TEST_SONG_2));
        Mockito.when(songMapper.toDTO(TestDataFactory.TEST_SONG_1)).thenReturn(TestDataFactory.TEST_SONG_1_DTO);
        Mockito.when(songMapper.toDTO(TestDataFactory.TEST_SONG_2)).thenReturn(TestDataFactory.TEST_SONG_2_DTO);

        // Act
        List<SongDTO> found = songFacade.getAllSongs();

        // Assert
        assertEquals(2, found.size());
        verify(songService, times(1)).getAllSongs();
        assertTrue(found.contains(TestDataFactory.TEST_SONG_1_DTO));
        assertTrue(found.contains(TestDataFactory.TEST_SONG_2_DTO));
    }

    @Test
    void getSongsByBand_nullArgument_throwsIllegalArgumentException() {
        // Act / Assert
        assertThrows(IllegalArgumentException.class, () -> songFacade.getSongsByBand(null));
        verify(songService, times(0)).getSongsByBand(any());
    }

    @Test
    void getSongsByBand_twoSongsFound_returnsList() {
        // Arrange
        Long bandId = 1L;
        Mockito.when(songService.getSongsByBand(bandId)).thenReturn(List.of(TestDataFactory.TEST_SONG_1, TestDataFactory.TEST_SONG_2));
        Mockito.when(songMapper.toDTO(TestDataFactory.TEST_SONG_1)).thenReturn(TestDataFactory.TEST_SONG_1_DTO);
        Mockito.when(songMapper.toDTO(TestDataFactory.TEST_SONG_2)).thenReturn(TestDataFactory.TEST_SONG_2_DTO);

        // Act
        List<SongDTO> found = songFacade.getSongsByBand(bandId);

        // Assert
        verify(songService, times(1)).getSongsByBand(bandId);
        assertTrue(found.contains(TestDataFactory.TEST_SONG_1_DTO));
        assertTrue(found.contains(TestDataFactory.TEST_SONG_2_DTO));
    }

    @Test
    void deleteSong_nullId_throwsIllegalArgumentException() {
        // Act / Assert
        assertThrows(IllegalArgumentException.class, () -> songFacade.deleteSong(null));
        verify(songService, times(0)).deleteSong(null);
    }

    @Test
    void deleteSong_invalidId_throwsIllegalArgumentException() {
        // Arrange
        Long invalidId = -1L;
        Mockito.when(songService.getSongById(invalidId)).thenReturn(null);

        // Act / Assert
        assertThrows(IllegalArgumentException.class, () -> songFacade.deleteSong(invalidId));
        verify(songService, times(1)).getSongById(invalidId);
        verify(songService, times(0)).deleteSong(invalidId);
    }

    @Test
    void deleteSong_validId_callsSongServiceDelete() {
        // Arrange
        Long validId = TestDataFactory.TEST_SONG_1.getId();
        Mockito.when(songService.getSongById(validId)).thenReturn(TestDataFactory.TEST_SONG_1);

        // Act
        songFacade.deleteSong(validId);

        // Assert
        verify(songService, times(1)).getSongById(validId);
        verify(songService, times(1)).deleteSong(validId);
    }

    @Test
    void updateSong_nullBandIds_throwsIllegalArgumentException() {
        // Arrange
        Long validId = 1L;

        // Act / Assert
        assertThrows(IllegalArgumentException.class, () -> songFacade.updateSong(validId, null));
        verify(songService, times(0)).updateSong(any(), any());
    }

    @Test
    void updateSong_nullArtisId_throwsIllegalArgumentException() {
        // Arrange
        SongDTO validSong = TestDataFactory.TEST_SONG_1_DTO;

        // Act / Assert
        assertThrows(IllegalArgumentException.class, () -> songFacade.updateSong(null, validSong));
        verify(songService, times(0)).updateSong(any(), any());
    }
}