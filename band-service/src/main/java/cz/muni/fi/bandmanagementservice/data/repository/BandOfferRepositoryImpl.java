package cz.muni.fi.bandmanagementservice.data.repository;

import cz.muni.fi.bandmanagementservice.exceptions.DataStorageException;
import cz.muni.fi.bandmanagementservice.data.model.BandOffer;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * @author Tomáš MAREK
 */
public class BandOfferRepositoryImpl implements BandOfferRepository {
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
    public void createBandOffer(BandOffer bandOffer) {
        bandOffer.setBandId(nextId++);
        bandOffers.put(bandOffer.getBandId(), bandOffer);
    }

    @Override
    public void updateBandOffer(BandOffer bandOffer) {
        verifyBandOfferExist(bandOffer);
        bandOffers.put(bandOffer.getBandId(), bandOffer);
    }

    @Override
    public void deleteBandOffer(BandOffer bandOffer) {
        verifyBandOfferExist(bandOffer);
        bandOffers.remove(bandOffer.getBandId());
    }

    private void verifyBandOfferExist(BandOffer bandOffer) {
        if (!bandOffers.containsKey(bandOffer.getBandId())) {
            throw new DataStorageException("Updated band offer with id %d not found!".formatted(bandOffer.getId()));
        }
    }
}
