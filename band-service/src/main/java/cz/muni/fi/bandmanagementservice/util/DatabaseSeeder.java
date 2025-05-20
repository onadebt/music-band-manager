package cz.muni.fi.bandmanagementservice.util;

import cz.muni.fi.bandmanagementservice.model.Band;
import cz.muni.fi.bandmanagementservice.model.BandOffer;
import cz.muni.fi.bandmanagementservice.repository.BandOfferRepository;
import cz.muni.fi.bandmanagementservice.repository.BandRepository;
import cz.muni.fi.shared.enums.BandOfferStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class DatabaseSeeder implements CommandLineRunner {

    private final BandRepository bandRepository;
    private final BandOfferRepository bandOfferRepository;

    private final Environment environment;


    @Override
    public void run(String... args) {
        boolean shouldSeed = Boolean.parseBoolean(environment.getProperty("app.db.seed", "false"));
        boolean shouldClear = Boolean.parseBoolean(environment.getProperty("app.db.clear", "false"));

        if (shouldClear) {
            bandOfferRepository.deleteAll();
            bandRepository.deleteAll();
            System.out.println("Database cleared.");
        }

        if (shouldSeed) {
            Band band1 = new Band();
            band1.setName("TameImpalaBand");
            band1.setMusicalStyle("Psychedelic Rock");
            band1.setManagerId(1L);
            band1.setLogoUrl("https://example.com/tame_impala_logo.png");

            Band band2 = new Band();
            band2.setName("JpegMafiaBand");
            band2.setMusicalStyle("Hip-Hop");
            band2.setManagerId(2L);
            band2.setLogoUrl("https://example.com/jpeg_mafia_logo.png");

            Band band3 = new Band();
            band3.setName("FredAgainBand");
            band3.setMusicalStyle("Electronic");
            band3.setManagerId(3L);
            band3.setLogoUrl("https://example.com/fred_again_logo.png");

            BandOffer bandOffer1 = new BandOffer();
            bandOffer1.setBandId(1L);
            bandOffer1.setInvitedMusicianId(1L);
            bandOffer1.setOfferingManagerId(1L);
            bandOffer1.setStatus(BandOfferStatus.PENDING);

            BandOffer bandOffer2 = new BandOffer();
            bandOffer2.setBandId(2L);
            bandOffer2.setInvitedMusicianId(2L);
            bandOffer2.setOfferingManagerId(2L);
            bandOffer2.setStatus(BandOfferStatus.PENDING);

            BandOffer bandOffer3 = new BandOffer();
            bandOffer3.setBandId(3L);
            bandOffer3.setInvitedMusicianId(3L);
            bandOffer3.setOfferingManagerId(3L);
            bandOffer3.setStatus(BandOfferStatus.PENDING);

            bandRepository.saveAll(List.of(band1, band2, band3));
            bandOfferRepository.saveAll(List.of(bandOffer1, bandOffer2, bandOffer3));
        }
    }
}
