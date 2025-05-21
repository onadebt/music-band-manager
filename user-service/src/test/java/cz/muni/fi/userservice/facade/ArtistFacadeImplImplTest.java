package cz.muni.fi.userservice.facade;

import cz.muni.fi.userservice.TestDataFactory;
import cz.muni.fi.userservice.dto.ArtistDto;
import cz.muni.fi.userservice.mapper.ArtistMapper;
import cz.muni.fi.userservice.model.Artist;
import cz.muni.fi.userservice.service.interfaces.ArtistService;
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
import static org.mockito.Mockito.*;

/**
 * @author Tomáš MAREK
 */
@ExtendWith(MockitoExtension.class)
public class ArtistFacadeImplImplTest {
    @InjectMocks
    private ArtistFacadeImpl artistFacadeImpl;

    @Mock
    private ArtistService artistService;

    @Mock
    private ArtistMapper artistMapper;

    @Test
    void register_nullArtistDto_throwsIllegalArgumentException() {
        // Act / Assert
        assertThrows(IllegalArgumentException.class, () -> artistFacadeImpl.register(null));
        verify(artistService, Mockito.times(0)).save(any());
    }

    @Test
    void register_validArtistDto_returnsSavedArtist() {
        // Arrange
        Artist testArtist = TestDataFactory.setUpTestArtist1();
        ArtistDto testArtistDto = TestDataFactory.setUpTestArtist1Dto();

        when(artistService.save(testArtist)).thenReturn(testArtist);
        when(artistMapper.toEntity(testArtistDto)).thenReturn(testArtist);
        when(artistMapper.toDto(testArtist)).thenReturn(testArtistDto);

        // Act
        ArtistDto registered = artistFacadeImpl.register(testArtistDto);

        // Assert
        verify(artistService, times(1)).save(testArtist);
        assertEquals(testArtistDto, registered);
    }

    @Test
    void findById_inputNull_throwsIllegalArgumentException() {
        // Act / Assert
        assertThrows(IllegalArgumentException.class, () -> artistFacadeImpl.findById(null));
        verify(artistService, Mockito.times(0)).findById(any());
    }

    @Test
    void findById_invalidId_throwsIllegalArgumentException() {
        // Arrange
        Long invalidId = -1L;
        when(artistService.findById(invalidId)).thenReturn(null);

        // Act / Assert
        assertThrows(IllegalArgumentException.class, () -> artistFacadeImpl.findById(invalidId));
    }

    @Test
    void findById_validId_returnsFoundArtist() {
        Artist testArtist = TestDataFactory.setUpTestArtist1();
        ArtistDto testArtistDto = TestDataFactory.setUpTestArtist1Dto();



        // Arrange
        when(artistService.findById(testArtist.getId())).thenReturn(testArtist);
        when(artistMapper.toDto(testArtist)).thenReturn(testArtistDto);
        // Act
        ArtistDto found = artistFacadeImpl.findById(testArtist.getId());

        // Assert
        assertEquals(testArtistDto, found);
        verify(artistService, times(1)).findById(testArtist.getId());
    }

    @Test
    void findByUsername_nullUsername_throwsIllegalArgumentException() {
        // Act / Assert
        assertThrows(IllegalArgumentException.class, () -> artistFacadeImpl.findByUsername(null));
        verify(artistService, times(0)).findByUsername(any());
    }

    @Test
    void findByUsername_invalidUsername_throwsIllegalArgumentException() {
        // Arrange
        String invalidUsername = "invalidUsername";
        when(artistService.findByUsername(invalidUsername)).thenReturn(null);

        // Act / Assert
        assertThrows(IllegalArgumentException.class, () -> artistFacadeImpl.findByUsername(invalidUsername));
    }

    @Test
    void findByUsername_validUsername_returnsFoundArtist() {
        Artist testArtist = TestDataFactory.setUpTestArtist1();
        ArtistDto testArtistDto = TestDataFactory.setUpTestArtist1Dto();

        // Arrange
        when(artistService.findByUsername(testArtist.getUsername())).thenReturn(testArtist);
        when(artistMapper.toDto(testArtist)).thenReturn(testArtistDto);

        // Act
        ArtistDto found = artistFacadeImpl.findByUsername(testArtist.getUsername());

        // Assert
        assertEquals(testArtistDto, found);
        verify(artistService, times(1)).findByUsername(testArtist.getUsername());
    }

    @Test
    void findAll_noArtistStored_returnsEmptyList() {
        // Arrange
        when(artistService.findAll()).thenReturn(List.of());

        // Act
        List<ArtistDto> found = artistFacadeImpl.findAll();

        // Assert
        assertEquals(0, found.size());
        verify(artistService, times(1)).findAll();
    }

    @Test
    void findAll_twoArtistsStored_returnsList() {
        Artist testArtist = TestDataFactory.setUpTestArtist1();
        Artist testArtist2 = TestDataFactory.setUpTestArtist2();
        ArtistDto testArtistDto = TestDataFactory.setUpTestArtist1Dto();
        ArtistDto testArtistDto2 = TestDataFactory.setUpTestArtist2Dto();

        // Arrange
        when(artistService.findAll()).thenReturn(List.of(testArtist, testArtist2));
        when(artistMapper.toDto(testArtist)).thenReturn(testArtistDto);
        when(artistMapper.toDto(testArtist2)).thenReturn(testArtistDto2);

        // Act
        List<ArtistDto> found = artistFacadeImpl.findAll();

        // Assert
        assertEquals(2, found.size());
        verify(artistService, times(1)).findAll();
        assertTrue(found.contains(testArtistDto));
        assertTrue(found.contains(testArtistDto2));
    }

    @Test
    void findByBandIds_nullArgument_throwsIllegalArgumentException() {
        // Act / Assert
        assertThrows(IllegalArgumentException.class, () -> artistFacadeImpl.findByBandIds(null));
        verify(artistService, times(0)).findByBandIds(any());
    }

    @Test
    void findByBandIds_twoArtistsFound_returnsList() {
        Artist testArtist = TestDataFactory.setUpTestArtist1();
        Artist testArtist2 = TestDataFactory.setUpTestArtist2();
        ArtistDto testArtistDto = TestDataFactory.setUpTestArtist1Dto();
        ArtistDto testArtistDto2 = TestDataFactory.setUpTestArtist2Dto();


        // Arrange
        Set<Long> bandIds = Set.of(1L, 2L);
        when(artistService.findByBandIds(bandIds)).thenReturn(List.of(testArtist, testArtist2));
        when(artistMapper.toDto(testArtist)).thenReturn(testArtistDto);
        when(artistMapper.toDto(testArtist2)).thenReturn(testArtistDto2);

        // Act
        List<ArtistDto> found = artistFacadeImpl.findByBandIds(bandIds);

        // Assert
        verify(artistService, times(1)).findByBandIds(bandIds);
        assertTrue(found.contains(testArtistDto));
        assertTrue(found.contains(testArtistDto2));
    }

    @Test
    void deleteById_nullId_throwsIllegalArgumentException() {
        // Act / Assert
        assertThrows(IllegalArgumentException.class, () -> artistFacadeImpl.deleteById(null));
        verify(artistService, times(0)).deleteById(null);
    }

    @Test
    void deleteById_invalidId_throwsIllegalArgumentException() {
        // Arrange
        Long invalidId = -1L;
        when(artistService.findById(invalidId)).thenReturn(null);

        // Act / Assert
        assertThrows(IllegalArgumentException.class, () -> artistFacadeImpl.deleteById(invalidId));
        verify(artistService, times(1)).findById(invalidId);
    }

    @Test
    void deleteById_validId_callsArtistServiceDelete() {
        Artist testArtist = TestDataFactory.setUpTestArtist1();

        // Act
        artistService.deleteById(testArtist.getId());

        // Assert
        verify(artistService, times(1)).deleteById(testArtist.getId());
    }

    @Test
    void updateBandIds_nullBandIds_throwsIllegalArgumentException() {
        // Act / Assert
        assertThrows(IllegalArgumentException.class, () -> artistFacadeImpl.updateBandIds(1L, null));
        verify(artistService, times(0)).updateArtistByBandIds(any(), any());
    }

    @Test
    void updateBandIds_nullArtisId_throwsIllegalArgumentException() {
        // Act / Assert
        assertThrows(IllegalArgumentException.class, () -> artistFacadeImpl.updateBandIds(null, Set.of()));
        verify(artistService, times(0)).updateArtistByBandIds(any(), any());
    }

    @Test
    void updateBandsIds_changedBandIds_returnsUpdatedArtist() {
        Artist testArtist = TestDataFactory.setUpTestArtist1();
        ArtistDto testArtistDto = TestDataFactory.setUpTestArtist1Dto();

        // Arrange
        Set<Long> bandIds = Set.of(1L, 2L);
        when(artistService.updateArtistByBandIds(testArtist.getId(), bandIds)).thenReturn(testArtist);
        when(artistMapper.toDto(testArtist)).thenReturn(testArtistDto);

        // Act
        ArtistDto updated = artistFacadeImpl.updateBandIds(testArtist.getId(), bandIds);

        // Assert
        assertEquals(testArtistDto, updated);
        verify(artistService, times(1)).updateArtistByBandIds(testArtist.getId(), bandIds);
    }
}
