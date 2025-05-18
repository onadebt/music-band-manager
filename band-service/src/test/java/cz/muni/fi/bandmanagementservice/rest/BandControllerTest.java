package cz.muni.fi.bandmanagementservice.rest;

import cz.muni.fi.bandmanagementservice.dto.BandDto;
import cz.muni.fi.bandmanagementservice.dto.BandInfoUpdateRequest;
import cz.muni.fi.bandmanagementservice.exceptions.BandNotFoundException;
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

import static org.junit.jupiter.api.Assertions.assertEquals;
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
    void testCreateBand() {
        BandDto band = new BandDto();
        when(bandFacade.createBand("BandName", "Rock", 1L)).thenReturn(band);

        ResponseEntity<BandDto> response = controller.createBand("BandName", "Rock", 1L);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(band, response.getBody());
    }

    @Test
    void testGetAllBands() {
        List<BandDto> bands = Collections.singletonList(new BandDto());
        when(bandFacade.getAllBands()).thenReturn(bands);

        ResponseEntity<List<BandDto>> response = controller.getAllBands();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(bands, response.getBody());
    }

    @Test
    void testGetBand_NotFound() {
        when(bandFacade.getBand(1L)).thenThrow(new BandNotFoundException(1L));

        ResponseEntity<BandDto> response = controller.getBand(1L);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void testUpdateBand_Success() {
        BandDto updatedBand = new BandDto();
        BandInfoUpdateRequest updateRequest = new BandInfoUpdateRequest();
        when(bandFacade.updateBand(updateRequest)).thenReturn(updatedBand);

        ResponseEntity<BandDto> response = controller.updateBand(updateRequest);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(updatedBand, response.getBody());
    }

    @Test
    void testUpdateBand_NotFound() {
        BandInfoUpdateRequest updateRequest = new BandInfoUpdateRequest();
        when(bandFacade.updateBand(updateRequest)).thenThrow(new BandNotFoundException(null));

        ResponseEntity<BandDto> response = controller.updateBand(updateRequest);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void testGetBand_Success() {
        BandDto band = new BandDto();
        when(bandFacade.getBand(1L)).thenReturn(band);

        ResponseEntity<BandDto> response = controller.getBand(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(band, response.getBody());
    }

    @Test
    void testRemoveMember_Success() {
        BandDto band = new BandDto();
        when(bandFacade.removeMember(1L, 2L)).thenReturn(band);

        ResponseEntity<BandDto> response = controller.removeMember(1L, 2L);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        assertEquals(band, response.getBody());
    }

    @Test
    void testRemoveMember_NotFound() {
        when(bandFacade.removeMember(1L, 2L)).thenThrow(new BandNotFoundException(1L));

        ResponseEntity<BandDto> response = controller.removeMember(1L, 2L);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void testAddMember_Success() {
        BandDto band = new BandDto();
        when(bandFacade.addMember(1L, 2L)).thenReturn(band);

        ResponseEntity<BandDto> response = controller.addMember(1L, 2L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(band, response.getBody());
    }

    @Test
    void testAddMember_NotFound() {
        when(bandFacade.addMember(1L, 2L)).thenThrow(new BandNotFoundException(1L));

        ResponseEntity<BandDto> response = controller.addMember(1L, 2L);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }
}
