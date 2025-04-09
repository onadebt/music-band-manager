package cz.muni.fi.tourmanagementservice.controller;


import cz.muni.fi.tourmanagementservice.dto.TourDTO;
import cz.muni.fi.tourmanagementservice.facades.TourFacade;
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
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;


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

    @Operation(summary = "Retrieve all tours")
    @GetMapping
    public ResponseEntity<List<TourDTO>> getAllTours() {
        return ResponseEntity.ok(tourFacade.getAllTours());
    }

    @Operation(summary = "Retrieve tours by band")
    @GetMapping("/band/{bandId}")
    public ResponseEntity<List<TourDTO>> getToursByBand(@PathVariable Long bandId) {
        return ResponseEntity.ok(tourFacade.getToursByBand(bandId));
    }

    @Operation(summary = "Retrieve tours by id")
    @GetMapping("/{id}")
    public ResponseEntity<TourDTO> getTourById(@PathVariable Long id) {
        return ResponseEntity.ok(tourFacade.getTourById(id));
    }

    @Operation(summary = "Create a new tour")
    @PostMapping
    public ResponseEntity<TourDTO> createTour(@RequestBody TourDTO tourDTO) {
        TourDTO createdTour = tourFacade.createTour(tourDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdTour);
    }

    @Operation(summary = "Update a tour")
    @PutMapping("/{id}")
    public ResponseEntity<TourDTO> updateTour(@PathVariable Long id, @RequestBody TourDTO tourDTO) {
        TourDTO updatedTour = tourFacade.updateTour(id, tourDTO);
        return ResponseEntity.ok(updatedTour);
    }

    @Operation(summary = "Delete a tour")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTour(@PathVariable Long id) {
        tourFacade.deleteTour(id);
        return ResponseEntity.noContent().build();
    }
}
