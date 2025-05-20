package cz.muni.fi.bandmanagementservice.mappers;

import cz.muni.fi.bandmanagementservice.dto.BandInfoUpdateDto;
import cz.muni.fi.bandmanagementservice.exceptions.BandNotFoundException;
import cz.muni.fi.bandmanagementservice.model.Band;
import cz.muni.fi.bandmanagementservice.dto.BandDto;
import org.mapstruct.Mapper;

/**
 * @author Tomáš MAREK
 */
@Mapper(componentModel = "spring")
public interface BandMapper {
    BandDto toDto(Band band);
    Band toEntity(BandInfoUpdateDto bandInfoUpdateDto);
}
