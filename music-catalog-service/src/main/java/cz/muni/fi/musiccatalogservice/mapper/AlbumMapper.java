package cz.muni.fi.musiccatalogservice.mapper;

import cz.muni.fi.musiccatalogservice.dto.AlbumDto;
import cz.muni.fi.musiccatalogservice.model.Album;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface AlbumMapper {

    @Mapping(target = "songs", ignore = true)
    AlbumDto toDto(Album album);

    @Mapping(target = "songs", ignore = true)
    Album toEntity(AlbumDto albumDto);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "songs", ignore = true)
    void updateEntityFromDto(AlbumDto dto, @MappingTarget Album entity);
}