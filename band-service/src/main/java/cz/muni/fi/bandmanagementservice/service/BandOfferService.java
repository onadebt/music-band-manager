package cz.muni.fi.bandmanagementservice.service;

import cz.muni.fi.bandmanagementservice.exceptions.*;
import cz.muni.fi.bandmanagementservice.model.Band;
import cz.muni.fi.bandmanagementservice.model.BandOffer;
import cz.muni.fi.bandmanagementservice.repository.BandOfferRepository;
import cz.muni.fi.bandmanagementservice.repository.BandRepository;
import cz.muni.fi.shared.enums.BandOfferStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * @author Tomáš MAREK
 */
@Transactional
@Service
public class BandOfferService {
    private final BandOfferRepository bandOfferRepository;
    private final BandRepository bandRepository;

    @Autowired
    public BandOfferService(BandOfferRepository bandOfferRepository, BandRepository bandRepository) {
        this.bandOfferRepository = bandOfferRepository;
        this.bandRepository = bandRepository;
    }

    @Transactional(readOnly = true)
    public BandOffer getBandOffer(Long bandOfferId) {
        Optional<BandOffer> maybeBandOffer = bandOfferRepository.findById(bandOfferId);
        if (maybeBandOffer.isEmpty()) {
            throw new BandOfferNotFoundException(bandOfferId);
        }
        return maybeBandOffer.get();
    }

    public BandOffer createBandOffer(Long bandId, Long invitedMusicianId, Long offeringManagerId) {
        Optional<Band> maybeOfferedBand = bandRepository.findById(bandId);
        if (maybeOfferedBand.isEmpty()) {
            throw new BandNotFoundException(bandId);
        }
        if (pendingOfferExists(invitedMusicianId, bandId)) {
            throw new BandOfferAlreadyExistsException(bandId, invitedMusicianId);
        }

        Band offeredBand = maybeOfferedBand.get();
        if (!offeredBand.getManagerId().equals(offeringManagerId)) {
            throw new InvalidManagerException(bandId, offeringManagerId);
        }
        if (offeredBand.getMembers().contains(invitedMusicianId)) {
            throw new MusicianAlreadyInBandException(bandId, invitedMusicianId);
        }
        BandOffer newOffer = new BandOffer(null, bandId, invitedMusicianId, offeringManagerId);
        return bandOfferRepository.save(newOffer);
    }

    public BandOffer acceptOffer(Long bandOfferId) {
        BandOffer bandOffer = getBandOffer(bandOfferId);
        verifyOfferIsPending(bandOffer);

        bandOffer.acceptOffer();
        BandOffer accepted = bandOfferRepository.save(bandOffer);

        Optional<Band> newBand = bandRepository.findById(bandOffer.getBandId());
        if (newBand.isEmpty()) {
            throw new BandNotFoundException(bandOffer.getBandId());
        }
        newBand.get().addMember(bandOffer.getInvitedMusicianId());
        bandRepository.save(newBand.get());

        return accepted;
    }

    public BandOffer rejectOffer(Long bandOfferId) {
        BandOffer bandOffer = getBandOffer(bandOfferId);
        verifyOfferIsPending(bandOffer);
        bandOffer.rejectOffer();
        return bandOfferRepository.save(bandOffer);
    }

    public void revokeOffer(Long bandOfferId) {
        BandOffer bandOffer = getBandOffer(bandOfferId);
        verifyOfferIsPending(bandOffer);
        bandOfferRepository.delete(bandOffer);
    }

    @Transactional(readOnly = true)
    public List<BandOffer> getAllBandOffers() {
        return bandOfferRepository.findAll();
    }

    @Transactional(readOnly = true)
    public List<BandOffer> getBandOffersByBandId(Long bandId) {
        return bandOfferRepository.findByBandId(bandId);
    }

    @Transactional(readOnly = true)
    public List<BandOffer> getBandOfferByInvitedMusicianId(Long invitedMusicianId) {
        return bandOfferRepository.findByInvitedMusicianId(invitedMusicianId);
    }

    private boolean pendingOfferExists(Long invitedMusicianId, Long bandId) {
        Optional<BandOffer> found = bandOfferRepository.findByBandIdAndInvitedMusicianId(invitedMusicianId, bandId);
        return found.filter(bandOffer -> bandOffer.getStatus() == BandOfferStatus.PENDING).isPresent();
    }

    private void verifyOfferIsPending(BandOffer offer) {
        if (!offer.getStatus().equals(BandOfferStatus.PENDING)) {
            throw new CannotManipulateOfferException(offer.getStatus());
        }
    }
}
