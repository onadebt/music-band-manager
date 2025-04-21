package cz.muni.fi.bandmanagementservice.band.facade;

import cz.muni.fi.bandmanagementservice.band.mappers.BandOfferMapper;
import cz.muni.fi.bandmanagementservice.band.service.BandOfferService;
import cz.muni.fi.bandmanagementservice.band.dto.BandOfferDto;
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

    @Autowired
    public BandOfferFacade(BandOfferService bandOfferService) {
        this.bandOfferService = bandOfferService;
    }



    public BandOfferDto getBandOffer(Long offerId) {
        return BandOfferMapper.mapToDto(bandOfferService.getBandOffer(offerId));
    }

    public BandOfferDto postBandOffer(Long bandId, Long invitedMusicianId, Long offeringManagerId) {
        return BandOfferMapper.mapToDto(bandOfferService.createBandOffer(bandId, invitedMusicianId, offeringManagerId));
    }

    public BandOfferDto acceptBandOffer(Long offerId) {
        return BandOfferMapper.mapToDto(bandOfferService.acceptOffer(offerId));
    }

    public BandOfferDto rejectBandOffer(Long offerId) {
        return BandOfferMapper.mapToDto(bandOfferService.rejectOffer(offerId));
    }

    public void revokeOffer(Long offerId){
        bandOfferService.revokeOffer(offerId);
    }

    public List<BandOfferDto> getAllBandOffers(){
        return bandOfferService.getAllBandOffers().stream().map(BandOfferMapper::mapToDto).collect(Collectors.toList());
    }

    public List<BandOfferDto> getBandOffersByBandId(Long bandId){
        return bandOfferService.getBandOffersByBandId(bandId).stream().map(BandOfferMapper::mapToDto).collect(Collectors.toList());
    }

    public List<BandOfferDto> getBandOffersByInvitedMusicianId(Long invitedMusicianId){
        return bandOfferService.getBandOfferByInvitedMusicianId(invitedMusicianId).stream().map(BandOfferMapper::mapToDto).collect(Collectors.toList());
    }


}
