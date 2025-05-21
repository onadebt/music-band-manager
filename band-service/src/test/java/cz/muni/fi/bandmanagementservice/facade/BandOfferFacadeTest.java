package cz.muni.fi.bandmanagementservice.facade;

import cz.muni.fi.bandmanagementservice.TestDataFactory;
import cz.muni.fi.bandmanagementservice.dto.BandOfferDto;
import cz.muni.fi.bandmanagementservice.exceptions.BandNotFoundException;
import cz.muni.fi.bandmanagementservice.exceptions.BandOfferNotFoundException;
import cz.muni.fi.bandmanagementservice.mappers.BandOfferMapper;
import cz.muni.fi.bandmanagementservice.model.BandOffer;
import cz.muni.fi.bandmanagementservice.saga.BandOfferSaga;
import cz.muni.fi.bandmanagementservice.service.BandOfferService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class BandOfferFacadeTest {

    @Mock
    private BandOfferService bandOfferService;

    @Mock
    private BandOfferSaga bandOfferSaga;

    @Mock
    private BandOfferMapper bandOfferMapper;

    @InjectMocks
    private BandOfferFacade bandOfferFacade;

    @Test
    void getBandOffer_existingId_returnsBandOfferDto() {
        BandOffer offer = TestDataFactory.setUpBandOffer1();
        BandOfferDto dto = TestDataFactory.setUpBandOfferDto1();

        when(bandOfferService.getBandOffer(1L)).thenReturn(offer);
        when(bandOfferMapper.toDto(offer)).thenReturn(dto);

        BandOfferDto result = bandOfferFacade.getBandOffer(1L);

        assertEquals(dto, result);
    }

    @Test
    void getBandOffer_notFound_throwsException() {
        when(bandOfferService.getBandOffer(99L)).thenThrow(new BandOfferNotFoundException(99L));

        assertThrows(BandOfferNotFoundException.class, () -> bandOfferFacade.getBandOffer(99L));
    }

    @Test
    void postBandOffer_validInput_returnsDto() {
        BandOffer offer = TestDataFactory.setUpBandOffer1();
        BandOfferDto dto = TestDataFactory.setUpBandOfferDto1();

        when(bandOfferService.createBandOffer(1L, 2L, 3L)).thenReturn(offer);
        when(bandOfferMapper.toDto(offer)).thenReturn(dto);

        BandOfferDto result = bandOfferFacade.postBandOffer(1L, 2L, 3L);

        assertEquals(dto, result);
    }

    @Test
    void postBandOffer_bandNotFound_throwsException() {
        when(bandOfferService.createBandOffer(1L, 2L, 3L)).thenThrow(new BandNotFoundException(1L));

        assertThrows(BandNotFoundException.class, () -> bandOfferFacade.postBandOffer(1L, 2L, 3L));
    }

    @Test
    void acceptBandOffer_validOffer_returnsUpdatedDto() {
        BandOffer offer = TestDataFactory.setUpBandOffer1();
        BandOfferDto dto = TestDataFactory.setUpBandOfferDto1();

        when(bandOfferSaga.startAcceptBandOffer(1L)).thenReturn(offer);
        when(bandOfferMapper.toDto(offer)).thenReturn(dto);

        BandOfferDto result = bandOfferFacade.acceptBandOffer(1L);

        assertEquals(dto, result);
    }

    @Test
    void acceptBandOffer_invalidId_throwsException() {
        when(bandOfferSaga.startAcceptBandOffer(99L)).thenThrow(new BandOfferNotFoundException(99L));

        assertThrows(BandOfferNotFoundException.class, () -> bandOfferFacade.acceptBandOffer(99L));
    }

    @Test
    void rejectBandOffer_validOffer_rejectsSuccessfully() {
        BandOffer offer = TestDataFactory.setUpBandOffer1();
        BandOfferDto dto = TestDataFactory.setUpBandOfferDto1();

        when(bandOfferService.rejectOffer(1L)).thenReturn(offer);
        when(bandOfferMapper.toDto(offer)).thenReturn(dto);

        BandOfferDto result = bandOfferFacade.rejectBandOffer(1L);

        assertEquals(dto, result);
    }

    @Test
    void revokeOffer_validOffer_executesSuccessfully() {
        doNothing().when(bandOfferService).revokeOffer(1L);

        assertDoesNotThrow(() -> bandOfferFacade.revokeOffer(1L));
        verify(bandOfferService).revokeOffer(1L);
    }

    @Test
    void getAllBandOffers_returnsList() {
        List<BandOffer> offers = List.of(TestDataFactory.setUpBandOffer1(), TestDataFactory.setUpBandOffer2());
        List<BandOfferDto> dtos = List.of(TestDataFactory.setUpBandOfferDto1(), TestDataFactory.setUpBandOfferDto2());

        when(bandOfferService.getAllBandOffers()).thenReturn(offers);
        when(bandOfferMapper.toDto(any())).thenReturn(dtos.get(0), dtos.get(1));

        List<BandOfferDto> result = bandOfferFacade.getAllBandOffers();

        assertEquals(2, result.size());
        verify(bandOfferMapper, times(2)).toDto(any());
    }

    @Test
    void getBandOffersByBandId_validId_returnsFilteredOffers() {
        when(bandOfferService.getBandOffersByBandId(1L)).thenReturn(List.of(TestDataFactory.setUpBandOffer1()));
        when(bandOfferMapper.toDto(any())).thenReturn(TestDataFactory.setUpBandOfferDto1());

        List<BandOfferDto> result = bandOfferFacade.getBandOffersByBandId(1L);

        assertEquals(1, result.size());
    }

    @Test
    void getBandOffersByInvitedMusicianId_validId_returnsFilteredOffers() {
        when(bandOfferService.getBandOfferByInvitedMusicianId(1L)).thenReturn(List.of(TestDataFactory.setUpBandOffer1()));
        when(bandOfferMapper.toDto(any())).thenReturn(TestDataFactory.setUpBandOfferDto1());

        List<BandOfferDto> result = bandOfferFacade.getBandOffersByInvitedMusicianId(1L);

        assertEquals(1, result.size());
    }
}
