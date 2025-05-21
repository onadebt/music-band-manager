package cz.muni.fi.userservice.artemis;

import cz.muni.fi.events.bandoffer.BandOfferAcceptCommand;
import cz.muni.fi.events.bandoffer.BandOfferAcceptFailedEvent;
import cz.muni.fi.events.bandoffer.BandOfferAcceptOkEvent;
import cz.muni.fi.userservice.model.Artist;
import cz.muni.fi.userservice.repository.ArtistRepository;
import cz.muni.fi.userservice.service.interfaces.ArtistService;
import lombok.AllArgsConstructor;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.support.JmsHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

import static cz.muni.fi.events.bandoffer.BandOfferEvents.*;

@Component
@AllArgsConstructor
public class BandOfferEventListener {
    private final JmsTemplate jms;
    private ArtistRepository artistRepository;
    private ArtistService artistService;

    @Transactional
    @JmsListener(destination = BAND_OFFER_ACCEPT_CMD)
    public void handle(BandOfferAcceptCommand cmd,
                       @Header(JmsHeaders.CORRELATION_ID) String sagaId) {
        Optional<Artist> opt = artistRepository.findById(cmd.getInvitedMusicianId());
        if (opt.isEmpty()) {
            sendFailed(cmd, sagaId);
            return;
        }

        artistService.linkArtistToBand(cmd.getBandId(), opt.get().getId());

        jms.convertAndSend(BAND_OFFER_ACCEPT_OK_EVENT,
                new BandOfferAcceptOkEvent(
                        cmd.getBandOfferId(),
                        cmd.getBandId(),
                        cmd.getInvitedMusicianId(),
                        cmd.getOfferingManagerId(),
                        UUID.fromString(sagaId)
                ));
    }

    private void sendFailed(BandOfferAcceptCommand cmd, String sagaId) {
        jms.convertAndSend(BAND_OFFER_ACCEPT_FAILED_EVENT,
                new BandOfferAcceptFailedEvent(
                        cmd.getBandOfferId(),
                        cmd.getBandId(),
                        cmd.getInvitedMusicianId(),
                        cmd.getOfferingManagerId(),
                        UUID.fromString(sagaId)
                ));
    }
}