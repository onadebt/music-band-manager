package cz.muni.fi.musiccatalogservice.mapper;

import cz.muni.fi.musiccatalogservice.dto.SongDto;
import cz.muni.fi.musiccatalogservice.model.Album;
import cz.muni.fi.musiccatalogservice.model.Song;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;

@Mapper(componentModel = "spring")
public interface SongMapper {

    @Mapping(source = "album", target = "albumId", qualifiedByName = "albumToAlbumId")
    SongDto toDto(Song song);

    @Mapping(target = "album", ignore = true)
    Song toEntity(SongDto songDto);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "album", ignore = true)
    void updateEntityFromDto(SongDto dto, @MappingTarget Song entity);

    @Named("albumToAlbumId")
    default Long albumToAlbumId(Album album) {
        return album != null ? album.getId() : null;
    }
}