package cz.muni.fi.tourmanagementservice.mapper;

import cz.muni.fi.tourmanagementservice.dto.CityVisitDTO;
import cz.muni.fi.tourmanagementservice.model.CityVisit;
import cz.muni.fi.tourmanagementservice.model.Tour;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;

public interface CityVisitMapper {

    @Mapping(source = "tour", target = "tourId", qualifiedByName = "tourToTourId")
    CityVisitDTO toDTO(CityVisit cityVisit);

    @Mapping(target = "tour", ignore = true)
    CityVisit toEntity(CityVisitDTO cityVisitDTO);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "tour", ignore = true)
    void updateEntityFromDto(CityVisitDTO dto, @MappingTarget CityVisit entity);

    @Named("tourToTourId")
    default Long tourToTourId(Tour tour) {
        return tour != null ? tour.getId() : null;
    }
}
