package cz.muni.fi.bandmanagementservice.facade;

import cz.muni.fi.bandmanagementservice.model.Band;
import cz.muni.fi.bandmanagementservice.model.BandInfoUpdate;
import cz.muni.fi.bandmanagementservice.mappers.BandMapper;
import cz.muni.fi.bandmanagementservice.dto.BandDto;
import cz.muni.fi.bandmanagementservice.dto.BandInfoUpdateRequest;
import cz.muni.fi.bandmanagementservice.service.BandService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class BandFacadeTest {

    @Mock
    private BandService bandService;

    @InjectMocks
    private BandFacade bandFacade;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testCreateBand() {
        Band band = new Band(1L, "Band Name", "Rock", 1L);
        when(bandService.createBand("Band Name", "Rock", 1L)).thenReturn(band);

        BandDto bandDto = bandFacade.createBand("Band Name", "Rock", 1L);

        assertEquals("Band Name", bandDto.getName());
        verify(bandService, times(1)).createBand("Band Name", "Rock", 1L);
    }

    @Test
    public void testGetBand() {
        Band band = new Band(1L, "Band Name", "Rock", 1L);
        when(bandService.getBand(1L)).thenReturn(band);

        BandDto bandDto = bandFacade.getBand(1L);

        assertEquals("Band Name", bandDto.getName());
        verify(bandService, times(1)).getBand(1L);
    }

    @Test
    public void testUpdateBand() {
        BandInfoUpdateRequest request = new BandInfoUpdateRequest();
        request.setId(1L);
        request.setName("Updated Band");
        BandInfoUpdate bandInfoUpdate = BandMapper.mapFromInfoUpdateRequest(request);
        Band band = new Band(1L, "Updated Band", "Jazz", 1L);
        when(bandService.updateBand(bandInfoUpdate)).thenReturn(band);

        BandDto bandDto = bandFacade.updateBand(request);

        assertEquals("Updated Band", bandDto.getName());
        verify(bandService, times(1)).updateBand(bandInfoUpdate);
    }

    @Test
    public void testGetAllBands() {
        Band band = new Band(1L, "Band Name", "Rock", 1L);
        when(bandService.getAllBands()).thenReturn(Collections.singletonList(band));

        List<BandDto> bands = bandFacade.getAllBands();

        assertEquals(1, bands.size());
        verify(bandService, times(1)).getAllBands();
    }

    @Test
    public void testRemoveMember() {
        Band band = new Band(1L, "Band Name", "Rock", 1L);
        when(bandService.removeMember(1L, 2L)).thenReturn(band);

        BandDto bandDto = bandFacade.removeMember(1L, 2L);

        assertEquals("Band Name", bandDto.getName());
        verify(bandService, times(1)).removeMember(1L, 2L);
    }
}