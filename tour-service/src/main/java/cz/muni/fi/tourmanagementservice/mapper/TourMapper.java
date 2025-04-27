package cz.muni.fi.tourmanagementservice.mapper;


import cz.muni.fi.tourmanagementservice.dto.TourDTO;
import cz.muni.fi.tourmanagementservice.model.Tour;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring", uses = TourMapper.class)
public interface TourMapper {

    @Mapping(target = "cityVisits")
    TourDTO toDTO(Tour tour);

    @Mapping(target = "cityVisits")
    Tour toEntity(TourDTO tourDTO);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "cityVisits")
    void updateEntityFromDto(TourDTO dto, @MappingTarget Tour entity);
}

