package cz.muni.fi.bandmanagementservice.mappers;

import cz.muni.fi.bandmanagementservice.model.BandOffer;
import cz.muni.fi.shared.enm.BandOfferStatus;
import cz.muni.fi.bandmanagementservice.dto.BandOfferDto;

/**
 * @author Tomáš MAREK
 */public class BandOfferMapper {
     public static BandOffer mapFromDto(BandOfferDto dto) {
         BandOffer offer = new BandOffer(dto.getId(), dto.getBandId(), dto.getInvitedMusicianId(), dto.getOfferingManagerId());
         assert dto.getStatus() != null;
         offer.setStatus(dto.getStatus());
         return offer;
     }


    public static BandOfferDto mapToDto(BandOffer bandOffer){
         BandOfferDto dto = new BandOfferDto();
         dto.setId(bandOffer.getId());
         dto.setBandId(bandOffer.getBandId());
         dto.setInvitedMusicianId(bandOffer.getInvitedMusicianId());
         dto.setOfferingManagerId(bandOffer.getOfferingManagerId());
         dto.setStatus(bandOffer.getStatus());
         return dto;
    }


}
