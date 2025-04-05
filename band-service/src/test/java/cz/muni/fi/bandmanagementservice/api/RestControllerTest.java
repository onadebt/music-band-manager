package cz.muni.fi.bandmanagementservice.api;

import cz.muni.fi.bandmanagementservice.band.exceptions.InvalidOperationException;
import cz.muni.fi.bandmanagementservice.band.exceptions.ResourceNotFoundException;
import cz.muni.fi.bandmanagementservice.band.facade.BandFacade;
import cz.muni.fi.bandmanagementservice.band.facade.BandOfferFacade;
import cz.muni.fi.bandmanagementservice.band.model.BandDto;
import cz.muni.fi.bandmanagementservice.band.model.BandInfoUpdateRequest;
import cz.muni.fi.bandmanagementservice.band.model.BandOfferDto;
import cz.muni.fi.bandmanagementservice.band.restController.restController;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class RestControllerTest {

    @Mock
    private BandFacade bandFacade;

    @Mock
    private BandOfferFacade bandOfferFacade;

    @InjectMocks
    private restController controller;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testAcceptBandOffer_Success() {
        BandOfferDto offer = new BandOfferDto();
        when(bandOfferFacade.acceptBandOffer(1L)).thenReturn(offer);

        ResponseEntity<BandOfferDto> response = controller.acceptBandOffer(1L);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(offer, response.getBody());
    }

    @Test
    void testAcceptBandOffer_InvalidOperation() {
        when(bandOfferFacade.acceptBandOffer(1L)).thenThrow(new InvalidOperationException("Invalid operation"));

        ResponseEntity<BandOfferDto> response = controller.acceptBandOffer(1L);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    void testAcceptBandOffer_NotFound() {
        when(bandOfferFacade.acceptBandOffer(1L)).thenThrow(new ResourceNotFoundException("Not found"));

        ResponseEntity<BandOfferDto> response = controller.acceptBandOffer(1L);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

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
        when(bandFacade.getBand(1L)).thenThrow(new ResourceNotFoundException("Not found"));

        ResponseEntity<BandDto> response = controller.getBand(1L);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void testUpdateBand_Success() {
        BandDto updatedBand = new BandDto();
        BandInfoUpdateRequest updateRequest = new BandInfoUpdateRequest();
        when(bandFacade.updateBand(updateRequest)).thenReturn(updatedBand);

        ResponseEntity<BandDto> response = controller.updateBand(updateRequest);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(updatedBand, response.getBody());
    }

    @Test
    void testUpdateBand_NotFound() {
        BandInfoUpdateRequest updateRequest = new BandInfoUpdateRequest();
        when(bandFacade.updateBand(updateRequest)).thenThrow(new ResourceNotFoundException("Not found"));

        ResponseEntity<BandDto> response = controller.updateBand(updateRequest);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void testRevokeBandOffer_Success() {
        ResponseEntity<Void> response = controller.revokeBandOffer(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(bandOfferFacade, times(1)).revokeOffer(1L);
    }

    @Test
    void testRevokeBandOffer_NotFound() {
        doThrow(new ResourceNotFoundException("Not found"))
                .when(bandOfferFacade).revokeOffer(1L);

        ResponseEntity<Void> response = controller.revokeBandOffer(1L);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void testRevokeBandOffer_InvalidOperation() {
        doThrow(new InvalidOperationException("Invalid operation"))
                .when(bandOfferFacade).revokeOffer(1L);

        ResponseEntity<Void> response = controller.revokeBandOffer(1L);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }
}
