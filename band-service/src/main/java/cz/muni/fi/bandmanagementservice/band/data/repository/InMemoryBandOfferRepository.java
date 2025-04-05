package cz.muni.fi.bandmanagementservice.band.data.repository;

import cz.muni.fi.bandmanagementservice.band.data.model.Band;
import cz.muni.fi.bandmanagementservice.band.data.model.BandOfferStatus;
import cz.muni.fi.bandmanagementservice.band.exceptions.DataStorageException;
import cz.muni.fi.bandmanagementservice.band.data.model.BandOffer;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * @author Tomáš MAREK
 */
@Component
public class InMemoryBandOfferRepository implements BandOfferRepository {
    private Long nextId = 1L;
    private final Map<Long, BandOffer> bandOffers = new HashMap<>();


    @Override
    public Collection<BandOffer> getAllBandOffers() {
        return bandOffers.values();
    }

    @Override
    public Optional<BandOffer> getBandOfferById(Long id) {
        if (bandOffers.containsKey(id)) {
            return Optional.of(bandOffers.get(id));
        }
        return Optional.empty();
    }

    @Override
    public BandOffer createBandOffer(BandOffer bandOffer) {
        if (bandOffer.getId() != null) {
            throw new DataStorageException("Cannot create bandOffer which already has id");
        }
        bandOffer.setId(nextId++);
        bandOffers.put(bandOffer.getId(), bandOffer);
        return bandOffer;
    }

    @Override
    public BandOffer updateBandOffer(BandOffer bandOffer) {
        verifyBandOfferExist(bandOffer);
        bandOffers.put(bandOffer.getId(), bandOffer);
        return bandOffer;
    }

    @Override
    public void deleteBandOffer(BandOffer bandOffer) {
        verifyBandOfferExist(bandOffer);
        bandOffers.remove(bandOffer.getId());
    }

    @Override
    public boolean pendingBandOfferExists(Long invitedMusicianId, Long bandId) {
        return getAllBandOffers().stream().anyMatch(
                o -> o.getBandId().equals(bandId)
                        && o.getInvitedMusicianId().equals(invitedMusicianId)
                        && o.getStatus().equals(BandOfferStatus.PENDING));
    }

    private void verifyBandOfferExist(BandOffer bandOffer) {
        if (!bandOffers.containsKey(bandOffer.getId())) {
            throw new DataStorageException("Updated band offer with id %d not found!".formatted(bandOffer.getId()));
        }
    }
}
