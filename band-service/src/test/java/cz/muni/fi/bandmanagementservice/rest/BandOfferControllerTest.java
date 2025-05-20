package cz.muni.fi.bandmanagementservice.rest;

import cz.muni.fi.bandmanagementservice.dto.BandOfferDto;
import cz.muni.fi.bandmanagementservice.exceptions.BandOfferNotFoundException;
import cz.muni.fi.bandmanagementservice.exceptions.CannotManipulateOfferException;
import cz.muni.fi.bandmanagementservice.facade.BandOfferFacade;
import cz.muni.fi.shared.enm.BandOfferStatus;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Created by Ivan Yatskiv, changed during refactor by Tomáš Marek
 */
@ExtendWith(MockitoExtension.class)
public class BandOfferControllerTest {
    @Mock
    BandOfferFacade bandOfferFacade;

    @InjectMocks
    BandOfferController controller;

    @Test
    void testGetBandOffer_Success() {
        BandOfferDto offer = new BandOfferDto();
        when(bandOfferFacade.getBandOffer(1L)).thenReturn(offer);

        ResponseEntity<BandOfferDto> response = controller.getBandOffer(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(offer, response.getBody());
    }

    @Test
    void testGetBandOffer_NotFound() {
        when(bandOfferFacade.getBandOffer(1L)).thenThrow(new BandOfferNotFoundException(1L));

        assertThrows(BandOfferNotFoundException.class, () -> controller.getBandOffer(1L));
    }

    @Test
    void testCreateBandOffer_Success() {
        BandOfferDto offer = new BandOfferDto();
        when(bandOfferFacade.postBandOffer(1L, 2L, 3L)).thenReturn(offer);

        ResponseEntity<BandOfferDto> response = controller.createBandOffer(1L, 2L, 3L);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(offer, response.getBody());
    }

    @Test
    void testAcceptBandOffer_Success() {
        BandOfferDto offer = new BandOfferDto();
        when(bandOfferFacade.acceptBandOffer(1L)).thenReturn(offer);

        ResponseEntity<BandOfferDto> response = controller.acceptBandOffer(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(offer, response.getBody());
    }

    @Test
    void testAcceptBandOffer_InvalidOperation() {
        when(bandOfferFacade.acceptBandOffer(1L)).thenThrow(new CannotManipulateOfferException(BandOfferStatus.ACCEPTED));

        assertThrows(CannotManipulateOfferException.class, () -> controller.acceptBandOffer(1L));
    }

    @Test
    void testAcceptBandOffer_NotFound() {
        when(bandOfferFacade.acceptBandOffer(1L)).thenThrow(new BandOfferNotFoundException(1L));

        assertThrows(BandOfferNotFoundException.class, () -> controller.acceptBandOffer(1L));
    }


    @Test
    void testRevokeBandOffer_Success() {
        ResponseEntity<Void> response = controller.revokeBandOffer(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(bandOfferFacade, times(1)).revokeOffer(1L);
    }

    @Test
    void testRevokeBandOffer_NotFound() {
        doThrow(new BandOfferNotFoundException(1L))
                .when(bandOfferFacade).revokeOffer(1L);

        assertThrows(BandOfferNotFoundException.class, () -> controller.revokeBandOffer(1L));
    }

    @Test
    void testRevokeBandOffer_InvalidOperation() {
        doThrow(new CannotManipulateOfferException(BandOfferStatus.REJECTED))
                .when(bandOfferFacade).revokeOffer(1L);

        assertThrows(CannotManipulateOfferException.class, () -> controller.revokeBandOffer(1L));
    }
}

