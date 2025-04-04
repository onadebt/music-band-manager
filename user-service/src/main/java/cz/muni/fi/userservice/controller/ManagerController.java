package cz.muni.fi.userservice.controller;

import cz.muni.fi.userservice.dto.ManagerDTO;
import cz.muni.fi.userservice.facade.ManagerFacade;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/api/managers")
@Tag(name = "Manager API", description = "Manager management API")
public class ManagerController {
    @Autowired
    private ManagerFacade managerFacade;

    @Operation(summary = "Register a new manager")
    @PostMapping("/register")
    public ResponseEntity<ManagerDTO> register(@Valid @RequestBody ManagerDTO managerDTO) {
        return ResponseEntity.ok(managerFacade.register(managerDTO));
    }

    @Operation(summary = "Get all managers")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable Long id) {
        managerFacade.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Get manager by ID")
    @GetMapping("/{id}")
    public ResponseEntity<ManagerDTO> getById(@PathVariable Long id) {
        return ResponseEntity.ok(managerFacade.findById(id));
    }

    @Operation(summary = "Get manager by username")
    @GetMapping("/all")
    public ResponseEntity<List<ManagerDTO>> getAllManagers() {
        return ResponseEntity.ok(managerFacade.findAll());
    }

    @Operation(summary = "Get manager by username")
    @PostMapping("/bands/{artistId}")
    public ResponseEntity<ManagerDTO> updateBands(@PathVariable Long artistId, @RequestBody Set<Long> bandIds) {
        return ResponseEntity.ok(managerFacade.updateBandIds(artistId, bandIds));
    }

    @Operation(summary = "Get managers by band IDs")
    @GetMapping("/bands")
    public ResponseEntity<List<ManagerDTO>> getManagersByBandIds(@RequestParam Set<Long> bandIds) {
        return ResponseEntity.ok(managerFacade.findByBandIds(bandIds));
    }
}
