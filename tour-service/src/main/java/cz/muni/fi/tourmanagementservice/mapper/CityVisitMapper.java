package cz.muni.fi.tourmanagementservice.mapper;

import cz.muni.fi.tourmanagementservice.dto.CityVisitDTO;
import cz.muni.fi.tourmanagementservice.model.CityVisit;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring", uses = CityVisitMapper.class)
public interface CityVisitMapper {

    @Mapping(target = "tour", ignore = true)
    CityVisitDTO toDTO(CityVisit cityVisit);

    @Mapping(target = "tour", ignore = true)
    CityVisit toEntity(CityVisitDTO cityVisitDTO);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "tour", ignore = true)
    void updateEntityFromDto(CityVisitDTO dto, @MappingTarget CityVisit entity);
}
