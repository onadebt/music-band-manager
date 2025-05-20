package cz.muni.fi.musiccatalogservice.rest;

import cz.muni.fi.musiccatalogservice.config.OpenApiConfig;
import cz.muni.fi.musiccatalogservice.dto.SongDto;
import cz.muni.fi.musiccatalogservice.facade.SongFacade;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/songs")
@Tag(name = "Song", description = "The Song API")
public class SongController {
    private static final String GENERAL_SCOPE = "test_1";
    private static final String MANAGER_SCOPE = "test_2";
    private static final String MUSICIAN_SCOPE = "test_3";

    private final SongFacade songFacade;

    @Autowired
    public SongController(SongFacade songFacade) {
        this.songFacade = songFacade;
    }

    @Operation(summary = "Get all songs", description = "Returns a list of all songs in the catalog")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved songs",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = SongDto.class)))
    })
    @SecurityRequirement(
            name = OpenApiConfig.SECURITY_SCHEME_NAME,
            scopes = {GENERAL_SCOPE}
    )
    @GetMapping
    public ResponseEntity<List<SongDto>> getAllSongs() {
        return ResponseEntity.ok(songFacade.getAllSongs());
    }

    @Operation(summary = "Get songs by album", description = "Returns songs for a specific album")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved songs",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = SongDto.class)))
    })
    @SecurityRequirement(
            name = OpenApiConfig.SECURITY_SCHEME_NAME,
            scopes = {GENERAL_SCOPE}
    )
    @GetMapping("/album/{albumId}")
    public ResponseEntity<List<SongDto>> getSongsByAlbum(
            @Parameter(description = "Album ID", required = true) @PathVariable Long albumId) {
        return ResponseEntity.ok(songFacade.getSongsByAlbum(albumId));
    }

    @Operation(summary = "Get songs by band", description = "Returns songs for a specific band")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved songs",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = SongDto.class)))
    })
    @SecurityRequirement(
            name = OpenApiConfig.SECURITY_SCHEME_NAME,
            scopes = {GENERAL_SCOPE}
    )
    @GetMapping("/band/{bandId}")
    public ResponseEntity<List<SongDto>> getSongsByBand(
            @Parameter(description = "Band ID", required = true) @PathVariable Long bandId) {
        return ResponseEntity.ok(songFacade.getSongsByBand(bandId));
    }

    @Operation(summary = "Get song by ID", description = "Returns a song by its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved song",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = SongDto.class))),
            @ApiResponse(responseCode = "404", description = "Song not found")
    })
    @SecurityRequirement(
            name = OpenApiConfig.SECURITY_SCHEME_NAME,
            scopes = {GENERAL_SCOPE}
    )
    @GetMapping("/{id}")
    public ResponseEntity<SongDto> getSongById(
            @Parameter(description = "Song ID", required = true) @PathVariable Long id) {
        return ResponseEntity.ok(songFacade.getSongById(id));
    }

    @Operation(summary = "Create a new song", description = "Creates a new song")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Song created successfully",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = SongDto.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input")
    })
    @SecurityRequirement(
            name = OpenApiConfig.SECURITY_SCHEME_NAME,
            scopes = {MANAGER_SCOPE}
    )
    @PostMapping
    public ResponseEntity<SongDto> createSong(
            @Parameter(description = "Song to create", required = true) @Valid @RequestBody SongDto songDto) {
        SongDto createdSong = songFacade.createSong(songDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdSong);
    }

    @Operation(summary = "Update a song", description = "Updates an existing song")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Song updated successfully",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = SongDto.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input"),
            @ApiResponse(responseCode = "404", description = "Song not found")
    })
    @SecurityRequirement(
            name = OpenApiConfig.SECURITY_SCHEME_NAME,
            scopes = {MANAGER_SCOPE}
    )
    @PutMapping("/{id}")
    public ResponseEntity<SongDto> updateSong(
            @Parameter(description = "Song ID", required = true) @PathVariable Long id,
            @Parameter(description = "Updated song details", required = true) @Valid @RequestBody SongDto songDto) {
        SongDto updatedSong = songFacade.updateSong(id, songDto);
        return ResponseEntity.ok(updatedSong);
    }

    @Operation(summary = "Delete a song", description = "Deletes a song by its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Song deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Song not found")
    })
    @SecurityRequirement(
            name = OpenApiConfig.SECURITY_SCHEME_NAME,
            scopes = {MANAGER_SCOPE}
    )
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSong(
            @Parameter(description = "Song ID", required = true) @PathVariable Long id) {
        songFacade.deleteSong(id);
        return ResponseEntity.noContent().build();
    }
}