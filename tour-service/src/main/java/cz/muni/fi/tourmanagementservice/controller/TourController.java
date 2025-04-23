package cz.muni.fi.tourmanagementservice.controller;


import cz.muni.fi.tourmanagementservice.dto.TourDTO;
import cz.muni.fi.tourmanagementservice.facades.TourFacade;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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
@Tag(name = "Tour API", description = "Tour management API")
public class TourController {

    private final TourFacade tourFacade;

    @Autowired
    public TourController(TourFacade tourFacade) {
        this.tourFacade = tourFacade;
    }

    @GetMapping
    @Operation(summary = "Retrieve all tours")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "List of tours retrieved successfully"),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content(mediaType = "application/json"))
    })
    public ResponseEntity<List<TourDTO>> getAllTours() {
        return ResponseEntity.ok(tourFacade.getAllTours());
    }

    @GetMapping("/band/{bandId}")
    @Operation(summary = "Retrieve tours by band")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Tour found"),
            @ApiResponse(responseCode = "404", description = "Tour not found", content = @Content(mediaType = "application/problem+json"))
    })
    public ResponseEntity<List<TourDTO>> getToursByBand(
            @Parameter(description = "Band ID", required = true) @PathVariable Long bandId) {
        return ResponseEntity.ok(tourFacade.getToursByBand(bandId));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Retrieve tours by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Tour found"),
            @ApiResponse(responseCode = "404", description = "Tour not found", content = @Content(mediaType = "application/problem+json"))
    })
    public ResponseEntity<TourDTO> getTourById(
            @Parameter(description = "Tour ID", required = true) @PathVariable Long id) {
        return ResponseEntity.ok(tourFacade.getTourById(id));
    }

    @PostMapping
    @Operation(summary = "Create a new tour")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Tour created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input data", content = @Content(mediaType = "application/problem+json")),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content(mediaType = "application/json")),
    })
    public ResponseEntity<TourDTO> createTour(
            @Parameter(description = "Tour to create", required = true) @Valid @RequestBody TourDTO tourDTO) {
        TourDTO createdTour = tourFacade.createTour(tourDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdTour);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update a tour")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Tour updated successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input data", content = @Content(mediaType = "application/problem+json")),
            @ApiResponse(responseCode = "404", description = "Tour not found", content = @Content(mediaType = "application/problem+json")),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content(mediaType = "application/json"))
    })
    public ResponseEntity<TourDTO> updateTour(
            @Parameter(description = "Tour ID", required = true) @PathVariable Long id,
            @Parameter(description = "Tour to create", required = true) @Valid @RequestBody TourDTO tourDTO) {
        TourDTO updatedTour = tourFacade.updateTour(id, tourDTO);
        return ResponseEntity.ok(updatedTour);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a tour")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Tour deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Tour not found", content = @Content(mediaType = "application/problem+json"))
    })
    public ResponseEntity<Void> deleteTour(
            @Parameter(description = "Tour ID", required = true) @PathVariable Long id) {
        tourFacade.deleteTour(id);
        return ResponseEntity.noContent().build();
    }
}
