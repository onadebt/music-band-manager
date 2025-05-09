package cz.muni.fi.musiccatalogservice.controller;

import cz.muni.fi.musiccatalogservice.MusicCatalogServiceApplication;
import cz.muni.fi.musiccatalogservice.config.OpenApiConfig;
import cz.muni.fi.musiccatalogservice.config.SecurityConfig;
import cz.muni.fi.musiccatalogservice.dto.AlbumDTO;
import cz.muni.fi.musiccatalogservice.facade.AlbumFacade;
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
@RequestMapping("/api/albums")
@Tag(name = "Album", description = "The Album API")
public class AlbumController {
    private static final String GENERAL_SCOPE = "test_1";
    private static final String MANAGER_SCOPE = "test_2";
    private static final String MUSICIAN_SCOPE = "test_3";

    private final AlbumFacade albumFacade;

    @Autowired
    public AlbumController(AlbumFacade albumFacade) {
        this.albumFacade = albumFacade;
    }

    @Operation(summary = "Get all albums", description = "Returns a list of all albums in the catalog")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved albums",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = AlbumDTO.class)))
    })
    @SecurityRequirement(
            name = OpenApiConfig.SECURITY_SCHEME_NAME,
            scopes = {GENERAL_SCOPE}
    )
    @GetMapping
    public ResponseEntity<List<AlbumDTO>> getAllAlbums() {
        return ResponseEntity.ok(albumFacade.getAllAlbums());
    }

    @Operation(summary = "Get albums by band", description = "Returns albums for a specific band")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved albums",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = AlbumDTO.class)))
    })
    @SecurityRequirement(
            name = OpenApiConfig.SECURITY_SCHEME_NAME,
            scopes = {GENERAL_SCOPE}
    )
    @GetMapping("/band/{bandId}")
    public ResponseEntity<List<AlbumDTO>> getAlbumsByBand(
            @Parameter(description = "Band ID", required = true) @PathVariable Long bandId) {
        return ResponseEntity.ok(albumFacade.getAlbumsByBand(bandId));
    }

    @Operation(summary = "Get album by ID", description = "Returns an album by its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved album",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = AlbumDTO.class))),
            @ApiResponse(responseCode = "404", description = "Album not found")
    })
    @SecurityRequirement(
            name = OpenApiConfig.SECURITY_SCHEME_NAME,
            scopes = {GENERAL_SCOPE}
    )
    @GetMapping("/{id}")
    public ResponseEntity<AlbumDTO> getAlbumById(
            @Parameter(description = "Album ID", required = true) @PathVariable Long id) {
        return ResponseEntity.ok(albumFacade.getAlbumById(id));
    }

    @Operation(summary = "Create a new album", description = "Creates a new album")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Album created successfully",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = AlbumDTO.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input")
    })
    @SecurityRequirement(
            name = OpenApiConfig.SECURITY_SCHEME_NAME,
            scopes = {MANAGER_SCOPE}
    )
    @PostMapping
    public ResponseEntity<AlbumDTO> createAlbum(
            @Parameter(description = "Album to create", required = true) @Valid @RequestBody AlbumDTO albumDTO) {
        AlbumDTO createdAlbum = albumFacade.createAlbum(albumDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdAlbum);
    }

    @Operation(summary = "Update an album", description = "Updates an existing album")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Album updated successfully",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = AlbumDTO.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input"),
            @ApiResponse(responseCode = "404", description = "Album not found")
    })
    @SecurityRequirement(
            name = OpenApiConfig.SECURITY_SCHEME_NAME,
            scopes = {MANAGER_SCOPE}
    )
    @PutMapping("/{id}")
    public ResponseEntity<AlbumDTO> updateAlbum(
            @Parameter(description = "Album ID", required = true) @PathVariable Long id,
            @Parameter(description = "Updated album details", required = true) @Valid @RequestBody AlbumDTO albumDTO) {
        AlbumDTO updatedAlbum = albumFacade.updateAlbum(id, albumDTO);
        return ResponseEntity.ok(updatedAlbum);
    }

    @Operation(summary = "Delete an album", description = "Deletes an album by its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Album deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Album not found")
    })
    @SecurityRequirement(
            name = OpenApiConfig.SECURITY_SCHEME_NAME,
            scopes = {MANAGER_SCOPE}
    )
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAlbum(
            @Parameter(description = "Album ID", required = true) @PathVariable Long id) {
        albumFacade.deleteAlbum(id);
        return ResponseEntity.noContent().build();
    }
}