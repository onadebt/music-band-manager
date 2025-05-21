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
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

/**
 * @author Tomáš MAREK
 */
@ExtendWith(MockitoExtension.class)
public class ArtistFacadeImplTest {
    @InjectMocks
    private ArtistFacadeImpl artistFacadeImpl;

    @Mock
    private ArtistService artistService;

    @Mock
    private ArtistMapper artistMapper;

    @Test
    void register_validArtistDto_returnsSavedArtist() {
        Artist testArtist = TestDataFactory.setUpTestArtist1();
        ArtistDto testArtistDto = TestDataFactory.setUpTestArtist1Dto();

        when(artistService.save(testArtist)).thenReturn(testArtist);
        when(artistMapper.toEntity(testArtistDto)).thenReturn(testArtist);
        when(artistMapper.toDto(testArtist)).thenReturn(testArtistDto);

        ArtistDto registered = artistFacadeImpl.register(testArtistDto);

        verify(artistService, times(1)).save(testArtist);
        assertEquals(testArtistDto, registered);
    }

    @Test
    void findById_validId_returnsFoundArtist() {
        Artist testArtist = TestDataFactory.setUpTestArtist1();
        ArtistDto testArtistDto = TestDataFactory.setUpTestArtist1Dto();

        when(artistService.findById(testArtist.getId())).thenReturn(testArtist);
        when(artistMapper.toDto(testArtist)).thenReturn(testArtistDto);

        ArtistDto found = artistFacadeImpl.findById(testArtist.getId());

        assertEquals(testArtistDto, found);
        verify(artistService, times(1)).findById(testArtist.getId());
    }

    @Test
    void findByUsername_validUsername_returnsFoundArtist() {
        Artist testArtist = TestDataFactory.setUpTestArtist1();
        ArtistDto testArtistDto = TestDataFactory.setUpTestArtist1Dto();

        when(artistService.findByUsername(testArtist.getUsername())).thenReturn(testArtist);
        when(artistMapper.toDto(testArtist)).thenReturn(testArtistDto);

        ArtistDto found = artistFacadeImpl.findByUsername(testArtist.getUsername());

        assertEquals(testArtistDto, found);
        verify(artistService, times(1)).findByUsername(testArtist.getUsername());
    }

    @Test
    void findAll_noArtistStored_returnsEmptyList() {
        when(artistService.findAll()).thenReturn(List.of());

        List<ArtistDto> found = artistFacadeImpl.findAll();

        assertEquals(0, found.size());
        verify(artistService, times(1)).findAll();
    }

    @Test
    void findAll_twoArtistsStored_returnsList() {
        Artist testArtist = TestDataFactory.setUpTestArtist1();
        Artist testArtist2 = TestDataFactory.setUpTestArtist2();
        ArtistDto testArtistDto = TestDataFactory.setUpTestArtist1Dto();
        ArtistDto testArtistDto2 = TestDataFactory.setUpTestArtist2Dto();

        when(artistService.findAll()).thenReturn(List.of(testArtist, testArtist2));
        when(artistMapper.toDto(testArtist)).thenReturn(testArtistDto);
        when(artistMapper.toDto(testArtist2)).thenReturn(testArtistDto2);

        List<ArtistDto> found = artistFacadeImpl.findAll();

        assertEquals(2, found.size());
        verify(artistService, times(1)).findAll();
        assertTrue(found.contains(testArtistDto));
        assertTrue(found.contains(testArtistDto2));
    }

    @Test
    void findByBandIds_twoArtistsFound_returnsList() {
        Artist testArtist = TestDataFactory.setUpTestArtist1();
        Artist testArtist2 = TestDataFactory.setUpTestArtist2();
        ArtistDto testArtistDto = TestDataFactory.setUpTestArtist1Dto();
        ArtistDto testArtistDto2 = TestDataFactory.setUpTestArtist2Dto();


        Set<Long> bandIds = Set.of(1L, 2L);
        when(artistService.findByBandIds(bandIds)).thenReturn(List.of(testArtist, testArtist2));
        when(artistMapper.toDto(testArtist)).thenReturn(testArtistDto);
        when(artistMapper.toDto(testArtist2)).thenReturn(testArtistDto2);

        List<ArtistDto> found = artistFacadeImpl.findByBandIds(bandIds);

        verify(artistService, times(1)).findByBandIds(bandIds);
        assertTrue(found.contains(testArtistDto));
        assertTrue(found.contains(testArtistDto2));
    }

    @Test
    void deleteById_validId_callsArtistServiceDelete() {
        Artist testArtist = TestDataFactory.setUpTestArtist1();

        artistService.deleteById(testArtist.getId());

        verify(artistService, times(1)).deleteById(testArtist.getId());
    }

    @Test
    void updateBandsIds_changedBandIds_returnsUpdatedArtist() {
        Artist testArtist = TestDataFactory.setUpTestArtist1();
        ArtistDto testArtistDto = TestDataFactory.setUpTestArtist1Dto();

        Set<Long> bandIds = Set.of(1L, 2L);
        when(artistService.updateArtistByBandIds(testArtist.getId(), bandIds)).thenReturn(testArtist);
        when(artistMapper.toDto(testArtist)).thenReturn(testArtistDto);

        ArtistDto updated = artistFacadeImpl.updateBandIds(testArtist.getId(), bandIds);

        assertEquals(testArtistDto, updated);
        verify(artistService, times(1)).updateArtistByBandIds(testArtist.getId(), bandIds);
    }
}
