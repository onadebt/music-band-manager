package cz.muni.fi.bandmanagementservice.mappers;

import cz.muni.fi.bandmanagementservice.data.model.BandOffer;
import cz.muni.fi.bandmanagementservice.band.model.BandOfferDto;

/**
 * @author Tomáš MAREK
 */public class BandOfferMapper {
     public static BandOffer mapFromDto(BandOfferDto dto){
         return new BandOffer(dto.getId(), dto.getBandId(), dto.getInvitedMusicianId(), dto.getOfferingManagerId());
     }


    public static BandOfferDto mapToDto(BandOffer bandOffer){
         BandOfferDto dto = new BandOfferDto();
         dto.setId(bandOffer.getId());
         dto.setBandId(bandOffer.getBandId());
         dto.setInvitedMusicianId(bandOffer.getInvitedMusicianId());
         dto.setOfferingManagerId(bandOffer.getOfferingManagerId());
         return dto;
    }
}
