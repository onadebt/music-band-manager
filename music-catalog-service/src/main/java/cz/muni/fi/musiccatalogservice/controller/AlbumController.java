package cz.muni.fi.musiccatalogservice.controller;

import cz.muni.fi.musiccatalogservice.dto.AlbumDTO;
import cz.muni.fi.musiccatalogservice.facade.AlbumFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/albums")
public class AlbumController {

    private final AlbumFacade albumFacade;

    @Autowired
    public AlbumController(AlbumFacade albumFacade) {
        this.albumFacade = albumFacade;
    }

    @GetMapping
    public ResponseEntity<List<AlbumDTO>> getAllAlbums() {
        return ResponseEntity.ok(albumFacade.getAllAlbums());
    }

    @GetMapping("/band/{bandId}")
    public ResponseEntity<List<AlbumDTO>> getAlbumsByBand(@PathVariable Long bandId) {
        return ResponseEntity.ok(albumFacade.getAlbumsByBand(bandId));
    }

    @GetMapping("/{id}")
    public ResponseEntity<AlbumDTO> getAlbumById(@PathVariable Long id) {
        return ResponseEntity.ok(albumFacade.getAlbumById(id));
    }

    @PostMapping
    public ResponseEntity<AlbumDTO> createAlbum(@RequestBody AlbumDTO albumDTO) {
        AlbumDTO createdAlbum = albumFacade.createAlbum(albumDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdAlbum);
    }

    @PutMapping("/{id}")
    public ResponseEntity<AlbumDTO> updateAlbum(@PathVariable Long id, @RequestBody AlbumDTO albumDTO) {
        AlbumDTO updatedAlbum = albumFacade.updateAlbum(id, albumDTO);
        return ResponseEntity.ok(updatedAlbum);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAlbum(@PathVariable Long id) {
        albumFacade.deleteAlbum(id);
        return ResponseEntity.noContent().build();
    }
}