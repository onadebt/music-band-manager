package cz.muni.fi.bandmanagementservice.service;

import cz.muni.fi.bandmanagementservice.TestDataFactory;
import cz.muni.fi.bandmanagementservice.exception.BandAlreadyExistsException;
import cz.muni.fi.bandmanagementservice.exception.BandNotFoundException;
import cz.muni.fi.bandmanagementservice.exception.MusicianAlreadyInBandException;
import cz.muni.fi.bandmanagementservice.exception.MusicianNotInBandException;
import cz.muni.fi.bandmanagementservice.model.Band;
import cz.muni.fi.bandmanagementservice.repository.BandRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class BandServiceTest {

    @Mock
    private BandRepository bandRepository;

    @InjectMocks
    private BandService bandService;

    @Test
    void createBand_uniqueName_savesAndReturnsBand() {
        Band band = TestDataFactory.setUpBand1();
        when(bandRepository.findByName(band.getName())).thenReturn(Optional.empty());
        when(bandRepository.save(any(Band.class))).thenReturn(band);

        Band result = bandService.createBand(band.getName(), band.getMusicalStyle(), band.getManagerId());

        assertEquals(band.getName(), result.getName());
        verify(bandRepository).findByName(band.getName());
        verify(bandRepository).save(any(Band.class));
    }

    @Test
    void createBand_nameAlreadyExists_throwsBandAlreadyExistsException() {
        Band existingBand = TestDataFactory.setUpBand1();
        when(bandRepository.findByName(existingBand.getName())).thenReturn(Optional.of(existingBand));

        assertThrows(BandAlreadyExistsException.class, () ->
                bandService.createBand(existingBand.getName(), "Other style", 2L));

        verify(bandRepository).findByName(existingBand.getName());
        verify(bandRepository, never()).save(any());
    }

    @Test
    void getBand_existingId_returnsBand() {
        Band band = TestDataFactory.setUpBand1();
        when(bandRepository.findById(1L)).thenReturn(Optional.of(band));

        Band result = bandService.getBand(1L);

        assertEquals(band.getName(), result.getName());
        verify(bandRepository).findById(1L);
    }

    @Test
    void getBand_nonExistentId_throwsBandNotFoundException() {
        when(bandRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(BandNotFoundException.class, () -> bandService.getBand(99L));
        verify(bandRepository).findById(99L);
    }

    @Test
    void getAllBands_someBandsExist_returnsList() {
        Band band = TestDataFactory.setUpBand1();
        when(bandRepository.findAll()).thenReturn(List.of(band));

        List<Band> result = bandService.getAllBands();

        assertEquals(1, result.size());
        assertEquals("Band 1", result.get(0).getName());
        verify(bandRepository).findAll();
    }

    @Test
    void updateBand_existingId_updatesFields() {
        Band existingBand = TestDataFactory.setUpBand1();
        Band updatedBand = TestDataFactory.setUpBand2();

        when(bandRepository.findById(1L)).thenReturn(Optional.of(existingBand));
        when(bandRepository.save(any(Band.class))).thenReturn(updatedBand);

        Band bandToUpdate = new Band(1L, "Band 2", "Pop", 2L);
        bandToUpdate.setLogoUrl("http://example.com/logo2.png");
        bandToUpdate.setMembers(Set.of(4L, 5L, 6L));

        Band result = bandService.updateBand(1L, bandToUpdate);

        assertEquals("Band 2", result.getName());
        assertEquals("Pop", result.getMusicalStyle());
        verify(bandRepository).findById(1L);
        verify(bandRepository).save(any(Band.class));
    }

    @Test
    void updateBand_nonExistentId_throwsBandNotFoundException() {
        Band bandToUpdate = TestDataFactory.setUpBand2();
        when(bandRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(BandNotFoundException.class, () -> bandService.updateBand(99L, bandToUpdate));
        verify(bandRepository).findById(99L);
    }

    @Test
    void addMember_notPresent_addsMemberSuccessfully() {
        Band band = TestDataFactory.setUpBand1();
        Long newMemberId = 99L;

        when(bandRepository.findById(band.getId())).thenReturn(Optional.of(band));
        when(bandRepository.save(any(Band.class))).thenReturn(band);

        Band result = bandService.addMember(band.getId(), newMemberId);

        assertTrue(result.getMembers().contains(newMemberId));
        verify(bandRepository).findById(band.getId());
        verify(bandRepository).save(any(Band.class));
    }

    @Test
    void addMember_alreadyPresent_throwsMusicianAlreadyInBandException() {
        Band band = TestDataFactory.setUpBand1();
        Long existingMember = 1L;

        when(bandRepository.findById(band.getId())).thenReturn(Optional.of(band));

        assertThrows(MusicianAlreadyInBandException.class, () -> bandService.addMember(band.getId(), existingMember));
        verify(bandRepository).findById(band.getId());
    }

    @Test
    void removeMember_present_removesMemberSuccessfully() {
        Band band = TestDataFactory.setUpBand1();
        Long memberId = 2L;

        when(bandRepository.findById(band.getId())).thenReturn(Optional.of(band));
        when(bandRepository.save(any(Band.class))).thenReturn(band);

        Band result = bandService.removeMember(band.getId(), memberId);

        assertFalse(result.getMembers().contains(memberId));
        verify(bandRepository).findById(band.getId());
        verify(bandRepository).save(any(Band.class));
    }

    @Test
    void removeMember_notInBand_throwsMusicianNotInBandException() {
        Band band = TestDataFactory.setUpBand1();
        Long notAMember = 999L;

        when(bandRepository.findById(band.getId())).thenReturn(Optional.of(band));

        assertThrows(MusicianNotInBandException.class, () -> bandService.removeMember(band.getId(), notAMember));
        verify(bandRepository).findById(band.getId());
    }
}
