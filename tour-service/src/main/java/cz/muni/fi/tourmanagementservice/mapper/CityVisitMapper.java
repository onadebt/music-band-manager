package cz.muni.fi.tourmanagementservice.mapper;

import cz.muni.fi.tourmanagementservice.dto.CityVisitDto;
import cz.muni.fi.tourmanagementservice.model.CityVisit;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface CityVisitMapper {

    CityVisitDto toDTO(CityVisit cityVisit);

    CityVisit toEntity(CityVisitDto cityVisitDTO);

    @Mapping(target = "id", ignore = true)
    void updateEntityFromDto(CityVisitDto dto, @MappingTarget CityVisit entity);
}
