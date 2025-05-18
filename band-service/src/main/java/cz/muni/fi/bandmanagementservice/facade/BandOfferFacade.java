package cz.muni.fi.bandmanagementservice.facade;

import cz.muni.fi.bandmanagementservice.mappers.BandOfferMapper;
import cz.muni.fi.bandmanagementservice.service.BandOfferService;
import cz.muni.fi.bandmanagementservice.dto.BandOfferDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Tomáš MAREK
 */
@Component
public class BandOfferFacade {
    private final BandOfferService bandOfferService;
    private final BandOfferMapper bandOfferMapper;

    @Autowired
    public BandOfferFacade(BandOfferService bandOfferService, BandOfferMapper bandOfferMapper) {
        this.bandOfferService = bandOfferService;
        this.bandOfferMapper = bandOfferMapper;
    }



    public BandOfferDto getBandOffer(Long offerId) {
        return bandOfferMapper.toDto(bandOfferService.getBandOffer(offerId));
    }

    public BandOfferDto postBandOffer(Long bandId, Long invitedMusicianId, Long offeringManagerId) {
        return bandOfferMapper.toDto(bandOfferService.createBandOffer(bandId, invitedMusicianId, offeringManagerId));
    }

    public BandOfferDto acceptBandOffer(Long offerId) {
        return bandOfferMapper.toDto(bandOfferService.acceptOffer(offerId));
    }

    public BandOfferDto rejectBandOffer(Long offerId) {
        return bandOfferMapper.toDto(bandOfferService.rejectOffer(offerId));
    }

    public void revokeOffer(Long offerId){
        bandOfferService.revokeOffer(offerId);
    }

    public List<BandOfferDto> getAllBandOffers(){
        return bandOfferService.getAllBandOffers().stream().map(bandOfferMapper::toDto).collect(Collectors.toList());
    }

    public List<BandOfferDto> getBandOffersByBandId(Long bandId){
        return bandOfferService.getBandOffersByBandId(bandId).stream().map(bandOfferMapper::toDto).collect(Collectors.toList());
    }

    public List<BandOfferDto> getBandOffersByInvitedMusicianId(Long invitedMusicianId){
        return bandOfferService.getBandOfferByInvitedMusicianId(invitedMusicianId).stream().map(bandOfferMapper::toDto).collect(Collectors.toList());
    }


}
