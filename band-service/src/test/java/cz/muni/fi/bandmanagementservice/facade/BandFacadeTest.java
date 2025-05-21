package cz.muni.fi.bandmanagementservice.facade;

import cz.muni.fi.bandmanagementservice.TestDataFactory;
import cz.muni.fi.bandmanagementservice.dto.BandDto;
import cz.muni.fi.bandmanagementservice.dto.BandInfoUpdateDto;
import cz.muni.fi.bandmanagementservice.exception.BandNotFoundException;
import cz.muni.fi.bandmanagementservice.mapper.BandMapper;
import cz.muni.fi.bandmanagementservice.model.Band;
import cz.muni.fi.bandmanagementservice.service.BandService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class BandFacadeTest {

    @Mock
    private BandMapper bandMapper;

    @Mock
    private BandService bandService;

    @InjectMocks
    private BandFacade bandFacade;

    @Test
    void createBand_validData_returnsCreatedBandDto() {
        Band band = TestDataFactory.setUpBand1();
        BandDto dto = TestDataFactory.setUpBandDto1();

        when(bandService.createBand(band.getName(), band.getMusicalStyle(), band.getManagerId())).thenReturn(band);
        when(bandMapper.toDto(band)).thenReturn(dto);

        BandDto result = bandFacade.createBand(band.getName(), band.getMusicalStyle(), band.getManagerId());

        assertEquals(band.getName(), result.getName());
        verify(bandService).createBand(band.getName(), band.getMusicalStyle(), band.getManagerId());
        verify(bandMapper).toDto(band);
    }

    @Test
    void getBand_existingId_returnsBandDto() {
        Band band = TestDataFactory.setUpBand1();
        BandDto dto = TestDataFactory.setUpBandDto1();

        when(bandService.getBand(band.getId())).thenReturn(band);
        when(bandMapper.toDto(band)).thenReturn(dto);

        BandDto result = bandFacade.getBand(band.getId());

        assertEquals(dto.getName(), result.getName());
        verify(bandService).getBand(band.getId());
        verify(bandMapper).toDto(band);
    }

    @Test
    void updateBand_validIdAndRequest_returnsUpdatedBandDto() {
        Long id = 1L;
        BandInfoUpdateDto request = TestDataFactory.setUpBandInfoUpdateDto1();
        Band bandEntity = TestDataFactory.setUpBand1();
        BandDto updatedDto = TestDataFactory.setUpBandDto1();

        when(bandMapper.toEntity(request)).thenReturn(bandEntity);
        when(bandService.updateBand(id, bandEntity)).thenReturn(bandEntity);
        when(bandMapper.toDto(bandEntity)).thenReturn(updatedDto);

        BandDto result = bandFacade.updateBand(id, request);

        assertEquals(updatedDto.getName(), result.getName());
        verify(bandService).updateBand(id, bandEntity);
        verify(bandMapper).toDto(bandEntity);
    }

    @Test
    void getAllBands_bandsExist_returnsListOfBandDtos() {
        Band band = TestDataFactory.setUpBand1();
        BandDto dto = TestDataFactory.setUpBandDto1();

        when(bandService.getAllBands()).thenReturn(List.of(band));
        when(bandMapper.toDto(band)).thenReturn(dto);

        List<BandDto> result = bandFacade.getAllBands();

        assertEquals(1, result.size());
        assertEquals(band.getName(), result.getFirst().getName());
        verify(bandService).getAllBands();
    }

    @Test
    void addMember_validBandAndMember_returnsUpdatedBandDto() {
        Band band = TestDataFactory.setUpBand1();
        BandDto dto = TestDataFactory.setUpBandDto1();

        when(bandService.addMember(1L, 2L)).thenReturn(band);
        when(bandMapper.toDto(band)).thenReturn(dto);

        BandDto result = bandFacade.addMember(1L, 2L);

        assertEquals(band.getName(), result.getName());
        verify(bandService).addMember(1L, 2L);
    }

    @Test
    void removeMember_validBandAndMember_returnsUpdatedBandDto() {
        Band band = TestDataFactory.setUpBand1();
        BandDto dto = TestDataFactory.setUpBandDto1();

        when(bandService.removeMember(1L, 2L)).thenReturn(band);
        when(bandMapper.toDto(band)).thenReturn(dto);

        BandDto result = bandFacade.removeMember(1L, 2L);

        assertEquals(band.getName(), result.getName());
        verify(bandService).removeMember(1L, 2L);
    }

    @Test
    void getBand_bandNotFound_throwsException() {
        Long invalidId = 999L;

        when(bandService.getBand(invalidId)).thenThrow(new BandNotFoundException(invalidId));

        assertThrows(BandNotFoundException.class, () -> bandFacade.getBand(invalidId));
        verify(bandService).getBand(invalidId);
    }

    @Test
    void updateBand_bandDoesNotExist_throwsException() {
        Long invalidId = 99L;
        BandInfoUpdateDto request = TestDataFactory.setUpBandInfoUpdateDto1();
        Band bandEntity = TestDataFactory.setUpBand1();

        when(bandMapper.toEntity(request)).thenReturn(bandEntity);
        when(bandService.updateBand(invalidId, bandEntity)).thenThrow(new BandNotFoundException(invalidId));

        assertThrows(BandNotFoundException.class, () -> bandFacade.updateBand(invalidId, request));
        verify(bandService).updateBand(invalidId, bandEntity);
    }

    @Test
    void getAllBands_noBandsExist_returnsEmptyList() {
        when(bandService.getAllBands()).thenReturn(Collections.emptyList());

        List<BandDto> result = bandFacade.getAllBands();

        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(bandService).getAllBands();
    }

    @Test
    void addMember_bandDoesNotExist_throwsException() {
        Long invalidId = 999L;
        Long memberId = 1L;

        when(bandService.addMember(invalidId, memberId)).thenThrow(new BandNotFoundException(invalidId));

        assertThrows(BandNotFoundException.class, () -> bandFacade.addMember(invalidId, memberId));
        verify(bandService).addMember(invalidId, memberId);
    }

    @Test
    void removeMember_memberNotInBand_throwsException() {
        Long bandId = 1L;
        Long memberId = 999L;

        when(bandService.removeMember(bandId, memberId)).thenThrow(new BandNotFoundException(memberId));

        assertThrows(BandNotFoundException.class, () -> bandFacade.removeMember(bandId, memberId));
        verify(bandService).removeMember(bandId, memberId);
    }
}
