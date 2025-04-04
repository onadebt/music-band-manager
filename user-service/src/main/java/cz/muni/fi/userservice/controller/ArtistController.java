package cz.muni.fi.userservice.controller;

import cz.muni.fi.userservice.dto.ArtistDTO;
import cz.muni.fi.userservice.facade.ArtistFacade;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/api/artists")
@Tag(name = "Artist API", description = "Artist management API")
public class ArtistController {

    @Autowired
    private ArtistFacade artistFacade;

    @Operation(summary = "Register a new artist")
    @PostMapping("/register")
    public ResponseEntity<ArtistDTO> register(@Valid @RequestBody ArtistDTO artistDTO) {
        return ResponseEntity.ok(artistFacade.register(artistDTO));
    }

    @Operation(summary = "Get all artists")
    @GetMapping("/all")
    public ResponseEntity<List<ArtistDTO>> getAllArtists() {
        return ResponseEntity.ok(artistFacade.findAll());
    }


    @Operation(summary = "Get artist by ID")
    @GetMapping("/{id}")
    public ResponseEntity<ArtistDTO> getArtistById(@PathVariable Long id) {
        return ResponseEntity.ok(artistFacade.findById(id));
    }

    @Operation(summary = "Delete artist by ID")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteArtist(@PathVariable Long id) {
        artistFacade.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Get artist by username")
    @GetMapping("/username/{username}")
    public ResponseEntity<ArtistDTO> getArtistByUsername(@PathVariable String username) {
        return ResponseEntity.ok(artistFacade.findByUsername(username));
    }

    @Operation(summary = "Update artist's band IDs")
    @PostMapping("/bands/{artistId}")
    public ResponseEntity<ArtistDTO> updateBands(@PathVariable Long artistId, @RequestBody Set<Long> bandIds) {
        return ResponseEntity.ok(artistFacade.updateBandIds(artistId, bandIds));
    }

    @Operation(summary = "Get artists by band IDs")
    @GetMapping("/bands")
    public ResponseEntity<List<ArtistDTO>> getArtistsByBandIds(@RequestParam Set<Long> bandIds) {
        return ResponseEntity.ok(artistFacade.findByBandIds(bandIds));
    }
}
