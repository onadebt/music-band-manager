package cz.muni.fi.userservice.service.interfaces;

import cz.muni.fi.userservice.model.Artist;

import java.util.List;
import java.util.Set;

public interface IArtistService {
    Artist save(Artist artist);

    Artist findById(Long id);

    Artist findByUsername(String username);

    List<Artist> findAll();

    void deleteById(Long id);

    List<Artist> findByBandIds (Set<Long> bandIds);

    Artist updateArtistByBandIds(Long artistId, Set<Long> bandIds);

    Artist updateArtist(Long id, Artist artist);
}
