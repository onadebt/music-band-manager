package cz.muni.fi.userservice.mappers;

import cz.muni.fi.userservice.dto.ArtistDTO;
import cz.muni.fi.userservice.model.Artist;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = UserMapper.class)
public interface ArtistMapper {
    Artist toEntity(ArtistDTO registerDTO);

    ArtistDTO toDTO(Artist artist);
}
