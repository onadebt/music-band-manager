package cz.muni.fi.bandmanagementservice.api;

import cz.muni.fi.bandmanagementservice.band.data.model.Band;
import cz.muni.fi.bandmanagementservice.band.data.model.BandInfoUpdate;
import cz.muni.fi.bandmanagementservice.band.data.repository.BandRepository;
import cz.muni.fi.bandmanagementservice.band.service.BandService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class BandServiceTest {

    @Mock
    private BandRepository bandRepository;

    @InjectMocks
    private BandService bandService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testCreateBand() {
        Band band = new Band(1L, "Band Name", "Rock", 1L);
        when(bandRepository.createBand(any(Band.class))).thenReturn(band);

        Band createdBand = bandService.createBand("Band Name", "Rock", 1L);

        assertEquals("Band Name", createdBand.getName());
        verify(bandRepository, times(1)).createBand(any(Band.class));
    }

    @Test
    public void testGetBand() {
        Band band = new Band(1L, "Band Name", "Rock", 1L);
        when(bandRepository.getBandById(1L)).thenReturn(Optional.of(band));

        Band foundBand = bandService.getBand(1L);

        assertEquals("Band Name", foundBand.getName());
        verify(bandRepository, times(1)).getBandById(1L);
    }

    @Test
    public void testUpdateBand() {
        Band band = new Band(1L, "Updated Band", "Jazz", 1L);
        when(bandRepository.createBand(any(Band.class))).thenReturn(band);

        BandInfoUpdate bandInfoUpdate = new BandInfoUpdate(1L, "Updated Band", "Jazz", 1L, "logoUrl");
        when(bandRepository.getBandById(1L)).thenReturn(Optional.of(band));
        when(bandRepository.updateBand(any(Band.class))).thenReturn(band);
        Band updatedBand = bandService.updateBand(bandInfoUpdate);

        assertEquals("Updated Band", updatedBand.getName());
        verify(bandRepository, times(1)).createBand(any(Band.class));
    }

    @Test
    public void testGetAllBands() {
        Band band = new Band(1L, "Band Name", "Rock", 1L);
        when(bandRepository.getAllBands()).thenReturn(Collections.singletonList(band));

        List<Band> bands = bandService.getAllBands();

        assertEquals(1, bands.size());
        verify(bandRepository, times(1)).getAllBands();
    }

    @Test
    public void testRemoveMember() {
        Band band = new Band(1L, "Band Name", "Rock", 1L);
        when(bandRepository.getBandById(1L)).thenReturn(Optional.of(band));
        when(bandRepository.createBand(any(Band.class))).thenReturn(band);

        Band updatedBand = bandService.removeMember(1L, 2L);

        assertEquals("Band Name", updatedBand.getName());
        verify(bandRepository, times(1)).getBandById(1L);
        verify(bandRepository, times(1)).createBand(any(Band.class));
    }
}