package cz.muni.fi.userservice.rest;

import cz.muni.fi.userservice.dto.ArtistDto;
import cz.muni.fi.userservice.dto.ArtistUpdateDto;
import cz.muni.fi.userservice.facade.ArtistFacade;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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

    private final ArtistFacade artistFacade;

    @Autowired
    public ArtistController(ArtistFacade artistFacade) {
        this.artistFacade = artistFacade;
    }

    @PostMapping
    @Operation(summary = "Register artist", description = "Register a new artist"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Artist registered successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input data", content = @Content(mediaType = "application/problem+json")),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content(mediaType = "application/json")),
    })
    public ResponseEntity<ArtistDto> register(@Valid @RequestBody ArtistDto artistDTO) {
        return ResponseEntity.ok(artistFacade.register(artistDTO));
    }


    @PutMapping("/{id}")
    @Operation(summary = "Update artist by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Artist updated successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input data", content = @Content(mediaType = "application/problem+json")),
            @ApiResponse(responseCode = "404", description = "Artist not found", content = @Content(mediaType = "application/problem+json")),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content(mediaType = "application/json"))

    })
    public ResponseEntity<ArtistDto> update(@PathVariable Long id, @RequestBody ArtistUpdateDto artistUpdateDto) {
        return ResponseEntity.ok(artistFacade.update(id, artistUpdateDto));
    }


    @GetMapping
    @Operation(summary = "Get all artists")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "List of artists retrieved successfully"),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content(mediaType = "application/json"))
    })
    public ResponseEntity<List<ArtistDto>> getAllArtists() {
        return ResponseEntity.ok(artistFacade.findAll());
    }


    @GetMapping("/{id}")
    @Operation(summary = "Get artist by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Artist found"),
            @ApiResponse(responseCode = "404", description = "Artist not found", content = @Content(mediaType = "application/problem+json"))
    })
    public ResponseEntity<ArtistDto> getArtistById(@PathVariable Long id) {
        return ResponseEntity.ok(artistFacade.findById(id));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete artist by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Artist deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Artist not found", content = @Content(mediaType = "application/problem+json"))
    })
    public ResponseEntity<Void> deleteArtist(@PathVariable Long id) {
        artistFacade.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/username/{username}")
    @Operation(summary = "Get artist by username")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Artist found"),
            @ApiResponse(responseCode = "404", description = "Artist not found", content = @Content(mediaType = "application/problem+json"))
    })
    public ResponseEntity<ArtistDto> getArtistByUsername(@PathVariable String username) {
        return ResponseEntity.ok(artistFacade.findByUsername(username));
    }

    @PostMapping("/bands/{artistId}")
    @Operation(summary = "Update artist's band IDs")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Artist's bands updated"),
            @ApiResponse(responseCode = "400", description = "Invalid input data", content = @Content(mediaType = "application/problem+json")),
            @ApiResponse(responseCode = "404", description = "Artist not found", content = @Content(mediaType = "application/problem+json")),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content(mediaType = "application/json"))
    })
    public ResponseEntity<ArtistDto> updateBands(@PathVariable Long artistId, @RequestBody Set<Long> bandIds) {
        return ResponseEntity.ok(artistFacade.updateBandIds(artistId, bandIds));
    }

    @PatchMapping("/link/{artistId}/{bandId}")
    @Operation(summary = "Link artist to band")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Artist linked to band successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input data", content = @Content(mediaType = "application/problem+json")),
            @ApiResponse(responseCode = "404", description = "Artist or band not found", content = @Content(mediaType = "application/problem+json")),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content(mediaType = "application/json"))
    })
    public ResponseEntity<ArtistDto> linkArtistToBand(@PathVariable Long artistId, @PathVariable Long bandId) {
        ArtistDto artistDto = artistFacade.linkArtistToBand(artistId, bandId);
        return ResponseEntity.ok(artistDto);
    }

    @PatchMapping("/unlink/{artistId}/{bandId}")
    @Operation(summary = "Unlink artist from band")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Artist unlinked from band successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input data", content = @Content(mediaType = "application/problem+json")),
            @ApiResponse(responseCode = "404", description = "Artist or band not found", content = @Content(mediaType = "application/problem+json")),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content(mediaType = "application/json"))
    })
    public ResponseEntity<ArtistDto> unlinkArtistFromBand(@PathVariable Long artistId, @PathVariable Long bandId) {
        ArtistDto artistDto = artistFacade.unlinkArtistFromBand(artistId, bandId);
        return ResponseEntity.ok(artistDto);
    }

    @GetMapping("/bands")
    @Operation(summary = "Get artists by band IDs")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "List of artists retrieved successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid band IDs provided", content = @Content(mediaType = "application/problem+json")),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content(mediaType = "application/json"))
    })
    public ResponseEntity<List<ArtistDto>> getArtistsByBandIds(@RequestParam Set<Long> bandIds) {
        return ResponseEntity.ok(artistFacade.findByBandIds(bandIds));
    }
}
