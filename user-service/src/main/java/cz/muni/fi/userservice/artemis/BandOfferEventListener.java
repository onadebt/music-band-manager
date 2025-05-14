package cz.muni.fi.userservice.artemis;

import cz.muni.fi.events.bandoffer.BandOfferAcceptedEvent;
import cz.muni.fi.events.bandoffer.BandOfferEvents;
import cz.muni.fi.userservice.service.ArtistServiceImpl;
import lombok.AllArgsConstructor;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@AllArgsConstructor
public class BandOfferEventListener {

    private final ArtistServiceImpl artistServiceImpl;

    @Transactional
    @JmsListener(destination = BandOfferEvents.BAND_OFFER_ACCEPTED_EVENT)
    public void handleBandOfferAccepted(BandOfferAcceptedEvent event) {
        artistServiceImpl.linkArtistToBand(event.getInvitedMusicianId(), event.getBandId());
    }
}