package cz.muni.fi.userservice.facade;

import cz.muni.fi.userservice.TestDataFactory;
import cz.muni.fi.userservice.dto.ArtistDTO;
import cz.muni.fi.userservice.mappers.ArtistMapper;
import cz.muni.fi.userservice.service.ArtistService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

/**
 * @author Tomáš MAREK
 */
@ExtendWith(MockitoExtension.class)
public class ArtistFacadeTest {
    @InjectMocks
    private ArtistFacade artistFacade;

    @Mock
    private ArtistService artistService;

    @Mock
    private ArtistMapper artistMapper;

    @Test
    void register_nullArtistDto_throwsIllegalArgumentException() {
        // Act / Assert
        assertThrows(IllegalArgumentException.class, () -> artistFacade.register(null));
        verify(artistService, Mockito.times(0)).save(any());
    }

    @Test
    void register_validArtistDto_returnsSavedArtist() {
        // Arrange
        Mockito.when(artistService.save(TestDataFactory.TEST_ARTIST_1)).thenReturn(TestDataFactory.TEST_ARTIST_1);
        Mockito.when(artistMapper.toEntity(TestDataFactory.TEST_ARTIST_1_DTO)).thenReturn(TestDataFactory.TEST_ARTIST_1);
        Mockito.when(artistMapper.toDTO(TestDataFactory.TEST_ARTIST_1)).thenReturn(TestDataFactory.TEST_ARTIST_1_DTO);

        // Act
        ArtistDTO registered = artistFacade.register(TestDataFactory.TEST_ARTIST_1_DTO);

        // Assert
        verify(artistService, times(1)).save(TestDataFactory.TEST_ARTIST_1);
        assertEquals(TestDataFactory.TEST_ARTIST_1_DTO, registered);
    }

    @Test
    void findById_inputNull_throwsIllegalArgumentException() {
        // Act / Assert
        assertThrows(IllegalArgumentException.class, () -> artistFacade.findById(null));
        verify(artistService, Mockito.times(0)).findById(any());
    }

    @Test
    void findById_invalidId_throwsIllegalArgumentException() {
        // Arrange
        Long invalidId = -1L;
        Mockito.when(artistService.findById(invalidId)).thenReturn(null);

        // Act / Assert
        assertThrows(IllegalArgumentException.class, () -> artistFacade.findById(invalidId));
    }

    @Test
    void findById_validId_returnsFoundArtist() {
        // Arrange
        Mockito.when(artistService.findById(TestDataFactory.TEST_ARTIST_1.getId())).thenReturn(TestDataFactory.TEST_ARTIST_1);
        Mockito.when(artistMapper.toDTO(TestDataFactory.TEST_ARTIST_1)).thenReturn(TestDataFactory.TEST_ARTIST_1_DTO);
        // Act
        ArtistDTO found = artistFacade.findById(TestDataFactory.TEST_ARTIST_1.getId());

        // Assert
        assertEquals(TestDataFactory.TEST_ARTIST_1_DTO, found);
        verify(artistService, times(1)).findById(TestDataFactory.TEST_ARTIST_1.getId());
    }

    @Test
    void findByUsername_nullUsername_throwsIllegalArgumentException() {
        // Act / Assert
        assertThrows(IllegalArgumentException.class, () -> artistFacade.findByUsername(null));
        verify(artistService, times(0)).findByUsername(any());
    }

    @Test
    void findByUsername_invalidUsername_throwsIllegalArgumentException() {
        // Arrange
        String invalidUsername = "invalidUsername";
        Mockito.when(artistService.findByUsername(invalidUsername)).thenReturn(null);

        // Act / Assert
        assertThrows(IllegalArgumentException.class, () -> artistFacade.findByUsername(invalidUsername));
    }

    @Test
    void findByUsername_validUsername_returnsFoundArtist() {
        // Arrange
        Mockito.when(artistService.findByUsername(TestDataFactory.TEST_ARTIST_1.getUsername())).thenReturn(TestDataFactory.TEST_ARTIST_1);
        Mockito.when(artistMapper.toDTO(TestDataFactory.TEST_ARTIST_1)).thenReturn(TestDataFactory.TEST_ARTIST_1_DTO);

        // Act
        ArtistDTO found = artistFacade.findByUsername(TestDataFactory.TEST_ARTIST_1.getUsername());

        // Assert
        assertEquals(TestDataFactory.TEST_ARTIST_1_DTO, found);
        verify(artistService, times(1)).findByUsername(TestDataFactory.TEST_ARTIST_1.getUsername());
    }

    @Test
    void findAll_noArtistStored_returnsEmptyList() {
        // Arrange
        Mockito.when(artistService.findAll()).thenReturn(List.of());

        // Act
        List<ArtistDTO> found = artistFacade.findAll();

        // Assert
        assertEquals(0, found.size());
        verify(artistService, times(1)).findAll();
    }

    @Test
    void findAll_twoArtistsStored_returnsList() {
        // Arrange
        Mockito.when(artistService.findAll()).thenReturn(List.of(TestDataFactory.TEST_ARTIST_1, TestDataFactory.TEST_ARTIST_2));
        Mockito.when(artistMapper.toDTO(TestDataFactory.TEST_ARTIST_1)).thenReturn(TestDataFactory.TEST_ARTIST_1_DTO);
        Mockito.when(artistMapper.toDTO(TestDataFactory.TEST_ARTIST_2)).thenReturn(TestDataFactory.TEST_ARTIST_2_DTO);

        // Act
        List<ArtistDTO> found = artistFacade.findAll();

        // Assert
        assertEquals(2, found.size());
        verify(artistService, times(1)).findAll();
        assertTrue(found.contains(TestDataFactory.TEST_ARTIST_1_DTO));
        assertTrue(found.contains(TestDataFactory.TEST_ARTIST_2_DTO));
    }

    @Test
    void findByBandIds_nullArgument_throwsIllegalArgumentException() {
        // Act / Assert
        assertThrows(IllegalArgumentException.class, () -> artistFacade.findByBandIds(null));
        verify(artistService, times(0)).findByBandIds(any());
    }

    @Test
    void findByBandIds_twoArtistsFound_returnsList() {
        // Arrange
        Set<Long> bandIds = Set.of(1L, 2L);
        Mockito.when(artistService.findByBandIds(bandIds)).thenReturn(List.of(TestDataFactory.TEST_ARTIST_1, TestDataFactory.TEST_ARTIST_2));
        Mockito.when(artistMapper.toDTO(TestDataFactory.TEST_ARTIST_1)).thenReturn(TestDataFactory.TEST_ARTIST_1_DTO);
        Mockito.when(artistMapper.toDTO(TestDataFactory.TEST_ARTIST_2)).thenReturn(TestDataFactory.TEST_ARTIST_2_DTO);

        // Act
        List<ArtistDTO> found = artistFacade.findByBandIds(bandIds);

        // Assert
        verify(artistService, times(1)).findByBandIds(bandIds);
        assertTrue(found.contains(TestDataFactory.TEST_ARTIST_1_DTO));
        assertTrue(found.contains(TestDataFactory.TEST_ARTIST_2_DTO));
    }

    @Test
    void deleteById_nullId_throwsIllegalArgumentException() {
        // Act / Assert
        assertThrows(IllegalArgumentException.class, () -> artistFacade.deleteById(null));
        verify(artistService, times(0)).deleteById(null);
    }

    @Test
    void deleteById_invalidId_throwsIllegalArgumentException() {
        // Arrange
        Long invalidId = -1L;
        Mockito.when(artistService.findById(invalidId)).thenReturn(null);

        // Act / Assert
        assertThrows(IllegalArgumentException.class, () -> artistFacade.deleteById(invalidId));
        verify(artistService, times(1)).findById(invalidId);
    }

    @Test
    void deleteById_validId_callsArtistServiceDelete() {
        // Act
        artistService.deleteById(TestDataFactory.TEST_ARTIST_1.getId());

        // Assert
        verify(artistService, times(1)).deleteById(TestDataFactory.TEST_ARTIST_1.getId());
    }

    @Test
    void updateBandIds_nullBandIds_throwsIllegalArgumentException() {
        // Act / Assert
        assertThrows(IllegalArgumentException.class, () -> artistFacade.updateBandIds(1L, null));
        verify(artistService, times(0)).updateArtistByBandIds(any(), any());
    }

    @Test
    void updateBandIds_nullArtisId_throwsIllegalArgumentException() {
        // Act / Assert
        assertThrows(IllegalArgumentException.class, () -> artistFacade.updateBandIds(null, Set.of()));
        verify(artistService, times(0)).updateArtistByBandIds(any(), any());
    }

    @Test
    void updateBandsIds_changedBandIds_returnsUpdatedArtist() {
        // Arrange
        Set<Long> bandIds = Set.of(1L, 2L);
        Mockito.when(artistService.updateArtistByBandIds(TestDataFactory.TEST_ARTIST_1.getId(), bandIds)).thenReturn(TestDataFactory.TEST_ARTIST_1);
        Mockito.when(artistMapper.toDTO(TestDataFactory.TEST_ARTIST_1)).thenReturn(TestDataFactory.TEST_ARTIST_1_DTO);

        // Act
        ArtistDTO updated = artistFacade.updateBandIds(TestDataFactory.TEST_ARTIST_1.getId(), bandIds);

        // Assert
        assertEquals(TestDataFactory.TEST_ARTIST_1_DTO, updated);
        verify(artistService, times(1)).updateArtistByBandIds(TestDataFactory.TEST_ARTIST_1.getId(), bandIds);
    }
}
