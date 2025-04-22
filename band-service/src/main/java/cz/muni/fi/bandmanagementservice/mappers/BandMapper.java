package cz.muni.fi.bandmanagementservice.mappers;

import cz.muni.fi.bandmanagementservice.model.Band;
import cz.muni.fi.bandmanagementservice.model.BandInfoUpdate;
import cz.muni.fi.bandmanagementservice.dto.BandDto;
import cz.muni.fi.bandmanagementservice.dto.BandInfoUpdateRequest;

import java.util.HashSet;

/**
 * @author Tomáš MAREK
 */
public class BandMapper {
    public static BandDto mapToDto(Band band){
        BandDto dto = new BandDto();
        dto.setId(band.getId());
        dto.setName(band.getName());
        dto.setMusicalStyle(band.getMusicalStyle());
        dto.setManagerId(band.getManagerId());
        dto.setLogo(band.getLogoUrl());
        dto.setMembers(new HashSet<>(band.getMembers()));
        return dto;
    }

    public static Band mapFromDto(BandDto bandDto){
        Band band = new Band(bandDto.getId(), bandDto.getName(), bandDto.getMusicalStyle(), bandDto.getManagerId());
        band.setLogoUrl(bandDto.getLogo());
        band.setMembers(new HashSet<>(bandDto.getMembers()));
        return band;
    }

    public static BandInfoUpdate mapFromInfoUpdateRequest(BandInfoUpdateRequest updateRequest){
        return new BandInfoUpdate(updateRequest.getId(), updateRequest.getName(), updateRequest.getMusicalStyle(), updateRequest.getManagerId(), updateRequest.getLogo());
    }

}
