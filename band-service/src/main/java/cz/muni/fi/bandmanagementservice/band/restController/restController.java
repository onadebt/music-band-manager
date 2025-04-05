package cz.muni.fi.bandmanagementservice.band.restController;

import cz.muni.fi.bandmanagementservice.band.exceptions.InvalidOperationException;
import cz.muni.fi.bandmanagementservice.band.exceptions.ResourceNotFoundException;
import cz.muni.fi.bandmanagementservice.band.facade.BandFacade;
import cz.muni.fi.bandmanagementservice.band.facade.BandOfferFacade;
import cz.muni.fi.bandmanagementservice.band.api.BandServiceApiDelegate;
import  cz.muni.fi.bandmanagementservice.band.model.BandDto;
import cz.muni.fi.bandmanagementservice.band.model.BandInfoUpdateRequest;
import cz.muni.fi.bandmanagementservice.band.model.BandOfferDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.List;

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
    public ResponseEntity<BandDto> updateBand(BandInfoUpdateRequest bandInfoUpdateRequest) {
        try {
            return new ResponseEntity<>(bandFacade.updateBand(bandInfoUpdateRequest), HttpStatus.CREATED);
        } catch (ResourceNotFoundException e){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @Override
    public ResponseEntity<List<BandDto>> getAllBands() {
        return new ResponseEntity<>(bandFacade.getAllBands(), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<List<BandOfferDto>> getAllBandOffers() {
        return new ResponseEntity<>(bandOfferFacade.getAllBandOffers(), HttpStatus.OK);
    }


    @Override
    public ResponseEntity<List<BandOfferDto>> getBandOffersByMusician(Long id) {
        return new ResponseEntity<>(bandOfferFacade.getBandOffersByInvitedMusicianId(id), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Void> revokeBandOffer(Long offerId) {
        try {
            bandOfferFacade.revokeOffer(offerId);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (ResourceNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (InvalidOperationException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @Override
    public ResponseEntity<List<BandOfferDto>> getBandOffersByBand(Long id) {
        return new ResponseEntity<>(bandOfferFacade.getBandOffersByBandId(id), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<BandDto> removeMember(Long bandId, Long memberId) {
        try {
            return new ResponseEntity<>(bandFacade.removeMember(bandId, memberId), HttpStatus.CREATED);
        } catch (ResourceNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (InvalidOperationException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }
}
