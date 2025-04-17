package cz.muni.fi.bandmanagementservice.band.rest;

import cz.muni.fi.bandmanagementservice.band.exceptions.InvalidOperationException;
import cz.muni.fi.bandmanagementservice.band.exceptions.ResourceNotFoundException;
import cz.muni.fi.bandmanagementservice.band.facade.BandFacade;
import cz.muni.fi.bandmanagementservice.band.dto.BandDto;
import cz.muni.fi.bandmanagementservice.band.dto.BandInfoUpdateRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author Tomáš MAREK
 */
@RestController
@RequestMapping("/api/bands")
@Tag(name = "Band API", description = "API for managing bands")
public class BandRestController {
    private final BandFacade bandFacade;

    @Autowired
    BandRestController(BandFacade facade) {
        this.bandFacade = facade;
    }

    @PostMapping("/create")
    @Operation(summary = "Creates a new band")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Band created", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "400", description = "Cannot create given band")
    })
    public ResponseEntity<BandDto> createBand(@RequestParam String name,
                                              @RequestParam String musicalStyle,
                                              @RequestParam Long managerId) {
        return new ResponseEntity<>(bandFacade.createBand(name, musicalStyle, managerId), HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Returns searched band")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "404", description = "Band not found")
    })
    public ResponseEntity<BandDto> getBand(@PathVariable Long id) {
        try {
            return new ResponseEntity<>(bandFacade.getBand(id), HttpStatus.OK);
        } catch (ResourceNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/update")
    @Operation(summary = "Update basic band info - managerId, name, logo, musical style")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Band updated", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "404", description = "Updated band not found"),
            @ApiResponse(responseCode = "400", description = "Band could not be updated")
    })
    public ResponseEntity<BandDto> updateBand(@RequestBody BandInfoUpdateRequest bandInfoUpdateRequest) {
        try {
            return new ResponseEntity<>(bandFacade.updateBand(bandInfoUpdateRequest), HttpStatus.CREATED);
        } catch (ResourceNotFoundException e){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/all")
    @Operation(summary = "Returns all bands")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK", content=@Content(mediaType = "application/json"))
    })
    public ResponseEntity<List<BandDto>> getAllBands() {
        return new ResponseEntity<>(bandFacade.getAllBands(), HttpStatus.OK);
    }

    @PostMapping("/{bandId}/removeMember/{memberId}")
    @Operation(summary = "Remove member from the band")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Member removed", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "400", description = "Member could not be removed"),
            @ApiResponse(responseCode = "404", description = "Band doest not exist")
    })
    public ResponseEntity<BandDto> removeMember(@PathVariable Long bandId, @PathVariable Long memberId) {
        try {
            return new ResponseEntity<>(bandFacade.removeMember(bandId, memberId), HttpStatus.CREATED);
        } catch (ResourceNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (InvalidOperationException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }
}
