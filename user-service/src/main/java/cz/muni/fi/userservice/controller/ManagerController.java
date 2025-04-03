package cz.muni.fi.userservice.controller;

import cz.muni.fi.userservice.dto.ManagerDTO;
import cz.muni.fi.userservice.facade.ManagerFacade;
import cz.muni.fi.userservice.mappers.ManagerMapper;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/managers")
public class ManagerController {
    @Autowired
    private ManagerFacade managerFacade;

    @PostMapping("/register")
    public ResponseEntity<ManagerDTO> register(@Valid @RequestBody ManagerDTO managerDTO) {
        return ResponseEntity.ok(managerFacade.register(managerDTO));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable Long id) {
        managerFacade.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<ManagerDTO> getById(@PathVariable Long id) {
        return ResponseEntity.ok(managerFacade.findById(id));
    }

    @GetMapping("/all")
    public ResponseEntity<List<ManagerDTO>> getAllManagers() {
        return ResponseEntity.ok(managerFacade.findAll());
    }
}
