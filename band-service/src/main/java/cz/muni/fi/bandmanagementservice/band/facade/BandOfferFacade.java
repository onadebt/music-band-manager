package cz.muni.fi.bandmanagementservice.band.facade;

import cz.muni.fi.bandmanagementservice.band.mappers.BandOfferMapper;
import cz.muni.fi.bandmanagementservice.band.service.BandOfferService;
import cz.muni.fi.bandmanagementservice.band.model.BandOfferDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author Tomáš MAREK
 */
@Component
public class BandOfferFacade {
    private final BandOfferService bandOfferService;

    @Autowired
    public BandOfferFacade(BandOfferService bandOfferService) {
        this.bandOfferService = bandOfferService;
    }

    public BandOfferDto acceptBandOffer(Long offerId) {
        bandOfferService.acceptOffer(offerId);
        return BandOfferMapper.mapToDto(bandOfferService.getBandOffer(offerId));
    }

    public BandOfferDto getBandOffer(Long offerId) {
        return BandOfferMapper.mapToDto(bandOfferService.getBandOffer(offerId));
    }

    public BandOfferDto postBandOffer(Long bandId, Long invitedMusicianId, Long offeringManagerId) {
        return BandOfferMapper.mapToDto(bandOfferService.createBandOffer(bandId, invitedMusicianId, offeringManagerId));
    }

    public BandOfferDto rejectBandOffer(Long offerId) {
        bandOfferService.rejectOffer(offerId);
        return BandOfferMapper.mapToDto(bandOfferService.getBandOffer(offerId));
    }
}
