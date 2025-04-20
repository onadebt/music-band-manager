package cz.muni.fi.bandmanagementservice.band.repository;

import cz.muni.fi.bandmanagementservice.band.model.BandOffer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

/**
 * @author Tomáš MAREK
 */
public interface BandOfferRepository extends JpaRepository<BandOffer, Long> {
    Optional<BandOffer> findByBandIdAndInvitedMusicianId(Long invitedMusicianId, Long bandId);

    List<BandOffer> findByBandId(Long bandId);

    List<BandOffer> findByInvitedMusicianId(Long invitedMusicianId);
}
