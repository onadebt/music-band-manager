package cz.muni.fi.bandmanagementservice.band.data.repository;

import cz.muni.fi.bandmanagementservice.band.data.model.BandOffer;

import java.util.Collection;
import java.util.Optional;

/**
 * @author Tomáš MAREK
 */
public interface BandOfferRepository {
    public Collection<BandOffer> getAllBandOffers();

    public Optional<BandOffer> getBandOfferById(Long id);

    public void createBandOffer(BandOffer bandOffer);

    public void updateBandOffer(BandOffer bandOffer);

    public void deleteBandOffer(BandOffer bandOffer);
}
