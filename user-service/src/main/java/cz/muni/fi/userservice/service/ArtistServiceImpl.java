package cz.muni.fi.userservice.service;

import cz.muni.fi.userservice.exception.UserAlreadyExistsException;
import cz.muni.fi.userservice.exception.UserNotFoundException;
import cz.muni.fi.userservice.model.Artist;
import cz.muni.fi.userservice.repository.ArtistRepository;
import cz.muni.fi.userservice.service.interfaces.ArtistService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;

@Transactional
@Service
public class ArtistServiceImpl implements ArtistService {

    private final ArtistRepository artistRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public ArtistServiceImpl(ArtistRepository artistRepository) {
        this.artistRepository = artistRepository;
        this.passwordEncoder = new BCryptPasswordEncoder();
    }

    public Artist save(Artist artist) {
        var existingArtist = artistRepository.findByUsername(artist.getUsername());
        if (existingArtist.isPresent()) {
            throw new UserAlreadyExistsException(artist);
        }

        String hashedPassword = passwordEncoder.encode(artist.getPassword());
        artist.setPassword(hashedPassword);
        artistRepository.save(artist);
        return artist;
    }

    @Transactional(readOnly = true)
    public Artist findById(Long id) {
        return artistRepository.findById(id).orElseThrow(() -> new UserNotFoundException(id));
    }

    @Transactional(readOnly = true)
    public Artist findByUsername(String username) {
        return artistRepository.findByUsername(username).orElseThrow(() -> new UserNotFoundException(username));
    }

    @Transactional(readOnly = true)
    public List<Artist> findAll() {
        return artistRepository.findAll();
    }

    @Transactional(readOnly = true)
    public List<Artist> findByBandIds(Set<Long> bandIds) {
        return artistRepository.findByBandIds(bandIds);
    }

    public void deleteById(Long id) {
        var maybeArtist = artistRepository.findById(id);
        if (maybeArtist.isEmpty()) {
            throw new UserNotFoundException(id);
        }

        artistRepository.deleteById(maybeArtist.get().getId());
    }

    public Artist updateArtistByBandIds(Long artistId, Set<Long> bandIds) {
        Artist artist = artistRepository.findById(artistId).orElseThrow(() -> new UserNotFoundException(artistId));
        artist.setBandIds(bandIds);
        return artistRepository.save(artist);
    }

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

    public Artist linkArtistToBand(Long artistId, Long bandId) {
        Artist artist = artistRepository.findById(artistId).orElseThrow(() -> new UserNotFoundException(artistId));
        Set<Long> bandIds = artist.getBandIds();
        bandIds.add(bandId);
        artist.setBandIds(bandIds);
        return artistRepository.save(artist);
    }

    public Artist unlinkArtistFromBand(Long artistId, Long bandId) {
        Artist artist = artistRepository.findById(artistId).orElseThrow(() -> new UserNotFoundException(artistId));
        Set<Long> bandIds = artist.getBandIds();
        bandIds.remove(bandId);
        artist.setBandIds(bandIds);
        return artistRepository.save(artist);
    }
}
