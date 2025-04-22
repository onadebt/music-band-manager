package cz.muni.fi.bandmanagementservice.repository;

import cz.muni.fi.bandmanagementservice.model.BandOffer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * @author Tomáš MAREK
 */
@Repository
public interface BandOfferRepository extends JpaRepository<BandOffer, Long> {
    Optional<BandOffer> findByBandIdAndInvitedMusicianId(Long invitedMusicianId, Long bandId);

    List<BandOffer> findByBandId(Long bandId);

    List<BandOffer> findByInvitedMusicianId(Long invitedMusicianId);
}
