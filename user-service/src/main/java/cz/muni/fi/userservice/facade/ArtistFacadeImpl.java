package cz.muni.fi.userservice.facade;

import cz.muni.fi.userservice.dto.ArtistDto;
import cz.muni.fi.userservice.dto.ArtistUpdateDto;
import cz.muni.fi.userservice.facade.interfaces.ArtistFacade;
import cz.muni.fi.userservice.mapper.ArtistMapper;
import cz.muni.fi.userservice.model.Artist;
import cz.muni.fi.userservice.service.interfaces.ArtistService;
import jakarta.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Transactional
@Validated
@Service
public class ArtistFacadeImpl implements ArtistFacade {

    private final ArtistService artistService;
    private final ArtistMapper artistMapper;

    @Autowired
    public ArtistFacadeImpl(ArtistService artistService, ArtistMapper artistMapper) {
        this.artistService = artistService;
        this.artistMapper = artistMapper;
    }

    public ArtistDto register(@NotNull ArtistDto artistDTO) {
        Artist artist = artistMapper.toEntity(artistDTO);
        artistService.save(artist);
        return artistMapper.toDto(artist);
    }

    public ArtistDto findById(@NotNull Long id) {
        Artist artist = artistService.findById(id);
        return artistMapper.toDto(artist);
    }

    public ArtistDto findByUsername(@NotNull String username) {
        Artist artist = artistService.findByUsername(username);
        return artistMapper.toDto(artist);
    }

    public List<ArtistDto> findAll() {
        return artistService.findAll().stream()
                .map(artistMapper::toDto)
                .collect(Collectors.toList());
    }

    public List<ArtistDto> findByBandIds(@NotNull Set<Long> bandIds) {
        List<Artist> artists = artistService.findByBandIds(bandIds);
        return artists.stream()
                .map(artistMapper::toDto)
                .collect(Collectors.toList());
    }

    public void deleteById(@NotNull Long id) {
        artistService.deleteById(id);
    }

    public ArtistDto updateBandIds(@NotNull Long artistId, @NotNull Set<Long> bandsIds) {
        Artist updatedArtist = artistService.updateArtistByBandIds(artistId, bandsIds);
        return artistMapper.toDto(updatedArtist);
    }

    public ArtistDto update(@NotNull Long id, @NotNull ArtistUpdateDto artistUpdateDto) {
        Artist artist = artistService.findById(id);
        Artist updatedArtist = artistMapper.updateArtistFromDto(artistUpdateDto, artist);
        artistService.updateArtist(id, updatedArtist);
        return artistMapper.toDto(updatedArtist);
    }

    public ArtistDto linkArtistToBand(@NotNull Long bandId, @NotNull Long artistId) {
        artistService.linkArtistToBand(bandId, artistId);
        return findById(artistId);
    }

    public ArtistDto unlinkArtistFromBand(@NotNull Long bandId, @NotNull Long artistId) {
        artistService.unlinkArtistFromBand(bandId, artistId);
        return findById(artistId);
    }
}
