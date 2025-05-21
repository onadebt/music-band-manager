package cz.muni.fi.bandmanagementservice.rest;

import cz.muni.fi.bandmanagementservice.TestDataFactory;
import cz.muni.fi.bandmanagementservice.dto.BandOfferDto;
import cz.muni.fi.bandmanagementservice.exception.BandOfferNotFoundException;
import cz.muni.fi.bandmanagementservice.exception.CannotManipulateOfferException;
import cz.muni.fi.bandmanagementservice.facade.BandOfferFacade;
import cz.muni.fi.enums.BandOfferStatus;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doThrow;
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
    BandOfferController bandOfferController;

    @Test
    void getBandOffer_validId_returnsOfferDto() {
        BandOfferDto dto = TestDataFactory.setUpBandOfferDto1();
        when(bandOfferFacade.getBandOffer(1L)).thenReturn(dto);

        ResponseEntity<BandOfferDto> response = bandOfferController.getBandOffer(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(dto, response.getBody());
    }

    @Test
    void getBandOffer_invalidId_throwsNotFoundException() {
        when(bandOfferFacade.getBandOffer(1L)).thenThrow(new BandOfferNotFoundException(1L));

        assertThrows(BandOfferNotFoundException.class, () -> bandOfferController.getBandOffer(1L));
    }

    @Test
    void createBandOffer_validRequest_returnsCreatedDto() {
        BandOfferDto dto = TestDataFactory.setUpBandOfferDto1();
        when(bandOfferFacade.postBandOffer(1L, 2L, 3L)).thenReturn(dto);

        ResponseEntity<BandOfferDto> response = bandOfferController.createBandOffer(1L, 2L, 3L);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(dto, response.getBody());
    }

    @Test
    void acceptBandOffer_validId_returnsAcceptedDto() {
        BandOfferDto dto = TestDataFactory.setUpBandOfferDto1();
        when(bandOfferFacade.acceptBandOffer(1L)).thenReturn(dto);

        ResponseEntity<BandOfferDto> response = bandOfferController.acceptBandOffer(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(dto, response.getBody());
    }

    @Test
    void acceptBandOffer_alreadyAccepted_throwsCannotManipulateException() {
        when(bandOfferFacade.acceptBandOffer(1L)).thenThrow(new CannotManipulateOfferException(BandOfferStatus.ACCEPTED));

        assertThrows(CannotManipulateOfferException.class, () -> bandOfferController.acceptBandOffer(1L));
    }

    @Test
    void acceptBandOffer_notFound_throwsNotFoundException() {
        when(bandOfferFacade.acceptBandOffer(1L)).thenThrow(new BandOfferNotFoundException(1L));

        assertThrows(BandOfferNotFoundException.class, () -> bandOfferController.acceptBandOffer(1L));
    }

    @Test
    void rejectBandOffer_validId_returnsRejectedDto() {
        BandOfferDto dto = TestDataFactory.setUpBandOfferDto1();
        when(bandOfferFacade.rejectBandOffer(1L)).thenReturn(dto);

        ResponseEntity<BandOfferDto> response = bandOfferController.rejectBandOffer(1L);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(dto, response.getBody());
    }

    @Test
    void rejectBandOffer_invalidState_throwsCannotManipulateException() {
        when(bandOfferFacade.rejectBandOffer(1L)).thenThrow(new CannotManipulateOfferException(BandOfferStatus.ACCEPTED));

        assertThrows(CannotManipulateOfferException.class, () -> bandOfferController.rejectBandOffer(1L));
    }

    @Test
    void rejectBandOffer_notFound_throwsException() {
        when(bandOfferFacade.rejectBandOffer(1L)).thenThrow(new BandOfferNotFoundException(1L));

        assertThrows(BandOfferNotFoundException.class, () -> bandOfferController.rejectBandOffer(1L));
    }

    @Test
    void revokeBandOffer_validOffer_executesSuccessfully() {
        ResponseEntity<Void> response = bandOfferController.revokeBandOffer(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(bandOfferFacade).revokeOffer(1L);
    }

    @Test
    void revokeBandOffer_notFound_throwsException() {
        doThrow(new BandOfferNotFoundException(1L)).when(bandOfferFacade).revokeOffer(1L);

        assertThrows(BandOfferNotFoundException.class, () -> bandOfferController.revokeBandOffer(1L));
    }

    @Test
    void revokeBandOffer_invalidState_throwsCannotManipulateException() {
        doThrow(new CannotManipulateOfferException(BandOfferStatus.REJECTED)).when(bandOfferFacade).revokeOffer(1L);

        assertThrows(CannotManipulateOfferException.class, () -> bandOfferController.revokeBandOffer(1L));
    }

    @Test
    void getAllBandOffers_returnsListOfOffers() {
        List<BandOfferDto> offers = List.of(TestDataFactory.setUpBandOfferDto1(), TestDataFactory.setUpBandOfferDto2());
        when(bandOfferFacade.getAllBandOffers()).thenReturn(offers);

        ResponseEntity<List<BandOfferDto>> response = bandOfferController.getAllBandOffers();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        Assertions.assertNotNull(response.getBody());
        assertEquals(2, response.getBody().size());
    }

    @Test
    void getBandOffersByBand_validId_returnsFilteredList() {
        List<BandOfferDto> offers = List.of(TestDataFactory.setUpBandOfferDto1());
        when(bandOfferFacade.getBandOffersByBandId(1L)).thenReturn(offers);

        ResponseEntity<List<BandOfferDto>> response = bandOfferController.getBandOffersByBand(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        Assertions.assertNotNull(response.getBody());
        assertEquals(1, response.getBody().size());
    }

    @Test
    void getBandOffersByMusician_validId_returnsFilteredList() {
        List<BandOfferDto> offers = List.of(TestDataFactory.setUpBandOfferDto1());
        when(bandOfferFacade.getBandOffersByInvitedMusicianId(1L)).thenReturn(offers);

        ResponseEntity<List<BandOfferDto>> response = bandOfferController.getBandOffersByMusician(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        Assertions.assertNotNull(response.getBody());
        assertEquals(1, response.getBody().size());
    }
}


