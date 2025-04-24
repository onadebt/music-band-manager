package cz.muni.fi.tourmanagementservice.mapper;

import cz.muni.fi.tourmanagementservice.dto.CityVisitDTO;
import cz.muni.fi.tourmanagementservice.model.CityVisit;
import cz.muni.fi.tourmanagementservice.model.Tour;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;

@Mapper(componentModel = "spring")
public interface CityVisitMapper {

    CityVisitDTO toDTO(CityVisit cityVisit);

    CityVisit toEntity(CityVisitDTO cityVisitDTO);

    @Mapping(target = "id", ignore = true)
    void updateEntityFromDto(CityVisitDTO dto, @MappingTarget CityVisit entity);
}
