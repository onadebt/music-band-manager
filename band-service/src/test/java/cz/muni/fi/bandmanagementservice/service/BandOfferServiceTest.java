package cz.muni.fi.bandmanagementservice.service;

import cz.muni.fi.bandmanagementservice.TestDataFactory;
import cz.muni.fi.bandmanagementservice.exceptions.*;
import cz.muni.fi.bandmanagementservice.model.Band;
import cz.muni.fi.bandmanagementservice.model.BandOffer;
import cz.muni.fi.bandmanagementservice.repository.BandOfferRepository;
import cz.muni.fi.bandmanagementservice.repository.BandRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class BandOfferServiceTest {

    @Mock
    private BandOfferRepository bandOfferRepository;

    @Mock
    private BandRepository bandRepository;

    @InjectMocks
    private BandOfferService bandOfferService;

    @Test
    void getBandOffer_existingId_returnsBandOffer() {
        BandOffer offer = TestDataFactory.setUpBandOffer1();
        when(bandOfferRepository.findById(1L)).thenReturn(Optional.of(offer));

        BandOffer result = bandOfferService.getBandOffer(1L);

        assertEquals(offer, result);
    }

    @Test
    void getBandOffer_nonExistent_throwsException() {
        when(bandOfferRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(BandOfferNotFoundException.class, () -> bandOfferService.getBandOffer(99L));
    }

    @Test
    void createBandOffer_validOffer_savesSuccessfully() {
        Band band = TestDataFactory.setUpBand1();
        band.setMembers(new HashSet<>());
        BandOffer offer = TestDataFactory.setUpBandOffer1();

        when(bandRepository.findById(band.getId())).thenReturn(Optional.of(band));
        when(bandOfferRepository.findByBandIdAndInvitedMusicianId(offer.getInvitedMusicianId(), band.getId()))
                .thenReturn(Optional.empty());
        when(bandOfferRepository.save(any(BandOffer.class))).thenReturn(offer);

        BandOffer result = bandOfferService.createBandOffer(band.getId(), offer.getInvitedMusicianId(), band.getManagerId());

        assertEquals(offer, result);
    }

    @Test
    void createBandOffer_bandNotFound_throwsException() {
        when(bandRepository.findById(999L)).thenReturn(Optional.empty());

        assertThrows(BandNotFoundException.class, () -> bandOfferService.createBandOffer(999L, 1L, 2L));
    }

    @Test
    void createBandOffer_wrongManager_throwsException() {
        Band band = TestDataFactory.setUpBand1();
        when(bandRepository.findById(band.getId())).thenReturn(Optional.of(band));

        assertThrows(InvalidManagerException.class, () -> bandOfferService.createBandOffer(band.getId(), 2L, 999L));
    }

    @Test
    void createBandOffer_musicianAlreadyInBand_throwsException() {
        Band band = TestDataFactory.setUpBand1();
        Long existingMember = band.getMembers().iterator().next();
        when(bandRepository.findById(band.getId())).thenReturn(Optional.of(band));

        assertThrows(MusicianAlreadyInBandException.class, () -> bandOfferService.createBandOffer(band.getId(), existingMember, band.getManagerId()));
    }

    @Test
    void createBandOffer_pendingOfferExists_throwsException() {
        Band band = TestDataFactory.setUpBand1();
        BandOffer existingOffer = TestDataFactory.setUpBandOffer1();

        when(bandRepository.findById(band.getId())).thenReturn(Optional.of(band));
        when(bandOfferRepository.findByBandIdAndInvitedMusicianId(existingOffer.getInvitedMusicianId(), band.getId()))
                .thenReturn(Optional.of(existingOffer));

        assertThrows(BandOfferAlreadyExistsException.class, () -> bandOfferService.createBandOffer(
                band.getId(), existingOffer.getInvitedMusicianId(), band.getManagerId()
        ));
    }

    @Test
    void acceptOffer_validPendingOffer_acceptsAndAddsMember() {
        BandOffer offer = TestDataFactory.setUpBandOffer1();
        Band band = TestDataFactory.setUpBand1();

        when(bandOfferRepository.findById(offer.getId())).thenReturn(Optional.of(offer));
        when(bandOfferRepository.save(any(BandOffer.class))).thenReturn(offer);
        when(bandRepository.findById(offer.getBandId())).thenReturn(Optional.of(band));

        BandOffer result = bandOfferService.acceptOffer(offer.getId());

        assertEquals(offer, result);
        verify(bandRepository).save(band);
    }

    @Test
    void acceptOffer_offerIsNotPending_throwsException() {
        BandOffer offer = TestDataFactory.setUpBandOffer1();
        offer.rejectOffer();

        when(bandOfferRepository.findById(offer.getId())).thenReturn(Optional.of(offer));

        assertThrows(CannotManipulateOfferException.class, () -> bandOfferService.acceptOffer(offer.getId()));
    }

    @Test
    void acceptOffer_bandNotFoundDuringAccept_throwsException() {
        BandOffer offer = TestDataFactory.setUpBandOffer1();
        when(bandOfferRepository.findById(offer.getId())).thenReturn(Optional.of(offer));
        when(bandOfferRepository.save(any(BandOffer.class))).thenReturn(offer);
        when(bandRepository.findById(offer.getBandId())).thenReturn(Optional.empty());

        assertThrows(BandNotFoundException.class, () -> bandOfferService.acceptOffer(offer.getId()));
    }

    @Test
    void rejectOffer_validPendingOffer_updatesStatus() {
        BandOffer offer = TestDataFactory.setUpBandOffer1();

        when(bandOfferRepository.findById(offer.getId())).thenReturn(Optional.of(offer));
        when(bandOfferRepository.save(any())).thenReturn(offer);

        BandOffer result = bandOfferService.rejectOffer(offer.getId());

        assertEquals(offer, result);
        verify(bandOfferRepository).save(offer);
    }

    @Test
    void revokeOffer_pendingOffer_deletesSuccessfully() {
        BandOffer offer = TestDataFactory.setUpBandOffer1();
        when(bandOfferRepository.findById(offer.getId())).thenReturn(Optional.of(offer));

        bandOfferService.revokeOffer(offer.getId());

        verify(bandOfferRepository).delete(offer);
    }

    @Test
    void revokeOffer_offerNotPending_throwsException() {
        BandOffer offer = TestDataFactory.setUpBandOffer1();
        offer.acceptOffer();

        when(bandOfferRepository.findById(offer.getId())).thenReturn(Optional.of(offer));

        assertThrows(CannotManipulateOfferException.class, () -> bandOfferService.revokeOffer(offer.getId()));
    }

    @Test
    void getAllBandOffers_returnsList() {
        List<BandOffer> offers = List.of(TestDataFactory.setUpBandOffer1());
        when(bandOfferRepository.findAll()).thenReturn(offers);

        List<BandOffer> result = bandOfferService.getAllBandOffers();

        assertEquals(1, result.size());
    }

    @Test
    void getBandOffersByBandId_returnsList() {
        List<BandOffer> offers = List.of(TestDataFactory.setUpBandOffer1());
        when(bandOfferRepository.findByBandId(1L)).thenReturn(offers);

        List<BandOffer> result = bandOfferService.getBandOffersByBandId(1L);

        assertEquals(1, result.size());
    }

    @Test
    void getBandOfferByInvitedMusicianId_returnsList() {
        List<BandOffer> offers = List.of(TestDataFactory.setUpBandOffer1());
        when(bandOfferRepository.findByInvitedMusicianId(1L)).thenReturn(offers);

        List<BandOffer> result = bandOfferService.getBandOfferByInvitedMusicianId(1L);

        assertEquals(1, result.size());
    }
}
