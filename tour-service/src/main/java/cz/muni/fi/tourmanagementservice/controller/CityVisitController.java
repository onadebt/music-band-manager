package cz.muni.fi.tourmanagementservice.controller;


import cz.muni.fi.tourmanagementservice.dto.CityVisitDto;
import cz.muni.fi.tourmanagementservice.facades.CityVisitFacade;
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
@RequestMapping("/api/cityVisits")
@Tag(name = "CityVisit", description = "CityVisit management API")
public class CityVisitController {

    private final CityVisitFacade cityVisitFacade;

    @Autowired
    public CityVisitController(CityVisitFacade cityVisitFacade) {
        this.cityVisitFacade = cityVisitFacade;
    }

    @GetMapping
    @Operation(summary = "Retrieve all city visits")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "List of city visits retrieved successfully"),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content(mediaType = "application/json"))
    })
    public ResponseEntity<List<CityVisitDto>> getAllCityVisits() {
        return ResponseEntity.ok(cityVisitFacade.getAllCityVisits());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Retrieve all city visits by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "City visit found"),
            @ApiResponse(responseCode = "404", description = "City visit not found", content = @Content(mediaType = "application/problem+json"))
    })
    public ResponseEntity<CityVisitDto> getCityVisitById(
            @Parameter(description = "City Visit ID", required = true) @PathVariable Long id) {
        return ResponseEntity.ok(cityVisitFacade.getCityVisitById(id));
    }


    @PostMapping
    @Operation(summary = "Create new city visit", description = "Create a new city visit"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "City visit created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input data", content = @Content(mediaType = "application/problem+json")),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content(mediaType = "application/json")),
    })
    public ResponseEntity<CityVisitDto> createCityVisit(
            @Parameter(description = "City Visit to create", required = true) @Valid @RequestBody CityVisitDto cityVisitDTO) {
        CityVisitDto createdCityVisit = cityVisitFacade.createCityVisit(cityVisitDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdCityVisit);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update a city visit")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "City visit updated successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input data", content = @Content(mediaType = "application/problem+json")),
            @ApiResponse(responseCode = "404", description = "City visit not found", content = @Content(mediaType = "application/problem+json")),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content(mediaType = "application/json"))
    })
    public ResponseEntity<CityVisitDto> updateCityVisit(
            @Parameter(description = "City Visit ID", required = true) @PathVariable Long id,
            @Parameter(description = "Updated City Visit", required = true) @Valid @RequestBody CityVisitDto cityVisitDTO) {
        CityVisitDto updatedCityVisit = cityVisitFacade.updateCityVisit(id, cityVisitDTO);
        return ResponseEntity.ok(updatedCityVisit);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a city visit")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "City visit deleted successfully"),
            @ApiResponse(responseCode = "404", description = "City visit not found", content = @Content(mediaType = "application/problem+json"))
    })
    public ResponseEntity<Void> deleteCityVisit(
            @Parameter(description = "City Visit ID", required = true) @PathVariable Long id) {
        cityVisitFacade.deleteCityVisit(id);
        return ResponseEntity.notFound().build();
    }
}
