package cz.muni.fi.bandmanagementservice.band.service;

import cz.muni.fi.bandmanagementservice.band.data.model.Band;
import cz.muni.fi.bandmanagementservice.band.data.model.BandOffer;
import cz.muni.fi.bandmanagementservice.band.data.model.BandOfferStatus;
import cz.muni.fi.bandmanagementservice.band.data.repository.BandOfferRepository;
import cz.muni.fi.bandmanagementservice.band.data.repository.BandRepository;
import cz.muni.fi.bandmanagementservice.band.exceptions.InvalidOperationException;
import cz.muni.fi.bandmanagementservice.band.exceptions.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * @author Tomáš MAREK
 */
@Service
public class BandOfferService {
    private final BandOfferRepository bandOfferRepository;
    private final BandRepository bandRepository;

    @Autowired
    public BandOfferService(BandOfferRepository bandOfferRepository, BandOfferRepository bandOfferRepository1, BandRepository bandRepository) {
        this.bandOfferRepository = bandOfferRepository;
        this.bandRepository = bandRepository;
    }

    public BandOffer getBandOffer(Long bandOfferId) {
        Optional<BandOffer> maybeBandOffer = bandOfferRepository.getBandOfferById(bandOfferId);
        if (maybeBandOffer.isEmpty()) {
            throw new ResourceNotFoundException("BandOffer with id %s does not exist!".formatted(bandOfferId));
        }
        return maybeBandOffer.get();
    }

    public BandOffer createBandOffer(Long bandId, Long invitedMusicianId, Long offeringManagerId) {
        Optional<Band> maybeOfferedBand = bandRepository.getBandById(bandId);
        if (maybeOfferedBand.isEmpty()) {
            throw new InvalidOperationException("Band with id %s does not exist".formatted(bandId));
        }
        Band offeredBand = maybeOfferedBand.get();
        if (!offeredBand.getManagerId().equals(offeringManagerId)){
            throw new InvalidOperationException("Offered can be created only by manager of the band");
        }

        // TODO verify manager and musician
        BandOffer newOffer = new BandOffer(null, bandId, invitedMusicianId, offeringManagerId);
        return bandOfferRepository.createBandOffer(newOffer);
    }

    public BandOffer acceptOffer(Long bandOfferId){
        BandOffer bandOffer = getBandOffer(bandOfferId);
        bandOffer.acceptOffer();
        BandOffer accepted = bandOfferRepository.updateBandOffer(bandOffer);

        Optional<Band> newBand = bandRepository.getBandById(bandOffer.getBandId());
        if (newBand.isEmpty()){
            throw new IllegalStateException("Band with id %d not found".formatted(bandOffer.getBandId()));
        }
        newBand.get().addMember(bandOffer.getInvitedMusicianId());
        bandRepository.updateBand(newBand.get());
        return accepted;
    }

    public BandOffer rejectOffer(Long bandOfferId){
        BandOffer bandOffer = getBandOffer(bandOfferId);
        bandOffer.rejectOffer();
        return bandOfferRepository.updateBandOffer(bandOffer);
    }

    public void revokeOffer(Long bandOfferId){
        BandOffer bandOffer = getBandOffer(bandOfferId);
        if (bandOffer.getStatus() != BandOfferStatus.PENDING){
            throw new InvalidOperationException("BandOffer was already accepted or rejected and cannot be revoked");
        }
        bandOfferRepository.deleteBandOffer(bandOffer);
    }

    public List<BandOffer> getAllBandOffers() {
        return bandOfferRepository.getAllBandOffers().stream().toList();
    }

    public List<BandOffer> getBandOffersByBandId(Long bandId) {
        return bandOfferRepository.getAllBandOffers().stream().filter(bandOffer -> bandOffer.getBandId().equals(bandId)).toList();
    }

    public List<BandOffer> getBandOfferByInvitedMusicianId(Long invitedMusicianId) {
        return bandOfferRepository.getAllBandOffers().stream().filter(bandOffer -> bandOffer.getInvitedMusicianId().equals(invitedMusicianId)).toList();
    }
}
