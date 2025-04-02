package cz.muni.fi.bandmanagementservice.facade;

import cz.muni.fi.bandmanagementservice.mappers.BandOfferMapper;
import cz.muni.fi.bandmanagementservice.service.BandOfferService;
import cz.muni.fi.generated.band.model.BandDto;
import cz.muni.fi.generated.band.model.BandOfferDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

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
