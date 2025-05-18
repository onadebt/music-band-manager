package cz.muni.fi.bandmanagementservice.rest;

import cz.muni.fi.bandmanagementservice.config.OpenAPIConfig;
import cz.muni.fi.bandmanagementservice.dto.BandOfferDto;
import cz.muni.fi.bandmanagementservice.facade.BandOfferFacade;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
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
public class BandOfferController {
    private static final String GENERAL_SCOPE = "test_1";
    private static final String MANAGER_SCOPE = "test_2";
    private static final String MUSICIAN_SCOPE = "test_3";

    private final BandOfferFacade bandOfferFacade;

    @Autowired
    BandOfferController(BandOfferFacade bandOfferFacade){
        this.bandOfferFacade = bandOfferFacade;
    }

    @PostMapping
    @Operation(summary = "Published new offer to join band")
    @SecurityRequirement(
            name = OpenAPIConfig.SECURITY_SCHEME_NAME,
            scopes = {MANAGER_SCOPE}
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Band Offer created", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "400", description = "Cannot create given band offer"),
            @ApiResponse(responseCode = "404", description = "Band not found")
    })
    public ResponseEntity<BandOfferDto> createBandOffer(@RequestParam Long bandId,
                                                        @RequestParam Long invitedMusicianId,
                                                        @RequestParam Long offeringManagerId) {

        return new ResponseEntity<>(bandOfferFacade.postBandOffer(bandId, invitedMusicianId, offeringManagerId), HttpStatus.CREATED);
    }

    @GetMapping("/{offerId}")
    @Operation(summary = "Gets required offer")
    @SecurityRequirement(
            name = OpenAPIConfig.SECURITY_SCHEME_NAME,
            scopes = {MUSICIAN_SCOPE}
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "OK", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "404", description = "Band offer not found")
    })
    public ResponseEntity<BandOfferDto> getBandOffer(@PathVariable Long offerId) {
        return new ResponseEntity<>(bandOfferFacade.getBandOffer(offerId), HttpStatus.OK);
    }

    @PostMapping("/{offerId}/accept")
    @Operation(summary = "Accepts offer")
    @SecurityRequirement(
            name = OpenAPIConfig.SECURITY_SCHEME_NAME,
            scopes = {MUSICIAN_SCOPE}
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Band offer accepted", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "400", description = "Band offer could not be accepted"),
            @ApiResponse(responseCode = "404", description = "Band offer not found")
    })
    public ResponseEntity<BandOfferDto> acceptBandOffer(@PathVariable Long offerId) {
        return new ResponseEntity<>(bandOfferFacade.acceptBandOffer(offerId), HttpStatus.OK);
    }

    @PostMapping("/{offerId}/reject")
    @Operation(summary = "Rejects offer")
    @SecurityRequirement(
            name = OpenAPIConfig.SECURITY_SCHEME_NAME,
            scopes = {MUSICIAN_SCOPE}
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Band offer rejected", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "400", description = "Band offer could not be rejected"),
            @ApiResponse(responseCode = "404", description = "Band offer not found")
    })
    public ResponseEntity<BandOfferDto> rejectBandOffer(@PathVariable Long offerId) {
        return new ResponseEntity<>(bandOfferFacade.rejectBandOffer(offerId), HttpStatus.CREATED);
    }

    @PostMapping("/{offerId}/revokes")
    @Operation(summary = "Revokes offer")
    @SecurityRequirement(
            name = OpenAPIConfig.SECURITY_SCHEME_NAME,
            scopes = {MANAGER_SCOPE}
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Band offer revoked", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "400", description = "Band offer could not be revoked"),
            @ApiResponse(responseCode = "404", description = "Band offer not found")
    })
    public ResponseEntity<Void> revokeBandOffer(@PathVariable Long offerId) {
        bandOfferFacade.revokeOffer(offerId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping
    @Operation(summary = "Returns all stored offer")
    @SecurityRequirement(
            name = OpenAPIConfig.SECURITY_SCHEME_NAME,
            scopes = {MANAGER_SCOPE, MUSICIAN_SCOPE}
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK", content = @Content(mediaType = "application/json"))
    })
    public ResponseEntity<List<BandOfferDto>> getAllBandOffers() {
        return new ResponseEntity<>(bandOfferFacade.getAllBandOffers(), HttpStatus.OK);
    }

    @GetMapping("/byBand/{bandId}")
    @Operation(summary = "Returns all band offers for by given band")
    @SecurityRequirement(
            name = OpenAPIConfig.SECURITY_SCHEME_NAME,
            scopes = {MANAGER_SCOPE, MUSICIAN_SCOPE}
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK", content = @Content(mediaType = "application/json"))
    })
    public ResponseEntity<List<BandOfferDto>> getBandOffersByBand(@PathVariable Long bandId) {
        return new ResponseEntity<>(bandOfferFacade.getBandOffersByBandId(bandId), HttpStatus.OK);
    }

    @GetMapping("/byMusician/{musicianId}")
    @Operation(summary = "Returns all band offers created for given musician")
    @SecurityRequirement(
            name = OpenAPIConfig.SECURITY_SCHEME_NAME,
            scopes = {MUSICIAN_SCOPE, MANAGER_SCOPE}
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK", content = @Content(mediaType = "application/json"))
    })
    public ResponseEntity<List<BandOfferDto>> getBandOffersByMusician(@PathVariable Long musicianId) {
        return new ResponseEntity<>(bandOfferFacade.getBandOffersByInvitedMusicianId(musicianId), HttpStatus.OK);
    }
}
