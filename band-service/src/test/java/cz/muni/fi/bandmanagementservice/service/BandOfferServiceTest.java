package cz.muni.fi.bandmanagementservice.service;

import cz.muni.fi.bandmanagementservice.exceptions.InvalidOperationException;
import cz.muni.fi.bandmanagementservice.exceptions.ResourceNotFoundException;
import cz.muni.fi.bandmanagementservice.model.Band;
import cz.muni.fi.bandmanagementservice.model.BandOffer;
import cz.muni.fi.shared.enm.BandOfferStatus;
import cz.muni.fi.bandmanagementservice.repository.BandOfferRepository;
import cz.muni.fi.bandmanagementservice.repository.BandRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class BandOfferServiceTest {

    @Mock
    private BandOfferRepository bandOfferRepository;

    @Mock
    private BandRepository bandRepository;

    @InjectMocks
    private BandOfferService bandOfferService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetBandOffer_ExistingOffer() {
        BandOffer bandOffer = new BandOffer(1L, 1L, 2L, 3L);
        when(bandOfferRepository.findById(1L)).thenReturn(Optional.of(bandOffer));

        BandOffer result = bandOfferService.getBandOffer(1L);

        assertEquals(bandOffer, result);
        verify(bandOfferRepository).findById(1L);
    }

    @Test
    void testGetBandOffer_NonExistingOffer() {
        when(bandOfferRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> bandOfferService.getBandOffer(1L));
        verify(bandOfferRepository).findById(1L);
    }

    @Test
    void testCreateBandOffer_Success() {
        Band band = new Band(1L, "Test Band", "Rock", 3L);
        when(bandRepository.findById(1L)).thenReturn(Optional.of(band));
        when(bandOfferRepository.findByBandIdAndInvitedMusicianId(2L, 1L)).thenReturn(Optional.empty());
        BandOffer newOffer = new BandOffer(null, 1L, 2L, 3L);
        when(bandOfferRepository.save(any(BandOffer.class))).thenReturn(newOffer);

        BandOffer result = bandOfferService.createBandOffer(1L, 2L, 3L);

        assertNotNull(result);
        assertEquals(1L, result.getBandId());
        assertEquals(2L, result.getInvitedMusicianId());
        assertEquals(3L, result.getOfferingManagerId());
        verify(bandRepository).findById(1L);
//        verify(bandOfferRepository).findByBandIdAndInvitedMusicianId(2L, 1L);
        verify(bandOfferRepository).save(any(BandOffer.class));
    }

    @Test
    void testCreateBandOffer_InvalidManager() {
        Band band = new Band(1L, "Test Band", "Jazz", 4L);
        when(bandRepository.findById(1L)).thenReturn(Optional.of(band));

        assertThrows(InvalidOperationException.class, () -> bandOfferService.createBandOffer(1L, 2L, 3L));
        verify(bandRepository).findById(1L);
    }

    @Test
    void testAcceptOffer_Success() {
        BandOffer bandOffer = new BandOffer(1L, 1L, 2L, 3L);
        bandOffer.setStatus(BandOfferStatus.PENDING);
        Band band = new Band(1L, "Test Band", "Pop", 3L);

        when(bandOfferRepository.findById(1L)).thenReturn(Optional.of(bandOffer));
        when(bandRepository.findById(1L)).thenReturn(Optional.of(band));
        when(bandOfferRepository.save(any(BandOffer.class))).thenReturn(bandOffer);

        BandOffer result = bandOfferService.acceptOffer(1L);

        assertEquals(BandOfferStatus.ACCEPTED, result.getStatus());
        assertTrue(band.getMembers().contains(2L));
        verify(bandOfferRepository).findById(1L);
        verify(bandRepository).findById(1L);
        verify(bandOfferRepository).save(bandOffer);
        verify(bandRepository).save(band);
    }

    @Test
    void testRejectOffer_Success() {
        BandOffer bandOffer = new BandOffer(1L, 1L, 2L, 3L);
        bandOffer.setStatus(BandOfferStatus.PENDING);

        when(bandOfferRepository.findById(1L)).thenReturn(Optional.of(bandOffer));
        when(bandOfferRepository.save(any(BandOffer.class))).thenReturn(bandOffer);

        BandOffer result = bandOfferService.rejectOffer(1L);

        assertEquals(BandOfferStatus.REJECTED, result.getStatus());
        verify(bandOfferRepository).findById(1L);
        verify(bandOfferRepository).save(bandOffer);
    }

    @Test
    void testRevokeOffer_Success() {
        BandOffer bandOffer = new BandOffer(1L, 1L, 2L, 3L);
        bandOffer.setStatus(BandOfferStatus.PENDING);

        when(bandOfferRepository.findById(1L)).thenReturn(Optional.of(bandOffer));

        bandOfferService.revokeOffer(1L);

        verify(bandOfferRepository).findById(1L);
        verify(bandOfferRepository).delete(bandOffer);
    }

    @Test
    void testRevokeOffer_InvalidStatus() {
        BandOffer bandOffer = new BandOffer(1L, 1L, 2L, 3L);
        bandOffer.setStatus(BandOfferStatus.ACCEPTED);

        when(bandOfferRepository.findById(1L)).thenReturn(Optional.of(bandOffer));

        assertThrows(InvalidOperationException.class, () -> bandOfferService.revokeOffer(1L));
        verify(bandOfferRepository).findById(1L);
    }
}
