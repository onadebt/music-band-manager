package cz.muni.fi.bandmanagementservice.band.mappers;

import cz.muni.fi.bandmanagementservice.band.model.BandOffer;
import cz.muni.fi.bandmanagementservice.band.model.BandOfferStatus;
import cz.muni.fi.bandmanagementservice.band.dto.BandOfferDto;

/**
 * @author Tomáš MAREK
 */public class BandOfferMapper {
     public static BandOffer mapFromDto(BandOfferDto dto) {
         BandOffer offer = new BandOffer(dto.getId(), dto.getBandId(), dto.getInvitedMusicianId(), dto.getOfferingManagerId());
         assert dto.getStatus() != null;
         offer.setStatus(mapStatusFromDto(dto.getStatus()));
         return offer;
     }


    public static BandOfferDto mapToDto(BandOffer bandOffer){
         BandOfferDto dto = new BandOfferDto();
         dto.setId(bandOffer.getId());
         dto.setBandId(bandOffer.getBandId());
         dto.setInvitedMusicianId(bandOffer.getInvitedMusicianId());
         dto.setOfferingManagerId(bandOffer.getOfferingManagerId());
         dto.setStatus(mapStatusToDto(bandOffer.getStatus()));
         return dto;
    }

    private static BandOfferDto.StatusEnum mapStatusToDto(BandOfferStatus status){
        return switch (status) {
            case PENDING -> BandOfferDto.StatusEnum.PENDING;
            case ACCEPTED -> BandOfferDto.StatusEnum.ACCEPTED;
            case REJECTED -> BandOfferDto.StatusEnum.REJECTED;
        };
    }

    private static BandOfferStatus mapStatusFromDto(BandOfferDto.StatusEnum status){
         return switch (status) {
             case PENDING -> BandOfferStatus.PENDING;
             case ACCEPTED -> BandOfferStatus.ACCEPTED;
             case REJECTED -> BandOfferStatus.REJECTED;
         };
    }
}
