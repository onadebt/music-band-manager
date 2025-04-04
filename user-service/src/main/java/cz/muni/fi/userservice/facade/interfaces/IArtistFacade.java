package cz.muni.fi.userservice.facade.interfaces;

import cz.muni.fi.userservice.dto.ArtistDTO;

import java.util.List;
import java.util.Set;

public interface IArtistFacade {
    ArtistDTO register(ArtistDTO artistDTO);

    ArtistDTO findById(Long id);

    ArtistDTO findByUsername(String username);

    List<ArtistDTO> findAll();

    List<ArtistDTO> findByBandIds(Set<Long> bandIds);

    void deleteById(Long id);

    ArtistDTO updateBandIds(Long artistId, Set<Long> bandsIds);
}
