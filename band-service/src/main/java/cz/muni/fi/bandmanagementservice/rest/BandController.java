package cz.muni.fi.bandmanagementservice.rest;

import cz.muni.fi.bandmanagementservice.config.OpenAPIConfig;
import cz.muni.fi.bandmanagementservice.exceptions.BandNotFoundException;
import cz.muni.fi.bandmanagementservice.exceptions.InvalidOperationException;
import cz.muni.fi.bandmanagementservice.facade.BandFacade;
import cz.muni.fi.bandmanagementservice.dto.BandDto;
import cz.muni.fi.bandmanagementservice.dto.BandInfoUpdateRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author Tomáš MAREK
 */
@RestController
@RequestMapping("/api/bands")
@Tag(name = "Band API", description = "API for managing bands")
public class BandController {
    private static final String GENERAL_SCOPE = "test_1";
    private static final String MANAGER_SCOPE = "test_2";
    private static final String MUSICIAN_SCOPE = "test_3";

    private final BandFacade bandFacade;

    @Autowired
    BandController(BandFacade facade) {
        this.bandFacade = facade;
    }

    @PostMapping
    @Operation(summary = "Creates a new band")
    @SecurityRequirement(
            name = OpenAPIConfig.SECURITY_SCHEME_NAME,
            scopes = {MANAGER_SCOPE}
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Band created", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "400", description = "Cannot create given band", content = @Content(mediaType = "application/problem+json"))
    })
    public ResponseEntity<BandDto> createBand(@RequestParam @NotBlank String name,
                                              @RequestParam @NotBlank String musicalStyle,
                                              @RequestParam @NotNull Long managerId) {
        return new ResponseEntity<>(bandFacade.createBand(name, musicalStyle, managerId), HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Returns searched band")
    @SecurityRequirement(
            name = OpenAPIConfig.SECURITY_SCHEME_NAME,
            scopes = {GENERAL_SCOPE}
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "404", description = "Band not found")
    })
    public ResponseEntity<BandDto> getBand(@PathVariable Long id) {
        return new ResponseEntity<>(bandFacade.getBand(id), HttpStatus.OK);
    }

    @PatchMapping
    @SecurityRequirement(
            name = OpenAPIConfig.SECURITY_SCHEME_NAME,
            scopes = {MANAGER_SCOPE}
    )
    @Operation(summary = "Update basic band info - managerId, name, logo, musical style")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Band updated", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "404", description = "Updated band not found", content = @Content(mediaType = "application/problem+json")),
            @ApiResponse(responseCode = "400", description = "Band could not be updated", content = @Content(mediaType = "application/problem+json"))
    })
    public ResponseEntity<BandDto> updateBand(@RequestBody @Valid BandInfoUpdateRequest bandInfoUpdateRequest) {
        return new ResponseEntity<>(bandFacade.updateBand(bandInfoUpdateRequest), HttpStatus.OK);
    }

    @GetMapping
    @Operation(summary = "Returns all bands")
    @SecurityRequirement(
            name = OpenAPIConfig.SECURITY_SCHEME_NAME,
            scopes = {GENERAL_SCOPE}
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK", content=@Content(mediaType = "application/json"))
    })
    public ResponseEntity<List<BandDto>> getAllBands() {
        return new ResponseEntity<>(bandFacade.getAllBands(), HttpStatus.OK);
    }

    @DeleteMapping("/{bandId}/members/{memberId}")
    @Operation(summary = "Remove member from the band")
    @SecurityRequirement(
            name = OpenAPIConfig.SECURITY_SCHEME_NAME,
            scopes = {MANAGER_SCOPE}
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Member removed", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "400", description = "Member could not be removed", content = @Content(mediaType = "application/problem+json")),
            @ApiResponse(responseCode = "404", description = "Band doest not exist", content = @Content(mediaType = "application/problem+json"))
    })
    public ResponseEntity<BandDto> removeMember(@PathVariable Long bandId, @PathVariable Long memberId) {
        return new ResponseEntity<>(bandFacade.removeMember(bandId, memberId), HttpStatus.NO_CONTENT);
    }

    @PatchMapping("/{bandId}/members/{memberId}")
    @Operation(summary = "Add member to the band")
    @SecurityRequirement(
            name = OpenAPIConfig.SECURITY_SCHEME_NAME,
            scopes = {MANAGER_SCOPE}
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Member added", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "400", description = "Member could not be added", content = @Content(mediaType = "application/problem+json")),
            @ApiResponse(responseCode = "404", description = "Band does not exist", content = @Content(mediaType = "application/problem+json"))
    })
    public ResponseEntity<BandDto> addMember(@PathVariable Long bandId, @PathVariable Long memberId) {
        return new ResponseEntity<>(bandFacade.addMember(bandId, memberId), HttpStatus.OK);
    }
}
