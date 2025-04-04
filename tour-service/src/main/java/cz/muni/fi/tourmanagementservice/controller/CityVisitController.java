package cz.muni.fi.tourmanagementservice.controller;


import cz.muni.fi.tourmanagementservice.dto.CityVisitDTO;
import cz.muni.fi.tourmanagementservice.facades.CityVisitFacade;
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

    private final CityVisitFacade cityVisitFacade;

    @Autowired
    public CityVisitController(CityVisitFacade cityVisitFacade) {
        this.cityVisitFacade = cityVisitFacade;
    }

    @GetMapping
    public ResponseEntity<List<CityVisitDTO>> getAllCityVisits() {
        return ResponseEntity.ok(cityVisitFacade.getAllCityVisits());
    }

    @GetMapping("/tour/{tourId}")
    public ResponseEntity<List<CityVisitDTO>> getCityVisitsByTour(@PathVariable Long tourId) {
        return ResponseEntity.ok(cityVisitFacade.getCityVisitsByTour(tourId));
    }

    @GetMapping("/{id}")
    public ResponseEntity<CityVisitDTO> getCityVisitById(@PathVariable Long id) {
        return ResponseEntity.ok(cityVisitFacade.getCityVisitById(id));
    }


    @PostMapping
    public ResponseEntity<CityVisitDTO> createCityVisit(@RequestBody CityVisitDTO cityVisitDTO) {
        CityVisitDTO createdCityVisit = cityVisitFacade.createCityVisit(cityVisitDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdCityVisit);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CityVisitDTO> updateCityVisit(@PathVariable Long id, @RequestBody CityVisitDTO cityVisitDTO) {
        CityVisitDTO updatedCityVisit = cityVisitFacade.updateCityVisit(id, cityVisitDTO);
        return ResponseEntity.ok(updatedCityVisit);
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCityVisit(@PathVariable Long id) {
        cityVisitFacade.deleteCityVisit(id);
        return ResponseEntity.notFound().build();
    }
}
