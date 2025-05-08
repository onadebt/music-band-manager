package cz.muni.fi.bandmanagementservice.repository;

import cz.muni.fi.bandmanagementservice.TestDataFactory;
import cz.muni.fi.bandmanagementservice.model.BandOffer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
public class BandOfferRepositoryTest {

    @Autowired
    BandOfferRepository bandOfferRepository;

    @BeforeEach
    void setUp() {
        bandOfferRepository.deleteAll();
    }

    @Test
    void findByBandIdAndInvitedMusicianId_bandOfferFound_returnsBandOffer() {
        // Arrange
        BandOffer bandOffer = TestDataFactory.setUpBandOffer1();
        bandOffer.setId(null);
        bandOfferRepository.save(bandOffer);

        // Act
        var foundBandOffer = bandOfferRepository.
                findByBandIdAndInvitedMusicianId(bandOffer.getInvitedMusicianId(), bandOffer.getBandId());

        // Assert
        assertTrue(foundBandOffer.isPresent());
    }

    @Test
    void findByBandIdAndInvitedMusicianId_bandOfferNotFound_returnsEmpty() {
        // Arrange
        BandOffer bandOffer = TestDataFactory.setUpBandOffer1();
        bandOffer.setId(null);
        bandOfferRepository.save(bandOffer);
        Long nonExistentBandId = -999L;
        Long nonExistentMusicianId = -999L;

        // Act
        var foundBandOffer = bandOfferRepository.
                findByBandIdAndInvitedMusicianId(nonExistentMusicianId, nonExistentBandId);

        // Assert
        assertTrue(foundBandOffer.isEmpty());
    }

    @Test
    void findByBandId_bandOffersFound_returnsBandOffers() {
        // Arrange
        BandOffer bandOffer1 = TestDataFactory.setUpBandOffer1();
        BandOffer bandOffer2 = TestDataFactory.setUpBandOffer1();
        bandOffer1.setId(null);
        bandOffer2.setId(null);
        bandOfferRepository.save(bandOffer1);
        bandOfferRepository.save(bandOffer2);

        // Act
        var foundBandOffers = bandOfferRepository.findByBandId(bandOffer1.getBandId());

        // Assert
        assertTrue(foundBandOffers.size() == 2);
    }

    @Test
    void findByBandId_bandOffersNotFound_returnsEmpty() {
        // Arrange
        BandOffer bandOffer = TestDataFactory.setUpBandOffer1();
        bandOffer.setId(null);
        bandOfferRepository.save(bandOffer);
        Long nonExistentBandId = -999L;

        // Act
        var foundBandOffers = bandOfferRepository.findByBandId(nonExistentBandId);

        // Assert
        assertTrue(foundBandOffers.isEmpty());
    }

    @Test
    void findByInvitedMusicianId_bandOffersFound_returnsBandOffers() {
        // Arrange
        BandOffer bandOffer1 = TestDataFactory.setUpBandOffer1();
        BandOffer bandOffer2 = TestDataFactory.setUpBandOffer1();
        bandOffer1.setId(null);
        bandOffer2.setId(null);
        bandOfferRepository.save(bandOffer1);
        bandOfferRepository.save(bandOffer2);

        // Act
        var foundBandOffers = bandOfferRepository.findByInvitedMusicianId(bandOffer1.getInvitedMusicianId());

        // Assert
        assertTrue(foundBandOffers.size() == 2);
    }

    @Test
    void findByInvitedMusicianId_bandOffersNotFound_returnsEmpty() {
        // Arrange
        BandOffer bandOffer = TestDataFactory.setUpBandOffer1();
        bandOffer.setId(null);
        bandOfferRepository.save(bandOffer);
        Long nonExistentMusicianId = -999L;

        // Act
        var foundBandOffers = bandOfferRepository.findByInvitedMusicianId(nonExistentMusicianId);

        // Assert
        assertTrue(foundBandOffers.isEmpty());
    }
}
