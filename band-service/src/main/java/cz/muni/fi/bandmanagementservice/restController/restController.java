package cz.muni.fi.bandmanagementservice.restController;

import cz.muni.fi.bandmanagementservice.exceptions.InvalidOperationException;
import cz.muni.fi.bandmanagementservice.exceptions.ResourceNotFoundException;
import cz.muni.fi.bandmanagementservice.facade.BandFacade;
import cz.muni.fi.bandmanagementservice.facade.BandOfferFacade;
import cz.muni.fi.generated.band.api.BandServiceApiDelegate;
import  cz.muni.fi.generated.band.model.BandDto;
import cz.muni.fi.generated.band.model.BandInfoUpdateRequest;
import cz.muni.fi.generated.band.model.BandOfferDto;
import cz.muni.fi.generated.band.model.ErrorMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.security.InvalidParameterException;
import java.util.Optional;

/**
 * @author Tomáš MAREK
 */
@Component
public class restController implements BandServiceApiDelegate {
    private final BandFacade bandFacade;
    private final BandOfferFacade bandOfferFacade;

    @Autowired
    restController(BandFacade bandFacade, BandOfferFacade bandOfferFacade) {
        this.bandFacade = bandFacade;
        this.bandOfferFacade = bandOfferFacade;
    }

    @Override
public ResponseEntity<BandOfferDto> acceptBandOffer(Long offerId) {
        try {
            return new ResponseEntity<>(bandOfferFacade.acceptBandOffer(offerId), HttpStatus.CREATED);
        } catch (InvalidOperationException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } catch (ResourceNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @Override
    public ResponseEntity<BandDto> createBand(String name, String musicalStyle, Long managerId) {
        return new ResponseEntity<>(bandFacade.createBand(name, musicalStyle, managerId), HttpStatus.CREATED);
    }

    @Override
    public ResponseEntity<BandDto> getBand(Long id) {
        try {
            return new ResponseEntity<>(bandFacade.getBand(id), HttpStatus.OK);
        } catch (ResourceNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @Override
    public ResponseEntity<BandOfferDto> getBandOffer(Long offerId) {
        try {
            return new ResponseEntity<>(bandOfferFacade.getBandOffer(offerId), HttpStatus.OK);
        } catch (ResourceNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @Override
    public ResponseEntity<BandOfferDto> postBandOffer(Long bandId, Long invitedMusicianId, Long offeringManagerId) {
        try {
            return new ResponseEntity<>(bandOfferFacade.postBandOffer(bandId, invitedMusicianId, offeringManagerId), HttpStatus.CREATED);
        } catch (InvalidOperationException e){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @Override
    public ResponseEntity<BandOfferDto> rejectBandOffer(Long offerId) {
        try {
            return new ResponseEntity<>(bandOfferFacade.rejectBandOffer(offerId), HttpStatus.CREATED);
        } catch (InvalidOperationException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } catch (ResourceNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @Override
    public ResponseEntity<BandDto> updateBand(Long id, BandInfoUpdateRequest bandInfoUpdateRequest) {
        try {
            return new ResponseEntity<>(bandFacade.updateBand(id, bandInfoUpdateRequest), HttpStatus.CREATED);
        } catch (ResourceNotFoundException e){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
