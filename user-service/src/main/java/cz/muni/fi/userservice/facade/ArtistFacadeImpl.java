package cz.muni.fi.userservice.facade;

import cz.muni.fi.userservice.dto.ArtistDto;
import cz.muni.fi.userservice.dto.ArtistUpdateDto;
import cz.muni.fi.userservice.facade.interfaces.ArtistFacade;
import cz.muni.fi.userservice.mappers.ArtistMapper;
import cz.muni.fi.userservice.model.Artist;
import cz.muni.fi.userservice.service.interfaces.ArtistService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Transactional
@Service
public class ArtistFacadeImpl implements ArtistFacade {

    private final ArtistService artistService;
    private final ArtistMapper artistMapper;

    @Autowired
    public ArtistFacadeImpl(ArtistService artistService, ArtistMapper artistMapper) {
        this.artistService = artistService;
        this.artistMapper = artistMapper;
    }

    public ArtistDto register(ArtistDto artistDTO) {
        if (artistDTO == null) {
            throw new IllegalArgumentException("ArtistDTO cannot be null");
        }
        Artist artist = artistMapper.toEntity(artistDTO);
        artistService.save(artist);
        return artistMapper.toDto(artist);
    }

    public ArtistDto findById(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("ID cannot be null");
        }
        Artist artist = artistService.findById(id);
        if (artist == null) {
            throw new IllegalArgumentException("Artist with ID " + id + " does not exist");
        }
        return artistMapper.toDto(artist);
    }

    public ArtistDto findByUsername(String username) {
        if (username == null) {
            throw new IllegalArgumentException("Username cannot be null");
        }
        Artist artist = artistService.findByUsername(username);
        if (artist == null) {
            throw new IllegalArgumentException("Artist with username " + username + " does not exist");
        }
        return artistMapper.toDto(artist);
    }

    public List<ArtistDto> findAll() {
        return artistService.findAll().stream()
                .map(artistMapper::toDto)
                .collect(Collectors.toList());
    }

    public List<ArtistDto> findByBandIds(Set<Long> bandIds) {
        if (bandIds == null) {
            throw new IllegalArgumentException("Band IDs cannot be null");
        }
        List<Artist> artists = artistService.findByBandIds(bandIds);
        return artists.stream()
                .map(artistMapper::toDto)
                .collect(Collectors.toList());
    }

    public void deleteById(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("ID cannot be null");
        }
        if (artistService.findById(id) == null) {
            throw new IllegalArgumentException("Artist with ID " + id + " does not exist");
        }

        artistService.deleteById(id);
    }

    public ArtistDto updateBandIds(Long artistId, Set<Long> bandsIds) {
        if (bandsIds == null) {
            throw new IllegalArgumentException("Band IDs cannot be null");
        }

        if (artistId == null) {
            throw new IllegalArgumentException("Artist ID cannot be null");
        }

        Artist updatedArtist = artistService.updateArtistByBandIds(artistId, bandsIds);
        return artistMapper.toDto(updatedArtist);
    }

    public ArtistDto update(Long id, ArtistUpdateDto artistUpdateDto) {
        if (id == null) {
            throw new IllegalArgumentException("ID cannot be null");
        }
        if (artistUpdateDto == null) {
            throw new IllegalArgumentException("ArtistDTO cannot be null");
        }

        Artist artist = artistService.findById(id);
        Artist updatedArtist = artistMapper.updateArtistFromDto(artistUpdateDto, artist);
        artistService.updateArtist(id, updatedArtist);
        return artistMapper.toDto(updatedArtist);
    }

    public ArtistDto linkArtistToBand(Long artistId, Long bandId) {
        if (artistId == null || bandId == null) {
            throw new IllegalArgumentException("Artist ID and Band ID cannot be null");
        }

        artistService.linkArtistToBand(artistId, bandId);
        return findById(artistId);
    }

    public ArtistDto unlinkArtistFromBand(Long artistId, Long bandId) {
        if (artistId == null || bandId == null) {
            throw new IllegalArgumentException("Artist ID and Band ID cannot be null");
        }

        artistService.unlinkArtistFromBand(artistId, bandId);
        return findById(artistId);
    }
}
