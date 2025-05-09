package cz.muni.fi.tourmanagementservice.mapper;


import cz.muni.fi.tourmanagementservice.dto.TourDto;
import cz.muni.fi.tourmanagementservice.model.Tour;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring", uses = TourMapper.class)
public interface TourMapper {

    @Mapping(target = "cityVisits")
    TourDto toDTO(Tour tour);

    @Mapping(target = "cityVisits")
    Tour toEntity(TourDto tourDTO);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "cityVisits")
    void updateEntityFromDto(TourDto dto, @MappingTarget Tour entity);
}

