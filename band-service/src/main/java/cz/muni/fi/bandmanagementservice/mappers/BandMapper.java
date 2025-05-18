package cz.muni.fi.bandmanagementservice.mappers;

import cz.muni.fi.bandmanagementservice.model.Band;
import cz.muni.fi.bandmanagementservice.model.BandInfoUpdate;
import cz.muni.fi.bandmanagementservice.dto.BandDto;
import cz.muni.fi.bandmanagementservice.dto.BandInfoUpdateRequest;
import org.mapstruct.Mapper;

import java.util.HashSet;

/**
 * @author Tomáš MAREK
 */
@Mapper(componentModel = "spring")
public interface BandMapper {

    Band toEntity(BandDto bandDto);

    BandDto toDto(Band band);


}
