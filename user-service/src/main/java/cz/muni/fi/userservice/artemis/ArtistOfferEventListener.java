package cz.muni.fi.userservice.artemis;

import cz.muni.fi.events.bandoffer.BandOfferAcceptedEvent;
import cz.muni.fi.userservice.service.ArtistService;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class ArtistOfferEventListener {

    private final ArtistService artistService;

    public ArtistOfferEventListener(ArtistService artistService) {
        this.artistService = artistService;
    }

    @JmsListener(destination = "offer-accepted")
    @Transactional
    public void handleArtistOfferAccepted(BandOfferAcceptedEvent event) {
        System.out.println("Received eventttttttttttttttttttttttttttttt");
        artistService.linkArtistToBand(event.getInvitedMusicianId(), event.getBandId());
        System.out.println("processed eventtttttttttttttttttttttttttttttt");
    }
}