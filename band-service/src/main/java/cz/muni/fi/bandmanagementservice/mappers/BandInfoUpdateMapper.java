package cz.muni.fi.bandmanagementservice.mappers;

import cz.muni.fi.bandmanagementservice.dto.BandInfoUpdateRequest;
import cz.muni.fi.bandmanagementservice.model.BandInfoUpdate;
import org.mapstruct.Mapper;

/**
 * @author Tomáš MAREK
 */
@Mapper(componentModel = "spring")
public interface BandInfoUpdateMapper {
    BandInfoUpdate toEntity(BandInfoUpdateRequest bandInfoUpdateRequest);

}
