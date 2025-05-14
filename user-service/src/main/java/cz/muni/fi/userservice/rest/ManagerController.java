package cz.muni.fi.userservice.rest;

import cz.muni.fi.userservice.config.OpenApiConfig;
import cz.muni.fi.userservice.dto.ManagerDto;
import cz.muni.fi.userservice.dto.ManagerUpdateDto;
import cz.muni.fi.userservice.facade.interfaces.ManagerFacade;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
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
import java.util.Set;

@RestController
@RequestMapping("/api/managers")
@Tag(name = "Manager API", description = "Manager management API")
public class ManagerController {
    private static final String GENERAL_SCOPE = "test_1";
    private static final String MANAGER_SCOPE = "test_2";
    private static final String MUSICIAN_SCOPE = "test_3";


    private final ManagerFacade managerFacade;

    @Autowired
    public ManagerController(ManagerFacade managerFacade) {
        this.managerFacade = managerFacade;
    }

    @PostMapping
    @Operation(summary = "Register a new manager")
    @SecurityRequirement(
            name = OpenApiConfig.SECURITY_SCHEME_NAME,
            scopes = {MANAGER_SCOPE}
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Manager registered successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input data", content = @Content(mediaType = "application/problem+json")),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content(mediaType = "application/json")),
    })
    public ResponseEntity<ManagerDto> register(@Valid @RequestBody ManagerDto managerDTO) {
        return new ResponseEntity<>(managerFacade.register(managerDTO), HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Get all managers")
    @SecurityRequirement(
            name = OpenApiConfig.SECURITY_SCHEME_NAME,
            scopes = {MANAGER_SCOPE}
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Manager deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Manager not found", content = @Content(mediaType = "application/problem+json")),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content(mediaType = "application/json"))
    })
    public ResponseEntity<Void> deleteById(@PathVariable Long id) {
        managerFacade.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get manager by ID")
    @SecurityRequirement(
            name = OpenApiConfig.SECURITY_SCHEME_NAME,
            scopes = {GENERAL_SCOPE}
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Manager found"),
            @ApiResponse(responseCode = "404", description = "Manager not found", content = @Content(mediaType = "application/problem+json")),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content(mediaType = "application/json"))
    })
    public ResponseEntity<ManagerDto> getById(@PathVariable Long id) {
        return ResponseEntity.ok(managerFacade.findById(id));
    }

    @GetMapping
    @Operation(summary = "Get all managers")
    @SecurityRequirement(
            name = OpenApiConfig.SECURITY_SCHEME_NAME,
            scopes = {GENERAL_SCOPE}
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "List of managers retrieved successfully"),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content(mediaType = "application/json"))
    })
    public ResponseEntity<List<ManagerDto>> getAllManagers() {
        return ResponseEntity.ok(managerFacade.findAll());
    }

    @PatchMapping("/bands/{managerId}")
    @Operation(summary = "Update manager bands by ID")
    @SecurityRequirement(
            name = OpenApiConfig.SECURITY_SCHEME_NAME,
            scopes = {MANAGER_SCOPE}
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Manager bands updated successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input data", content = @Content(mediaType = "application/problem+json")),
            @ApiResponse(responseCode = "404", description = "Manager not found", content = @Content(mediaType = "application/problem+json")),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content(mediaType = "application/json"))
    })
    public ResponseEntity<ManagerDto> updateBands(@PathVariable Long managerId, @RequestBody Set<Long> bandIds) {
        return ResponseEntity.ok(managerFacade.updateBandIds(managerId, bandIds));
    }

    @GetMapping("/bands")
    @Operation(summary = "Get managers by band IDs")
    @SecurityRequirement(
            name = OpenApiConfig.SECURITY_SCHEME_NAME,
            scopes = {GENERAL_SCOPE}
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "List of managers by band IDs retrieved successfully"),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content(mediaType = "application/json"))
    })
    public ResponseEntity<List<ManagerDto>> getManagersByBandIds(@RequestParam Set<Long> bandIds) {
        return ResponseEntity.ok(managerFacade.findByBandIds(bandIds));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update manager by ID")
    @SecurityRequirement(
            name = OpenApiConfig.SECURITY_SCHEME_NAME,
            scopes = {MANAGER_SCOPE}
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Manager updated successfully"),
            @ApiResponse(responseCode = "404", description = "Manager not found", content = @Content(mediaType = "application/problem+json")),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content(mediaType = "application/json"))
    })
    public ResponseEntity<ManagerDto> update(@PathVariable Long id, @RequestBody ManagerUpdateDto updateDto) {
        return ResponseEntity.ok(managerFacade.update(id, updateDto));
    }
}
