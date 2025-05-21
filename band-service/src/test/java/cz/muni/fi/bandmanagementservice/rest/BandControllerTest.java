package cz.muni.fi.bandmanagementservice.rest;

import cz.muni.fi.bandmanagementservice.TestDataFactory;
import cz.muni.fi.bandmanagementservice.dto.BandDto;
import cz.muni.fi.bandmanagementservice.dto.BandInfoUpdateDto;
import cz.muni.fi.bandmanagementservice.exception.BandAlreadyExistsException;
import cz.muni.fi.bandmanagementservice.exception.BandNotFoundException;
import cz.muni.fi.bandmanagementservice.facade.BandFacade;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;


/**
 * Created by Ivan Yatskiv, changed during refactor by Tomáš Marek
 */
@ExtendWith(MockitoExtension.class)
public class BandControllerTest {

    @Mock
    private BandFacade bandFacade;

    @InjectMocks
    private BandController controller;

    @Test
    void createBand_validRequest_returnsCreatedBandDto() {
        BandDto bandDto = TestDataFactory.setUpBandDto1();
        when(bandFacade.createBand("BandName", "Rock", 1L)).thenReturn(bandDto);

        ResponseEntity<BandDto> response = controller.createBand("BandName", "Rock", 1L);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(bandDto, response.getBody());
    }

    @Test
    void createBand_duplicateName_throwsException() {
        when(bandFacade.createBand("BandName", "Rock", 1L))
                .thenThrow(new BandAlreadyExistsException("Band with name already exists"));

        assertThrows(BandAlreadyExistsException.class, () ->
                controller.createBand("BandName", "Rock", 1L));
    }

    @Test
    void getBand_existingId_returnsBandDto() {
        BandDto bandDto = TestDataFactory.setUpBandDto1();
        when(bandFacade.getBand(1L)).thenReturn(bandDto);

        ResponseEntity<BandDto> response = controller.getBand(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(bandDto, response.getBody());
    }

    @Test
    void getBand_nonExistentId_throwsBandNotFoundException() {
        when(bandFacade.getBand(999L)).thenThrow(new BandNotFoundException(999L));

        assertThrows(BandNotFoundException.class, () -> controller.getBand(999L));
    }

    @Test
    void getAllBands_multipleBands_returnsList() {
        List<BandDto> bands = List.of(TestDataFactory.setUpBandDto1(), TestDataFactory.setUpBandDto2());
        when(bandFacade.getAllBands()).thenReturn(bands);

        ResponseEntity<List<BandDto>> response = controller.getAllBands();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(2, response.getBody().size());
    }

    @Test
    void getAllBands_noBands_returnsEmptyList() {
        when(bandFacade.getAllBands()).thenReturn(Collections.emptyList());

        ResponseEntity<List<BandDto>> response = controller.getAllBands();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getBody().isEmpty());
    }

    @Test
    void updateBand_validRequest_returnsUpdatedBandDto() {
        Long id = 1L;
        BandInfoUpdateDto updateDto = TestDataFactory.setUpBandInfoUpdateDto1();
        BandDto updatedDto = TestDataFactory.setUpBandDto1();

        when(bandFacade.updateBand(id, updateDto)).thenReturn(updatedDto);

        ResponseEntity<BandDto> response = controller.updateBand(id, updateDto);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(updatedDto, response.getBody());
    }

    @Test
    void updateBand_nonExistentBand_throwsBandNotFoundException() {
        Long id = 999L;
        BandInfoUpdateDto updateDto = TestDataFactory.setUpBandInfoUpdateDto1();

        when(bandFacade.updateBand(id, updateDto)).thenThrow(new BandNotFoundException(id));

        assertThrows(BandNotFoundException.class, () -> controller.updateBand(id, updateDto));
    }

    @Test
    void removeMember_memberExists_returnsNoContentWithBand() {
        BandDto dto = TestDataFactory.setUpBandDto1();
        when(bandFacade.removeMember(1L, 2L)).thenReturn(dto);

        ResponseEntity<BandDto> response = controller.removeMember(1L, 2L);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        assertEquals(dto, response.getBody());
    }

    @Test
    void removeMember_bandNotFound_throwsException() {
        when(bandFacade.removeMember(1L, 2L)).thenThrow(new BandNotFoundException(1L));

        assertThrows(BandNotFoundException.class, () -> controller.removeMember(1L, 2L));
    }

    @Test
    void addMember_validMember_returnsUpdatedBandDto() {
        BandDto dto = TestDataFactory.setUpBandDto1();
        when(bandFacade.addMember(1L, 2L)).thenReturn(dto);

        ResponseEntity<BandDto> response = controller.addMember(1L, 2L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(dto, response.getBody());
    }

    @Test
    void addMember_bandNotFound_throwsException() {
        when(bandFacade.addMember(1L, 2L)).thenThrow(new BandNotFoundException(1L));

        assertThrows(BandNotFoundException.class, () -> controller.addMember(1L, 2L));
    }
}

