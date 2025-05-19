package cz.muni.fi.bandmanagementservice.facade;

import cz.muni.fi.bandmanagementservice.TestDataFactory;
import cz.muni.fi.bandmanagementservice.dto.BandInfoUpdateDto;
import cz.muni.fi.bandmanagementservice.mappers.BandInfoUpdateMapper;
import cz.muni.fi.bandmanagementservice.model.Band;
import cz.muni.fi.bandmanagementservice.model.BandInfoUpdate;
import cz.muni.fi.bandmanagementservice.mappers.BandMapper;
import cz.muni.fi.bandmanagementservice.dto.BandDto;
import cz.muni.fi.bandmanagementservice.service.BandService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class BandFacadeTest {
    @Mock
    private BandMapper bandMapper;

    @Mock
    private BandInfoUpdateMapper bandInfoUpdateMapper;

    @Mock
    private BandService bandService;

    @InjectMocks
    private BandFacade bandFacade;



    @Test
    public void testCreateBand() {
        Band band = TestDataFactory.setUpBand1();
        BandDto dto = TestDataFactory.setUpBandDto1();
        when(bandMapper.toDto(band)).thenReturn(dto);
        when(bandService.createBand("Band 1", "Rock", 1L)).thenReturn(band);

        BandDto bandDto = bandFacade.createBand("Band 1", "Rock", 1L);

        assertEquals("Band 1", bandDto.getName());
        verify(bandService, times(1)).createBand("Band 1", "Rock", 1L);
    }

    @Test
    public void testGetBand() {
        Band band = TestDataFactory.setUpBand1();
        BandDto dto = TestDataFactory.setUpBandDto1();
        when(bandService.getBand(1L)).thenReturn(band);
        when(bandMapper.toDto(band)).thenReturn(dto);

        BandDto bandDto = bandFacade.getBand(1L);

        assertEquals("Band 1", bandDto.getName());
        verify(bandService, times(1)).getBand(1L);
    }

    @Test
    public void testUpdateBand() {
        BandInfoUpdateDto request = new BandInfoUpdateDto();
        request.setId(1L);
        request.setName("Updated Band");
        request.setMusicalStyle("Jazz");
        request.setLogoUrl(null);
        request.setManagerId(2L);

        BandInfoUpdate infoUpdate = new BandInfoUpdate(1L, "Updated Band", "Jazz", 2L, null);

        Band updatedBand = new Band(1L, "Updated Band", "Jazz", 2L);
        BandDto updatedBandDto = new BandDto();
        updatedBandDto.setId(1L);
        updatedBandDto.setName("Updated Band");
        updatedBandDto.setMusicalStyle("Jazz");
        updatedBandDto.setLogo(null);

        when(bandMapper.toDto(updatedBand)).thenReturn(updatedBandDto);
        when(bandInfoUpdateMapper.toEntity(request)).thenReturn(infoUpdate);
        when(bandService.updateBand(infoUpdate)).thenReturn(updatedBand);

        BandDto bandDto = bandFacade.updateBand(request);

        assertEquals("Updated Band", bandDto.getName());
        verify(bandService, times(1)).updateBand(infoUpdate);
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
    public void testAddMember() {
        Band band = TestDataFactory.setUpBand1();
        BandDto dto = TestDataFactory.setUpBandDto1();

        when(bandService.addMember(1L, 2L)).thenReturn(band);
        when(bandMapper.toDto(band)).thenReturn(dto);

        BandDto bandDto = bandFacade.addMember(1L, 2L);

        assertEquals("Band 1", bandDto.getName());
        verify(bandService, times(1)).addMember(1L, 2L);
    }

    @Test
    public void testRemoveMember() {
        Band band = TestDataFactory.setUpBand1();
        BandDto dto = TestDataFactory.setUpBandDto1();

        when(bandMapper.toDto(band)).thenReturn(dto);
        when(bandService.removeMember(1L, 2L)).thenReturn(band);

        BandDto bandDto = bandFacade.removeMember(1L, 2L);

        assertEquals("Band 1", bandDto.getName());
        verify(bandService, times(1)).removeMember(1L, 2L);
    }
}