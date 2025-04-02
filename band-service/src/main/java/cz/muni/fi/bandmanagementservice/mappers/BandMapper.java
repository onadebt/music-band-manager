package cz.muni.fi.bandmanagementservice.mappers;

import cz.muni.fi.bandmanagementservice.data.model.Band;
import cz.muni.fi.bandmanagementservice.data.model.BandInfoUpdate;
import cz.muni.fi.bandmanagementservice.band.model.BandDto;
import cz.muni.fi.bandmanagementservice.band.model.BandInfoUpdateRequest;

/**
 * @author Tomáš MAREK
 */
public class BandMapper {
    public static BandDto mapToDto(Band band){
        BandDto dto = new BandDto();
        dto.id(band.getId());
        dto.name(band.getName());
        dto.musicalStyle(band.getMusicalStyle());
        dto.managerId(band.getManagerId());
        dto.logo(band.getLogoUrl());
        dto.members(band.getMembers().stream().toList());
        return dto;
    };

    public static Band mapFromDto(BandDto bandDto){
        Band band = new Band(bandDto.getId(), bandDto.getName(), bandDto.getMusicalStyle(), bandDto.getManagerId());
        band.setLogoUrl(bandDto.getLogo().get());
        band.setMembers(bandDto.getMembers());
        return band;
    };

    public static BandInfoUpdate mapFromInfoUpdateRequest(BandInfoUpdateRequest updateRequest){
        return new BandInfoUpdate(updateRequest.getId(), updateRequest.getName(), updateRequest.getMusicalStyle(), updateRequest.getManagerId(), updateRequest.getLogo().get());
    };

}
