package cz.muni.fi.musiccatalogservice.mapper;

import cz.muni.fi.musiccatalogservice.dto.AlbumDTO;
import cz.muni.fi.musiccatalogservice.model.Album;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface AlbumMapper {

    @Mapping(target = "songs", ignore = true)
    AlbumDTO toDTO(Album album);

    @Mapping(target = "songs", ignore = true)
    Album toEntity(AlbumDTO albumDTO);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "songs", ignore = true)
    void updateEntityFromDto(AlbumDTO dto, @MappingTarget Album entity);
}