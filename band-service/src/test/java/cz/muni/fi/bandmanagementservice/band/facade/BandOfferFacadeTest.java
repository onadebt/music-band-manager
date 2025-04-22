package cz.muni.fi.bandmanagementservice.band.facade;

import cz.muni.fi.bandmanagementservice.band.dto.BandOfferDto;
import cz.muni.fi.bandmanagementservice.band.model.BandOffer;
import cz.muni.fi.bandmanagementservice.band.mappers.BandOfferMapper;
import cz.muni.fi.bandmanagementservice.band.service.BandOfferService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class BandOfferFacadeTest {

    private BandOfferService bandOfferService;
    private BandOfferFacade bandOfferFacade;

    private BandOffer bandOffer;
    private BandOfferDto bandOfferDto;

    @BeforeEach
    void setUp() {
        bandOfferService = mock(BandOfferService.class);
        bandOfferFacade = new BandOfferFacade(bandOfferService);

        bandOffer = new BandOffer();
        bandOfferDto = new BandOfferDto();
    }

    @Test
    void getBandOffer_shouldReturnMappedDto() {
        Long offerId = 1L;

        when(bandOfferService.getBandOffer(offerId)).thenReturn(bandOffer);

        try (MockedStatic<BandOfferMapper> mocked = mockStatic(BandOfferMapper.class)) {
            mocked.when(() -> BandOfferMapper.mapToDto(bandOffer)).thenReturn(bandOfferDto);

            BandOfferDto result = bandOfferFacade.getBandOffer(offerId);

            assertEquals(bandOfferDto, result);
            verify(bandOfferService).getBandOffer(offerId);
        }
    }

    @Test
    void postBandOffer_shouldReturnCreatedOfferDto() {
        Long bandId = 1L, musicianId = 2L, managerId = 3L;

        when(bandOfferService.createBandOffer(bandId, musicianId, managerId)).thenReturn(bandOffer);

        try (MockedStatic<BandOfferMapper> mocked = mockStatic(BandOfferMapper.class)) {
            mocked.when(() -> BandOfferMapper.mapToDto(bandOffer)).thenReturn(bandOfferDto);

            BandOfferDto result = bandOfferFacade.postBandOffer(bandId, musicianId, managerId);

            assertEquals(bandOfferDto, result);
            verify(bandOfferService).createBandOffer(bandId, musicianId, managerId);
        }
    }

    @Test
    void acceptBandOffer_shouldReturnAcceptedOfferDto() {
        Long offerId = 1L;

        when(bandOfferService.acceptOffer(offerId)).thenReturn(bandOffer);

        try (MockedStatic<BandOfferMapper> mocked = mockStatic(BandOfferMapper.class)) {
            mocked.when(() -> BandOfferMapper.mapToDto(bandOffer)).thenReturn(bandOfferDto);

            BandOfferDto result = bandOfferFacade.acceptBandOffer(offerId);

            assertEquals(bandOfferDto, result);
            verify(bandOfferService).acceptOffer(offerId);
        }
    }

    @Test
    void rejectBandOffer_shouldReturnRejectedOfferDto() {
        Long offerId = 1L;

        when(bandOfferService.rejectOffer(offerId)).thenReturn(bandOffer);

        try (MockedStatic<BandOfferMapper> mocked = mockStatic(BandOfferMapper.class)) {
            mocked.when(() -> BandOfferMapper.mapToDto(bandOffer)).thenReturn(bandOfferDto);

            BandOfferDto result = bandOfferFacade.rejectBandOffer(offerId);

            assertEquals(bandOfferDto, result);
            verify(bandOfferService).rejectOffer(offerId);
        }
    }

    @Test
    void revokeOffer_shouldCallService() {
        Long offerId = 1L;

        bandOfferFacade.revokeOffer(offerId);

        verify(bandOfferService).revokeOffer(offerId);
    }

    @Test
    void getAllBandOffers_shouldReturnMappedList() {
        List<BandOffer> offers = Arrays.asList(bandOffer, bandOffer);
        List<BandOfferDto> dtos = Arrays.asList(bandOfferDto, bandOfferDto);

        when(bandOfferService.getAllBandOffers()).thenReturn(offers);

        try (MockedStatic<BandOfferMapper> mocked = mockStatic(BandOfferMapper.class)) {
            mocked.when(() -> BandOfferMapper.mapToDto(any(BandOffer.class))).thenReturn(bandOfferDto);

            List<BandOfferDto> result = bandOfferFacade.getAllBandOffers();

            assertEquals(dtos.size(), result.size());
            verify(bandOfferService).getAllBandOffers();
        }
    }

    @Test
    void getBandOffersByBandId_shouldReturnMappedList() {
        Long bandId = 5L;
        when(bandOfferService.getBandOffersByBandId(bandId)).thenReturn(List.of(bandOffer));

        try (MockedStatic<BandOfferMapper> mocked = mockStatic(BandOfferMapper.class)) {
            mocked.when(() -> BandOfferMapper.mapToDto(bandOffer)).thenReturn(bandOfferDto);

            List<BandOfferDto> result = bandOfferFacade.getBandOffersByBandId(bandId);

            assertEquals(1, result.size());
            verify(bandOfferService).getBandOffersByBandId(bandId);
        }
    }

    @Test
    void getBandOffersByInvitedMusicianId_shouldReturnMappedList() {
        Long musicianId = 2L;
        when(bandOfferService.getBandOfferByInvitedMusicianId(musicianId)).thenReturn(List.of(bandOffer));

        try (MockedStatic<BandOfferMapper> mocked = mockStatic(BandOfferMapper.class)) {
            mocked.when(() -> BandOfferMapper.mapToDto(bandOffer)).thenReturn(bandOfferDto);

            List<BandOfferDto> result = bandOfferFacade.getBandOffersByInvitedMusicianId(musicianId);

            assertEquals(1, result.size());
            verify(bandOfferService).getBandOfferByInvitedMusicianId(musicianId);
        }
    }
}
