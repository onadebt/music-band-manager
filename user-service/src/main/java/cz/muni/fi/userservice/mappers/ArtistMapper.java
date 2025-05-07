package cz.muni.fi.userservice.mappers;

import cz.muni.fi.userservice.dto.ArtistDto;
import cz.muni.fi.userservice.dto.ArtistUpdateDto;
import cz.muni.fi.userservice.model.Artist;
import cz.muni.fi.userservice.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring", uses = UserMapper.class)
public interface ArtistMapper {
    Artist toEntity(ArtistDto registerDTO);

    ArtistDto toDto(Artist artist);

    default Artist updateArtistFromDto(ArtistUpdateDto updateDto, @MappingTarget Artist artist) {
        if (updateDto == null || artist == null) {
            return artist;
        }
        if (updateDto.getUsername() != null) {
            artist.setUsername(updateDto.getUsername());
        }
        if (updateDto.getEmail() != null) {
            artist.setEmail(updateDto.getEmail());
        }
        if (updateDto.getFirstName() != null) {
            artist.setFirstName(updateDto.getFirstName());
        }
        if (updateDto.getLastName() != null) {
            artist.setLastName(updateDto.getLastName());
        }
        if (updateDto.getPassword() != null) {
            artist.setPassword(updateDto.getPassword());
        }
        if (updateDto.getStageName() != null) {
            artist.setStageName(updateDto.getStageName());
        }
        if (updateDto.getBio() != null) {
            artist.setBio(updateDto.getBio());
        }
        if (updateDto.getSkills() != null) {
            artist.setSkills(updateDto.getSkills());
        }
        if (updateDto.getBandIds() != null) {
            artist.setBandIds(updateDto.getBandIds());
        }
        return artist;
    }
}
