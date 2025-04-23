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

    public List<Song> getAllSongs() {
        return songRepository.findAll();
    }

    public List<Song> getSongsByAlbum(Long albumId) {
        return songRepository.findByAlbumId(albumId);
    }

    public List<Song> getSongsByBand(Long bandId) {
        return songRepository.findByBandId(bandId);
    }

    public Song getSongById(Long id) {
        return songRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Song not found with id: " + id));
    }

    @Transactional
    public Song createSong(Song song) {
        return songRepository.save(song);
    }

    @Transactional
    public Song updateSong(Long id, Song songDetails) {
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
        getSongById(id);
        songRepository.deleteById(id);
    }
}