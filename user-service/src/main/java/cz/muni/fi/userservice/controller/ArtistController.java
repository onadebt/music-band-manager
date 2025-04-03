package cz.muni.fi.userservice.controller;

import cz.muni.fi.userservice.dto.ArtistDTO;
import cz.muni.fi.userservice.facade.ArtistFacade;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/api/artists")
public class ArtistController {

    @Autowired
    private ArtistFacade artistFacade;

    @PostMapping("/register")
    public ResponseEntity<ArtistDTO> register(@Valid @RequestBody ArtistDTO artistDTO) {
        return ResponseEntity.ok(artistFacade.register(artistDTO));
    }

    @GetMapping("/all")
    public ResponseEntity<List<ArtistDTO>> getAllArtists() {
        return ResponseEntity.ok(artistFacade.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ArtistDTO> getArtistById(@PathVariable Long id) {
        return ResponseEntity.ok(artistFacade.findById(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteArtist(@PathVariable Long id) {
        artistFacade.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/username/{username}")
    public ResponseEntity<ArtistDTO> getArtistByUsername(@PathVariable String username) {
        return ResponseEntity.ok(artistFacade.findByUsername(username));
    }

    @PostMapping("/bands/{artistId}")
    public ResponseEntity<ArtistDTO> updateBands(@PathVariable Long artistId, @RequestBody Set<Long> bandIds) {
        return ResponseEntity.ok(artistFacade.updateBandIds(artistId, bandIds));
    }

    @GetMapping("/bands")
    public ResponseEntity<List<ArtistDTO>> getArtistsByBandIds(@RequestParam Set<Long> bandIds) {
        return ResponseEntity.ok(artistFacade.findByBandIds(bandIds));
    }
}
