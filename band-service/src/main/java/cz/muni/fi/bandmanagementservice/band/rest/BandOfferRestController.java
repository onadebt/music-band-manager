package cz.muni.fi.bandmanagementservice.band.rest;

import cz.muni.fi.bandmanagementservice.band.dto.BandOfferDto;
import cz.muni.fi.bandmanagementservice.band.exceptions.InvalidOperationException;
import cz.muni.fi.bandmanagementservice.band.exceptions.ResourceNotFoundException;
import cz.muni.fi.bandmanagementservice.band.facade.BandOfferFacade;
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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author Tomáš MAREK
 */
@RestController
@RequestMapping("/api/bands/offers")
@Tag(name="Band Offer API", description = "API for managing band join offers")
public class BandOfferRestController {
    private final BandOfferFacade bandOfferFacade;

    @Autowired
    BandOfferRestController(BandOfferFacade bandOfferFacade){
        this.bandOfferFacade = bandOfferFacade;
    }

    @PostMapping("/create")
    @Operation(summary = "Published new offer to join band")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Band Offer created", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "400", description = "Cannot create given band offer")
    })
    public ResponseEntity<BandOfferDto> createBandOffer(@RequestParam Long bandId,
                                                        @RequestParam Long invitedMusicianId,
                                                        @RequestParam Long offeringManagerId) {
        try {
            return new ResponseEntity<>(bandOfferFacade.postBandOffer(bandId, invitedMusicianId, offeringManagerId), HttpStatus.CREATED);
        } catch (InvalidOperationException e){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/{offerId}")
    @Operation(summary = "Gets required offer")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "OK", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "404", description = "Band offer not found")
    })
    public ResponseEntity<BandOfferDto> getBandOffer(@PathVariable Long offerId) {
        try {
            return new ResponseEntity<>(bandOfferFacade.getBandOffer(offerId), HttpStatus.OK);
        } catch (ResourceNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/{offerId}/accept")
    @Operation(summary = "Accepts offer")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Band offer accepted", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "400", description = "Band offer could not be accepted"),
            @ApiResponse(responseCode = "404", description = "Band offer not found")
    })
    public ResponseEntity<BandOfferDto> acceptBandOffer(@PathVariable Long offerId) {
        try {
            return new ResponseEntity<>(bandOfferFacade.acceptBandOffer(offerId), HttpStatus.CREATED);
        } catch (InvalidOperationException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } catch (ResourceNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/{offerId}/reject")
    @Operation(summary = "Rejects offer")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Band offer rejected", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "400", description = "Band offer could not be rejected"),
            @ApiResponse(responseCode = "404", description = "Band offer not found")
    })
    public ResponseEntity<BandOfferDto> rejectBandOffer(@PathVariable Long offerId) {
        try {
            return new ResponseEntity<>(bandOfferFacade.rejectBandOffer(offerId), HttpStatus.CREATED);
        } catch (InvalidOperationException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } catch (ResourceNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/{offerId}/revokes")
    @Operation(summary = "Revokes offer")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Band offer revoked", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "400", description = "Band offer could not be revoked"),
            @ApiResponse(responseCode = "404", description = "Band offer not found")
    })
    public ResponseEntity<Void> revokeBandOffer(@PathVariable Long offerId) {
        try {
            bandOfferFacade.revokeOffer(offerId);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (ResourceNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (InvalidOperationException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/all")
    @Operation(summary = "Returns all stored offer")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK", content = @Content(mediaType = "application/json"))
    })
    public ResponseEntity<List<BandOfferDto>> getAllBandOffers() {
        return new ResponseEntity<>(bandOfferFacade.getAllBandOffers(), HttpStatus.OK);
    }

    @GetMapping("/byBand/{bandId}")
    @Operation(summary = "Returns all band offers for by given band")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK", content = @Content(mediaType = "application/json"))
    })
    public ResponseEntity<List<BandOfferDto>> getBandOffersByBand(@PathVariable Long bandId) {
        return new ResponseEntity<>(bandOfferFacade.getBandOffersByBandId(bandId), HttpStatus.OK);
    }

    @GetMapping("/byMusician/{musicianId}")
    @Operation(summary = "Returns all band offers created for given musician")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK", content = @Content(mediaType = "application/json"))
    })
    public ResponseEntity<List<BandOfferDto>> getBandOffersByMusician(@PathVariable Long musicianId) {
        return new ResponseEntity<>(bandOfferFacade.getBandOffersByInvitedMusicianId(musicianId), HttpStatus.OK);
    }
}
