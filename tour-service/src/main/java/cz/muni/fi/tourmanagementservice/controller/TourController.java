package cz.muni.fi.tourmanagementservice.controller;


import cz.muni.fi.tourmanagementservice.config.OpenApiConfig;
import cz.muni.fi.tourmanagementservice.dto.CityVisitDto;
import cz.muni.fi.tourmanagementservice.dto.TourDto;
import cz.muni.fi.tourmanagementservice.facades.TourFacade;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@RestController
@RequestMapping("/api/tours")
@Tag(name = "Tour", description = "Tour management API")
public class TourController {
    private static final String GENERAL_SCOPE = "test_1";
    private static final String MANAGER_SCOPE = "test_2";
    private static final String MUSICIAN_SCOPE = "test_3";


    private final TourFacade tourFacade;

    @Autowired
    public TourController(TourFacade tourFacade) {
        this.tourFacade = tourFacade;
    }

    @GetMapping
    @Operation(summary = "Retrieve all tours")
    @SecurityRequirement(
            name = OpenApiConfig.SECURITY_SCHEME_NAME,
            scopes = {GENERAL_SCOPE}
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "List of tours retrieved successfully"),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content(mediaType = "application/json"))
    })
    public ResponseEntity<List<TourDto>> getAllTours() {
        return ResponseEntity.ok(tourFacade.getAllTours());
    }

    @GetMapping("/band/{bandId}")
    @Operation(summary = "Retrieve tours by band")
    @SecurityRequirement(
            name = OpenApiConfig.SECURITY_SCHEME_NAME,
            scopes = {GENERAL_SCOPE}
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Tour found"),
            @ApiResponse(responseCode = "404", description = "Tour not found", content = @Content(mediaType = "application/problem+json"))
    })
    public ResponseEntity<List<TourDto>> getToursByBand(
            @Parameter(description = "Band ID", required = true) @PathVariable Long bandId) {
        return ResponseEntity.ok(tourFacade.getToursByBand(bandId));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Retrieve tours by id")
    @SecurityRequirement(
            name = OpenApiConfig.SECURITY_SCHEME_NAME,
            scopes = {GENERAL_SCOPE}
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Tour found"),
            @ApiResponse(responseCode = "404", description = "Tour not found", content = @Content(mediaType = "application/problem+json"))
    })
    public ResponseEntity<TourDto> getTourById(
            @Parameter(description = "Tour ID", required = true) @PathVariable Long id) {
        return ResponseEntity.ok(tourFacade.getTourById(id));
    }

    @PostMapping
    @Operation(summary = "Create a new tour")
    @SecurityRequirement(
            name = OpenApiConfig.SECURITY_SCHEME_NAME,
            scopes = {MANAGER_SCOPE}
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Tour created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input data", content = @Content(mediaType = "application/problem+json")),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content(mediaType = "application/json")),
    })
    public ResponseEntity<TourDto> createTour(
            @Parameter(description = "Tour to create", required = true) @Valid @RequestBody TourDto tourDTO) {
        TourDto createdTour = tourFacade.createTour(tourDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdTour);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update a tour")
    @SecurityRequirement(
            name = OpenApiConfig.SECURITY_SCHEME_NAME,
            scopes = {MANAGER_SCOPE}
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Tour updated successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input data", content = @Content(mediaType = "application/problem+json")),
            @ApiResponse(responseCode = "404", description = "Tour not found", content = @Content(mediaType = "application/problem+json")),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content(mediaType = "application/json"))
    })
    public ResponseEntity<TourDto> updateTour(
            @Parameter(description = "Tour ID", required = true) @PathVariable Long id,
            @Parameter(description = "Tour to create", required = true) @Valid @RequestBody TourDto tourDTO) {
        TourDto updatedTour = tourFacade.updateTour(id, tourDTO);
        return ResponseEntity.ok(updatedTour);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a tour")
    @SecurityRequirement(
            name = OpenApiConfig.SECURITY_SCHEME_NAME,
            scopes = {MANAGER_SCOPE}
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Tour deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Tour not found", content = @Content(mediaType = "application/problem+json"))
    })
    public ResponseEntity<Void> deleteTour(
            @Parameter(description = "Tour ID", required = true) @PathVariable Long id) {
        tourFacade.deleteTour(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{tourId}/city-visit")
    @Operation(summary = "Add a city visit to a tour")
    @SecurityRequirement(
            name = OpenApiConfig.SECURITY_SCHEME_NAME,
            scopes = {MANAGER_SCOPE}
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "City visit added successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input data", content = @Content(mediaType = "application/problem+json")),
            @ApiResponse(responseCode = "404", description = "Tour not found", content = @Content(mediaType = "application/problem+json")),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content(mediaType = "application/json"))
    })
    public ResponseEntity<Void> addCityVisitToTour(
            @Parameter(description = "Tour ID", required = true) @PathVariable Long tourId,
            @Parameter(description = "City visit to add", required = true) @Valid @RequestBody CityVisitDto cityVisitDTO) {
        tourFacade.addCityVisitToTour(tourId, cityVisitDTO);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @DeleteMapping("/{tourId}/city-visit/{cityVisitId}")
    @Operation(summary = "Remove a city visit from a tour")
    @SecurityRequirement(
            name = OpenApiConfig.SECURITY_SCHEME_NAME,
            scopes = {MANAGER_SCOPE}
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "City visit removed successfully"),
            @ApiResponse(responseCode = "404", description = "Tour or city visit not found", content = @Content(mediaType = "application/problem+json"))
    })
    public ResponseEntity<Void> removeCityVisitFromTour(
            @Parameter(description = "Tour ID", required = true) @PathVariable Long tourId,
            @Parameter(description = "City visit ID", required = true) @PathVariable Long cityVisitId) {
        tourFacade.removeCityVisitFromTour(tourId, cityVisitId);
        return ResponseEntity.noContent().build();
    }
}
