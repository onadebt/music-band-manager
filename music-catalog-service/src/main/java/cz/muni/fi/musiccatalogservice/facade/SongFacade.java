package cz.muni.fi.musiccatalogservice.facade;

import cz.muni.fi.musiccatalogservice.dto.SongDTO;
import cz.muni.fi.musiccatalogservice.exception.ResourceNotFoundException;
import cz.muni.fi.musiccatalogservice.service.SongService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class SongFacade {

    private final SongService songService;

    @Autowired
    public SongFacade(SongService songService) {
        this.songService = songService;
    }

    public List<SongDTO> getAllSongs() {
        return songService.getAllSongs();
    }

    public List<SongDTO> getSongsByAlbum(Long albumId) {
        return songService.getSongsByAlbum(albumId);
    }

    public List<SongDTO> getSongsByBand(Long bandId) {
        return songService.getSongsByBand(bandId);
    }

    public SongDTO getSongById(Long id) {
        return songService.getSongById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Song not found with id: " + id));
    }

    public SongDTO createSong(SongDTO songDTO) {
        return songService.createSong(songDTO);
    }

    public SongDTO updateSong(Long id, SongDTO songDTO) {
        SongDTO updatedSong = songService.updateSong(id, songDTO);
        if (updatedSong == null) {
            throw new ResourceNotFoundException("Song not found with id: " + id);
        }
        return updatedSong;
    }

    public void deleteSong(Long id) {
        boolean deleted = songService.deleteSong(id);
        if (!deleted) {
            throw new ResourceNotFoundException("Song not found with id: " + id);
        }
    }
}