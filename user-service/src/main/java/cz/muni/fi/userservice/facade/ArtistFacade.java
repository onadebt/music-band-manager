package cz.muni.fi.userservice.facade;

import cz.muni.fi.userservice.dto.ArtistDTO;
import cz.muni.fi.userservice.facade.interfaces.IArtistFacade;
import cz.muni.fi.userservice.mappers.ArtistMapper;
import cz.muni.fi.userservice.model.Artist;
import cz.muni.fi.userservice.service.interfaces.IArtistService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class ArtistFacade implements IArtistFacade {
    @Autowired
    private IArtistService artistService;

    @Autowired
    private ArtistMapper artistMapper;

    public ArtistDTO register(ArtistDTO artistDTO) {
        if (artistDTO == null) {
            throw new IllegalArgumentException("ArtistDTO cannot be null");
        }
        Artist artist = artistMapper.toEntity(artistDTO);
        artistService.save(artist);
        return artistMapper.toDTO(artist);
    }

    public ArtistDTO findById(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("ID cannot be null");
        }
        Artist artist = artistService.findById(id);
        if (artist == null) {
            throw new IllegalArgumentException("Artist with ID " + id + " does not exist");
        }
        return artistMapper.toDTO(artist);
    }

    public ArtistDTO findByUsername(String username) {
        if (username == null) {
            throw new IllegalArgumentException("Username cannot be null");
        }
        Artist artist = artistService.findByUsername(username);
        if (artist == null) {
            throw new IllegalArgumentException("Artist with username " + username + " does not exist");
        }
        return artistMapper.toDTO(artist);
    }

    public List<ArtistDTO> findAll() {
        return artistService.findAll().stream()
                .map(artistMapper::toDTO)
                .collect(Collectors.toList());
    }

    public List<ArtistDTO> findByBandIds(Set<Long> bandIds) {
        if (bandIds == null) {
            throw new IllegalArgumentException("Band IDs cannot be null");
        }
        List<Artist> artists = artistService.findByBandIds(bandIds);
        return artists.stream()
                .map(artistMapper::toDTO)
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

    public ArtistDTO updateBandIds(Long artistId, Set<Long> bandsIds) {
        if (bandsIds == null) {
            throw new IllegalArgumentException("Band IDs cannot be null");
        }

        if (artistId == null) {
            throw new IllegalArgumentException("Artist ID cannot be null");
        }

        Artist updatedArtist = artistService.updateArtistByBandIds(artistId, bandsIds);
        return artistMapper.toDTO(updatedArtist);
    }
}
