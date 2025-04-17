package cz.muni.fi.bandmanagementservice.band.repository;

import cz.muni.fi.bandmanagementservice.band.model.BandOffer;

import java.util.Collection;
import java.util.Optional;

/**
 * @author Tomáš MAREK
 */
public interface BandOfferRepository {
    public Collection<BandOffer> getAllBandOffers();

    public Optional<BandOffer> getBandOfferById(Long id);

    public BandOffer createBandOffer(BandOffer bandOffer);

    public BandOffer updateBandOffer(BandOffer bandOffer);

    public void deleteBandOffer(BandOffer bandOffer);

    public boolean pendingBandOfferExists(Long invitedMusicianId, Long bandId);
}
