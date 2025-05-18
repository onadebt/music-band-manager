package cz.muni.fi.bandmanagementservice.service;

import cz.muni.fi.bandmanagementservice.artemis.BandOfferEventProducer;
import cz.muni.fi.bandmanagementservice.exceptions.BandNotFoundException;
import cz.muni.fi.bandmanagementservice.exceptions.BandOfferNotFoundException;
import cz.muni.fi.bandmanagementservice.exceptions.InvalidOperationException;
import cz.muni.fi.bandmanagementservice.model.Band;
import cz.muni.fi.bandmanagementservice.model.BandOffer;
import cz.muni.fi.bandmanagementservice.repository.BandOfferRepository;
import cz.muni.fi.bandmanagementservice.repository.BandRepository;
import cz.muni.fi.events.bandoffer.BandOfferAcceptedEvent;
import cz.muni.fi.shared.enm.BandOfferStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * @author Tomáš MAREK
 */
@Transactional
@Service
public class BandOfferService {
    private final BandOfferRepository bandOfferRepository;
    private final BandRepository bandRepository;
    private final BandOfferEventProducer bandOfferEventProducer;

    @Autowired
    public BandOfferService(BandOfferRepository bandOfferRepository, BandRepository bandRepository, BandOfferEventProducer bandOfferEventProducer) {
        this.bandOfferRepository = bandOfferRepository;
        this.bandRepository = bandRepository;
        this.bandOfferEventProducer = bandOfferEventProducer;
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
        Band offeredBand = maybeOfferedBand.get();
        if (!offeredBand.getManagerId().equals(offeringManagerId)) {
            throw new InvalidOperationException("Offer can be created only by manager of the band");
        }
        if (offeredBand.getMembers().contains(invitedMusicianId)) {
            throw new InvalidOperationException("Musician is already member of the band");
        }
        if (pendingOfferExists(invitedMusicianId, bandId)) {
            throw new InvalidOperationException("There is already a pending band offer for given musician and band");
        }
        // TODO verify manager and musician
        BandOffer newOffer = new BandOffer(null, bandId, invitedMusicianId, offeringManagerId);
        return bandOfferRepository.save(newOffer);
    }

    public BandOffer acceptOffer(Long bandOfferId) {
        BandOffer bandOffer = getBandOffer(bandOfferId);
        bandOffer.acceptOffer();
        BandOffer accepted = bandOfferRepository.save(bandOffer);

        Optional<Band> newBand = bandRepository.findById(bandOffer.getBandId());
        if (newBand.isEmpty()) {
            throw new IllegalStateException("Band with id %d not found".formatted(bandOffer.getBandId()));
        }
        newBand.get().addMember(bandOffer.getInvitedMusicianId());
        bandRepository.save(newBand.get());


        bandOfferEventProducer.sendOfferAcceptedEvent(
                BandOfferAcceptedEvent.builder()
                .id(UUID.randomUUID().getMostSignificantBits() & Long.MAX_VALUE)
                .bandId(bandOffer.getBandId())
                .invitedMusicianId(bandOffer.getInvitedMusicianId())
                .offeringManagerId(bandOffer.getOfferingManagerId())
                .build()
        );

        return accepted;
    }

    public BandOffer rejectOffer(Long bandOfferId) {
        BandOffer bandOffer = getBandOffer(bandOfferId);
        bandOffer.rejectOffer();
        return bandOfferRepository.save(bandOffer);
    }

    public void revokeOffer(Long bandOfferId) {
        BandOffer bandOffer = getBandOffer(bandOfferId);
        if (bandOffer.getStatus() != BandOfferStatus.PENDING) {
            throw new InvalidOperationException("BandOffer was already accepted or rejected and cannot be revoked");
        }
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
}
