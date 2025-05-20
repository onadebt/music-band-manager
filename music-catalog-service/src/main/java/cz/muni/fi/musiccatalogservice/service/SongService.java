package cz.muni.fi.musiccatalogservice.service;

import cz.muni.fi.musiccatalogservice.exception.ResourceNotFoundException;
import cz.muni.fi.musiccatalogservice.model.Song;
import cz.muni.fi.musiccatalogservice.repository.SongRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class SongService {

    private final SongRepository songRepository;

    @Autowired
    public SongService(SongRepository songRepository) {
        this.songRepository = songRepository;
    }

    @Transactional(readOnly = true)
    public List<Song> getAllSongs() {
        return songRepository.findAll();
    }

    @Transactional(readOnly = true)
    public List<Song> getSongsByAlbum(Long albumId) {
        if (albumId == null) {
            throw new IllegalArgumentException("Album ID cannot be null");
        }

        if (albumId < 0) {
            throw new IllegalArgumentException("Invalid album ID: " + albumId);
        }

        return songRepository.findByAlbumId(albumId);
    }

    @Transactional(readOnly = true)
    public List<Song> getSongsByBand(Long bandId) {
        if (bandId == null) {
            throw new IllegalArgumentException("Band ID cannot be null");
        }

        if (bandId < 0) {
            throw new IllegalArgumentException("Invalid band ID: " + bandId);
        }

        return songRepository.findByBandId(bandId);
    }

    @Transactional(readOnly = true)
    public Song getSongById(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("Song ID cannot be null");
        }

        if (id < 0) {
            throw new IllegalArgumentException("Invalid song ID: " + id);
        }

        return songRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Song not found with id: " + id));
    }

    @Transactional
    public Song createSong(Song song) {
        if (song == null) {
            throw new IllegalArgumentException("Song cannot be null");
        }

        return songRepository.save(song);
    }

    @Transactional
    public Song updateSong(Long id, Song songDetails) {
        if (id == null) {
            throw new IllegalArgumentException("Song ID cannot be null");
        }

        if (songDetails == null) {
            throw new IllegalArgumentException("Song details cannot be null");
        }

        Song song = getSongById(id);
        song.setName(songDetails.getName());
        song.setDuration(songDetails.getDuration());
        song.setBandId(songDetails.getBandId());
        if (songDetails.getAlbum() != null) {
            song.setAlbum(songDetails.getAlbum());
        }
        return songRepository.save(song);
    }

    @Transactional
    public void deleteSong(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("Song ID cannot be null");
        }

        if (id < 0) {
            throw new IllegalArgumentException("Invalid song ID: " + id);
        }

        // Check if song exists
        getSongById(id);
        songRepository.deleteById(id);
    }
}