package cz.muni.fi.userservice.service;

import cz.muni.fi.userservice.model.Artist;
import cz.muni.fi.userservice.repository.ArtistRepository;
import cz.muni.fi.userservice.service.interfaces.IArtistService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
public class ArtistService implements IArtistService {

    @Autowired
    private ArtistRepository artistRepository;

    @Override
    public Artist save(Artist artist) {
        System.out.println("Saving artist: " + artist.getUsername());
        artistRepository.save(artist);
        return artist;
    }

    @Override
    public Artist findById(Long id) {
        return artistRepository.findById(id).orElse(null);
    }

    @Override
    public Artist findByUsername(String username) {
        return artistRepository.findByUsername(username).orElse(null);
    }

    @Override
    public List<Artist> findAll() {
        return artistRepository.findAll();
    }

    @Override
    public void deleteById(Long id) {
        artistRepository.deleteById(id);
    }

    @Override
    public List<Artist> findByBandIds(Set<Long> bandIds) {
        return artistRepository.findByBandIds(bandIds);
    }

    @Override
    public Artist updateArtistByBandIds(Long artistId, Set<Long> bandIds) {
        Artist artist = artistRepository.findById(artistId).orElse(null);
        if (artist == null) {
            throw new IllegalArgumentException("Artist with ID " + artistId + " does not exist");
        }
        artist.setBandIds(bandIds);
        return artistRepository.save(artist);
    }
}
