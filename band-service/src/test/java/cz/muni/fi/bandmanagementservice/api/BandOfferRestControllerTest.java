package cz.muni.fi.bandmanagementservice.api;

import cz.muni.fi.bandmanagementservice.band.dto.BandOfferDto;
import cz.muni.fi.bandmanagementservice.band.exceptions.InvalidOperationException;
import cz.muni.fi.bandmanagementservice.band.exceptions.ResourceNotFoundException;
import cz.muni.fi.bandmanagementservice.band.facade.BandOfferFacade;
import cz.muni.fi.bandmanagementservice.band.rest.BandOfferRestController;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Created by Ivan Yatskiv, changed during refactor by Tomáš Marek
 */
@ExtendWith(MockitoExtension.class)
public class BandOfferRestControllerTest {
    @Mock
    BandOfferFacade bandOfferFacade;

    @InjectMocks
    BandOfferRestController controller;

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

