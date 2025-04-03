package cz.muni.fi.tourmanagementservice.controller;


import cz.muni.fi.tourmanagementservice.dto.CityVisitDTO;
import cz.muni.fi.tourmanagementservice.service.CityVisitService;
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
public class CityVisitController {

    private final CityVisitService cityVisitService;

    @Autowired
    public CityVisitController(CityVisitService cityVisitService) {
        this.cityVisitService = cityVisitService;
    }

    @GetMapping
    public ResponseEntity<List<CityVisitDTO>> getAllCityVisits() {
        return ResponseEntity.ok(cityVisitService.getAllCityVisits());
    }

    @GetMapping("/tour/{tourId}")
    public ResponseEntity<List<CityVisitDTO>> getCityVisitsByTour(@PathVariable Long tourId) {
        return ResponseEntity.ok(cityVisitService.getCityVisitByTour(tourId));
    }

    @GetMapping("/{id}")
    public ResponseEntity<CityVisitDTO> getCityVisitById(@PathVariable Long id) {
        return cityVisitService.getCityVisitById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }


    @PostMapping
    public ResponseEntity<CityVisitDTO> createCityVisit(@RequestBody CityVisitDTO cityVisitDTO) {
        CityVisitDTO createdCityVisit = cityVisitService.createCityVisit(cityVisitDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdCityVisit);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CityVisitDTO> updateCityVisit(@PathVariable Long id, @RequestBody CityVisitDTO cityVisitDTO) {
        CityVisitDTO updatedCityVisit = cityVisitService.updateCityVisit(id, cityVisitDTO);

        if (updatedCityVisit != null) {
            return ResponseEntity.ok(updatedCityVisit);
        }

        return ResponseEntity.notFound().build();
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCityVisit(@PathVariable Long id) {
        boolean deleted = cityVisitService.deleteCityVisit(id);

        if (deleted) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.notFound().build();
    }
}
