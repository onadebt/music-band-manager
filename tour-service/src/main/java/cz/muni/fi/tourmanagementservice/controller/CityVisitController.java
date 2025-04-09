package cz.muni.fi.tourmanagementservice.controller;


import cz.muni.fi.tourmanagementservice.dto.CityVisitDTO;
import cz.muni.fi.tourmanagementservice.facades.CityVisitFacade;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "CityVisit API", description = "CityVisit management API")
public class CityVisitController {

    private final CityVisitFacade cityVisitFacade;

    @Autowired
    public CityVisitController(CityVisitFacade cityVisitFacade) {
        this.cityVisitFacade = cityVisitFacade;
    }

    @Operation(summary = "Retrieve all city visits")
    @GetMapping
    public ResponseEntity<List<CityVisitDTO>> getAllCityVisits() {
        return ResponseEntity.ok(cityVisitFacade.getAllCityVisits());
    }

    @Operation(summary = "Retrieve all city visits by tour")
    @GetMapping("/tour/{tourId}")
    public ResponseEntity<List<CityVisitDTO>> getCityVisitsByTour(@PathVariable Long tourId) {
        return ResponseEntity.ok(cityVisitFacade.getCityVisitsByTour(tourId));
    }

    @Operation(summary = "Retrieve all city visits by id")
    @GetMapping("/{id}")
    public ResponseEntity<CityVisitDTO> getCityVisitById(@PathVariable Long id) {
        return ResponseEntity.ok(cityVisitFacade.getCityVisitById(id));
    }


    @Operation(summary = "Create new city visit")
    @PostMapping
    public ResponseEntity<CityVisitDTO> createCityVisit(@RequestBody CityVisitDTO cityVisitDTO) {
        CityVisitDTO createdCityVisit = cityVisitFacade.createCityVisit(cityVisitDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdCityVisit);
    }

    @Operation(summary = "Update a city visit")
    @PutMapping("/{id}")
    public ResponseEntity<CityVisitDTO> updateCityVisit(@PathVariable Long id, @RequestBody CityVisitDTO cityVisitDTO) {
        CityVisitDTO updatedCityVisit = cityVisitFacade.updateCityVisit(id, cityVisitDTO);
        return ResponseEntity.ok(updatedCityVisit);
    }

    @Operation(summary = "Delete a city visit")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCityVisit(@PathVariable Long id) {
        cityVisitFacade.deleteCityVisit(id);
        return ResponseEntity.notFound().build();
    }
}
