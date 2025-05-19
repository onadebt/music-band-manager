package cz.muni.fi.bandmanagementservice.saga;

import cz.muni.fi.bandmanagementservice.exceptions.ResourceNotFoundException;
import cz.muni.fi.bandmanagementservice.model.Band;
import cz.muni.fi.bandmanagementservice.repository.BandRepository;
import cz.muni.fi.bandmanagementservice.service.BandService;
import cz.muni.fi.events.band.*;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;
import static cz.muni.fi.events.band.BandEvents.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class BandMemberSaga {
    private final JmsTemplate jmsTemplate;
    private final BandRepository bandRepository;
    private final BandService bandService;

    @Transactional
    public Band startAddMember(Long bandId, Long memberId) {
        Optional<Band> optionalBand = bandRepository.findById(bandId);
        if (optionalBand.isEmpty()) {
            throw new ResourceNotFoundException("Band with id " + bandId + " not found");
        }

        UUID sagaId = UUID.randomUUID();
        jmsTemplate.convertAndSend(BAND_LINK_ARTIST_CMD,
                new LinkArtistToBandCommand(bandId, memberId, sagaId),
                message -> {
                    message.setJMSCorrelationID(sagaId.toString());
                    return message;
                });

        return optionalBand.get();
    }

    @Transactional
    @JmsListener(destination = USER_ARTIST_LINKED_OK_EVENT)
    public void onLinkOk(ArtistLinkedOkEvent e) {
        bandService.addMember(e.getBandId(), e.getArtistId());
    }


    @JmsListener(destination = USER_ARTIST_LINKED_FAILED_EVENT)
    public void onLinkFailed(ArtistLinkFailedEvent e) {
        log.warn("Artist {} not linked to band {} – saga aborted", e.getArtistId(), e.getBandId());
    }


    @Transactional
    public Band startRemoveMember(Long bandId, Long memberId) {
        Optional<Band> optionalBand = bandRepository.findById(bandId);
        if (optionalBand.isEmpty()) {
            throw new ResourceNotFoundException("Band with id " + bandId + " not found");
        }

        UUID sagaId = UUID.randomUUID();
        jmsTemplate.convertAndSend(BAND_UNLINK_ARTIST_CMD,
                new UnlinkArtistFromBandCommand(bandId, memberId, sagaId),
                message -> {
                    message.setJMSCorrelationID(sagaId.toString());
                    return message;
                });

        return optionalBand.get();
    }

    @Transactional
    @JmsListener(destination = USER_ARTIST_UNLINKED_OK_EVENT)
    public void onUnlinkOk(ArtistUnlinkedOkEvent e) {
        bandService.removeMember(e.getBandId(), e.getArtistId());
    }

    @JmsListener(destination = USER_ARTIST_UNLINKED_FAILED_EVENT)
    public void onUnlinkFailed(ArtistUnlinkedFailedEvent e) {
        log.warn("Artist {} not unlinked from band {} – saga aborted", e.getArtistId(), e.getBandId());
    }
}
