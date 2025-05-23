package cz.muni.fi.bandmanagementservice.facade;

import cz.muni.fi.bandmanagementservice.dto.BandOfferDto;
import cz.muni.fi.bandmanagementservice.mapper.BandOfferMapper;
import cz.muni.fi.bandmanagementservice.saga.BandOfferSaga;
import cz.muni.fi.bandmanagementservice.service.BandOfferService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Tomáš MAREK
 */
@Service
@Transactional
public class BandOfferFacade {
    private final BandOfferService bandOfferService;
    private final BandOfferSaga bandOfferSaga;
    private final BandOfferMapper bandOfferMapper;

    @Autowired
    public BandOfferFacade(BandOfferService bandOfferService, BandOfferSaga bandOfferSaga, BandOfferMapper bandOfferMapper) {
        this.bandOfferService = bandOfferService;
        this.bandOfferSaga = bandOfferSaga;
        this.bandOfferMapper = bandOfferMapper;
    }


    @Transactional(readOnly = true)
    public BandOfferDto getBandOffer(Long offerId) {
        return bandOfferMapper.toDto(bandOfferService.getBandOffer(offerId));
    }

    public BandOfferDto postBandOffer(Long bandId, Long invitedMusicianId, Long offeringManagerId) {
        return bandOfferMapper.toDto(bandOfferService.createBandOffer(bandId, invitedMusicianId, offeringManagerId));
    }

    public BandOfferDto acceptBandOffer(Long offerId) {
        return bandOfferMapper.toDto(bandOfferSaga.startAcceptBandOffer(offerId));
    }

    public BandOfferDto rejectBandOffer(Long offerId) {
        return bandOfferMapper.toDto(bandOfferService.rejectOffer(offerId));
    }

    public void revokeOffer(Long offerId) {
        bandOfferService.revokeOffer(offerId);
    }

    @Transactional(readOnly = true)
    public List<BandOfferDto> getAllBandOffers() {
        return bandOfferService.getAllBandOffers().stream().map(bandOfferMapper::toDto).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<BandOfferDto> getBandOffersByBandId(Long bandId) {
        return bandOfferService.getBandOffersByBandId(bandId).stream().map(bandOfferMapper::toDto).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<BandOfferDto> getBandOffersByInvitedMusicianId(Long invitedMusicianId) {
        return bandOfferService.getBandOfferByInvitedMusicianId(invitedMusicianId).stream().map(bandOfferMapper::toDto).collect(Collectors.toList());
    }
}
