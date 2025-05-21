package cz.muni.fi.bandmanagementservice.mapper;

import cz.muni.fi.bandmanagementservice.model.BandOffer;
import cz.muni.fi.bandmanagementservice.dto.BandOfferDto;
import org.mapstruct.Mapper;

/**
 * @author Tomáš MAREK
 */
@Mapper(componentModel = "spring")
public interface BandOfferMapper {
    BandOfferDto toDto(BandOffer bandOffer);
}
