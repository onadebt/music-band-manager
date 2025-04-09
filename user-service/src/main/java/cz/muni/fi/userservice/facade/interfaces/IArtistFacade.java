package cz.muni.fi.userservice.facade.interfaces;

import cz.muni.fi.userservice.dto.ArtistDto;
import cz.muni.fi.userservice.dto.ArtistUpdateDto;

import java.util.List;
import java.util.Set;

public interface IArtistFacade {
    ArtistDto register(ArtistDto artistDTO);

    ArtistDto findById(Long id);

    ArtistDto findByUsername(String username);

    List<ArtistDto> findAll();

    List<ArtistDto> findByBandIds(Set<Long> bandIds);

    void deleteById(Long id);

    ArtistDto updateBandIds(Long artistId, Set<Long> bandsIds);

    ArtistDto update(Long id, ArtistUpdateDto artistUpdateDto);
}
