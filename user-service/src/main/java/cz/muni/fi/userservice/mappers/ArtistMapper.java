package cz.muni.fi.userservice.mappers;

import cz.muni.fi.userservice.dto.ArtistDto;
import cz.muni.fi.userservice.dto.ArtistUpdateDto;
import cz.muni.fi.userservice.model.Artist;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring", uses = UserMapper.class)
public interface ArtistMapper {
    Artist toEntity(ArtistDto registerDTO);

    ArtistDto toDto(Artist artist);

    default Artist updateArtistFromDto(ArtistUpdateDto updateDto, @MappingTarget Artist artist) {
        UserMapper userMapper = Mappers.getMapper(UserMapper.class);
        userMapper.updateUserFromDto(updateDto, artist);

        if (updateDto.getStageName() != null) {
            artist.setStageName(updateDto.getStageName());
        }
        if (updateDto.getBio() != null) {
            artist.setBio(updateDto.getBio());
        }
        if (updateDto.getSkills() != null) {
            artist.setSkills(updateDto.getSkills());
        }
        return artist;
    }
}
