package cz.muni.fi.userservice.service;

import cz.muni.fi.userservice.exception.UserNotFoundException;
import cz.muni.fi.userservice.model.Artist;
import cz.muni.fi.userservice.repository.ArtistRepository;
import cz.muni.fi.userservice.service.interfaces.IArtistService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
public class ArtistService implements IArtistService {

    private final ArtistRepository artistRepository;
    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @Autowired
    public ArtistService(ArtistRepository artistRepository) {
        this.artistRepository = artistRepository;
    }

    @Override
    public Artist save(Artist artist) {
        var existingArtist = artistRepository.findByUsername(artist.getUsername());
        if (existingArtist.isPresent()) {
            throw new UserNotFoundException(artist.getUsername());
        }

        String hashedPassword = passwordEncoder.encode(artist.getPassword());
        artist.setPassword(hashedPassword);
        artistRepository.save(artist);
        return artist;
    }

    @Override
    public Artist findById(Long id) {
        return artistRepository.findById(id).orElseThrow(() -> new UserNotFoundException(id));
    }

    @Override
    public Artist findByUsername(String username) {
        return artistRepository.findByUsername(username).orElseThrow(() -> new UserNotFoundException(username));
    }

    @Override
    public List<Artist> findAll() {
        return artistRepository.findAll();
    }

    @Override
    public void deleteById(Long id) {
        var maybeArtist = artistRepository.findById(id);
        if (maybeArtist.isEmpty()) {
            throw new UserNotFoundException(id);
        }

        artistRepository.deleteById(maybeArtist.get().getId());
    }

    @Override
    public List<Artist> findByBandIds(Set<Long> bandIds) {
        return artistRepository.findByBandIds(bandIds);
    }

    @Override
    public Artist updateArtistByBandIds(Long artistId, Set<Long> bandIds) {
        Artist artist = artistRepository.findById(artistId).orElseThrow(() -> new UserNotFoundException(artistId));
        artist.setBandIds(bandIds);
        return artistRepository.save(artist);
    }

    @Override
    public Artist updateArtist(Long id, Artist artist) {
        Artist existingArtist = artistRepository.findById(artist.getId()).orElseThrow(() -> new UserNotFoundException(artist.getId()));
        existingArtist.setUsername(artist.getUsername());
        existingArtist.setEmail(artist.getEmail());
        existingArtist.setPassword(passwordEncoder.encode(artist.getPassword()));
        existingArtist.setFirstName(artist.getFirstName());
        existingArtist.setLastName(artist.getLastName());
        existingArtist.setStageName(artist.getStageName());
        existingArtist.setBio(artist.getBio());
        existingArtist.setSkills(artist.getSkills());
        existingArtist.setBandIds(artist.getBandIds());
        return artistRepository.save(existingArtist);
    }

    @Override
    public void linkArtistToBand(Long artistId, Long bandId) {
        Artist artist = artistRepository.findById(artistId).orElseThrow(() -> new UserNotFoundException(artistId));
        Set<Long> bandIds = artist.getBandIds();
        bandIds.add(bandId);
        artist.setBandIds(bandIds);
        artistRepository.save(artist);
    }

    @Override
    public void unlinkArtistFromBand(Long artistId, Long bandId) {
        Artist artist = artistRepository.findById(artistId).orElseThrow(() -> new UserNotFoundException(artistId));
        Set<Long> bandIds = artist.getBandIds();
        bandIds.remove(bandId);
        artist.setBandIds(bandIds);
        artistRepository.save(artist);
    }
}
