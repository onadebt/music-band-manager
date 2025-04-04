package cz.muni.fi.musiccatalogservice.controller;

import cz.muni.fi.musiccatalogservice.dto.SongDTO;
import cz.muni.fi.musiccatalogservice.facade.SongFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/songs")
public class SongController {

    private final SongFacade songFacade;

    @Autowired
    public SongController(SongFacade songFacade) {
        this.songFacade = songFacade;
    }

    @GetMapping
    public ResponseEntity<List<SongDTO>> getAllSongs() {
        return ResponseEntity.ok(songFacade.getAllSongs());
    }

    @GetMapping("/album/{albumId}")
    public ResponseEntity<List<SongDTO>> getSongsByAlbum(@PathVariable Long albumId) {
        return ResponseEntity.ok(songFacade.getSongsByAlbum(albumId));
    }

    @GetMapping("/band/{bandId}")
    public ResponseEntity<List<SongDTO>> getSongsByBand(@PathVariable Long bandId) {
        return ResponseEntity.ok(songFacade.getSongsByBand(bandId));
    }

    @GetMapping("/{id}")
    public ResponseEntity<SongDTO> getSongById(@PathVariable Long id) {
        return ResponseEntity.ok(songFacade.getSongById(id));
    }

    @PostMapping
    public ResponseEntity<SongDTO> createSong(@RequestBody SongDTO songDTO) {
        SongDTO createdSong = songFacade.createSong(songDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdSong);
    }

    @PutMapping("/{id}")
    public ResponseEntity<SongDTO> updateSong(@PathVariable Long id, @RequestBody SongDTO songDTO) {
        SongDTO updatedSong = songFacade.updateSong(id, songDTO);
        return ResponseEntity.ok(updatedSong);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSong(@PathVariable Long id) {
        songFacade.deleteSong(id);
        return ResponseEntity.noContent().build();
    }
}