package cz.muni.fi.userservice.artemis;

import cz.muni.fi.events.band.*;
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

import static cz.muni.fi.events.band.BandEvents.*;

@Component
@AllArgsConstructor
public class BandEventListener {
    private final JmsTemplate jms;
    private ArtistRepository artistRepository;
    private ArtistService artistService;

    @Transactional
    @JmsListener(destination = BAND_LINK_ARTIST_CMD)
    public void handle(LinkArtistToBandCommand cmd,
                       @Header(JmsHeaders.CORRELATION_ID) String sagaId) {
        Optional<Artist> opt = artistRepository.findById(cmd.getArtistId());
        if (opt.isEmpty()) {
            sendFailed(cmd, sagaId);
            return;
        }

        artistService.linkArtistToBand(cmd.getBandId(), opt.get().getId());

        jms.convertAndSend(USER_ARTIST_LINKED_OK_EVENT,
                new ArtistLinkedOkEvent(cmd.getBandId(), cmd.getArtistId(), UUID.fromString(sagaId)));
    }

    private void sendFailed(LinkArtistToBandCommand cmd, String sagaId) {
        jms.convertAndSend(USER_ARTIST_LINKED_FAILED_EVENT,
                new ArtistLinkFailedEvent(cmd.getBandId(), cmd.getArtistId(), UUID.fromString(sagaId)));
    }

    @Transactional
    @JmsListener(destination = BAND_UNLINK_ARTIST_CMD)
    public void handle(UnlinkArtistFromBandCommand cmd,
                       @Header(JmsHeaders.CORRELATION_ID) String sagaId) {
        Optional<Artist> opt = artistRepository.findById(cmd.getArtistId());
        if (opt.isEmpty()) {
            sendFailed(cmd, sagaId);
            return;
        }

        artistService.unlinkArtistFromBand(cmd.getBandId(), opt.get().getId());

        jms.convertAndSend(USER_ARTIST_UNLINKED_OK_EVENT,
                new ArtistUnlinkedOkEvent(cmd.getBandId(), cmd.getArtistId(), UUID.fromString(sagaId)));
    }

    private void sendFailed(UnlinkArtistFromBandCommand cmd, String sagaId) {
        jms.convertAndSend(USER_ARTIST_UNLINKED_FAILED_EVENT,
                new ArtistLinkFailedEvent(cmd.getBandId(), cmd.getArtistId(), UUID.fromString(sagaId)));
    }
}
