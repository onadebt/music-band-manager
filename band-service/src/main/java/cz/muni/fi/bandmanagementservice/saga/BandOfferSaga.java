package cz.muni.fi.bandmanagementservice.saga;

import cz.muni.fi.bandmanagementservice.exceptions.BandOfferNotFoundException;
import cz.muni.fi.bandmanagementservice.exceptions.CannotManipulateOfferException;
import cz.muni.fi.bandmanagementservice.model.BandOffer;
import cz.muni.fi.bandmanagementservice.repository.BandOfferRepository;
import cz.muni.fi.bandmanagementservice.service.BandOfferService;
import cz.muni.fi.events.band.ArtistLinkedOkEvent;
import cz.muni.fi.events.band.LinkArtistToBandCommand;
import cz.muni.fi.events.bandoffer.BandOfferAcceptFailedEvent;
import cz.muni.fi.events.bandoffer.BandOfferAcceptOkEvent;
import cz.muni.fi.shared.enm.BandOfferStatus;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

import static cz.muni.fi.events.bandoffer.BandOfferEvents.BAND_OFFER_ACCEPT_FAILED_EVENT;
import static cz.muni.fi.events.bandoffer.BandOfferEvents.BAND_OFFER_ACCEPT_OK_EVENT;

@Slf4j
@Service
@RequiredArgsConstructor
public class BandOfferSaga {
    private final JmsTemplate jmsTemplate;
    private final BandOfferRepository bandOfferRepository;
    private final BandOfferService bandOfferService;

    @Transactional
    public BandOffer startAcceptBandOffer(Long bandOfferId) {
        Optional<BandOffer> optionalBandOffer = bandOfferRepository.findById(bandOfferId);
        if (optionalBandOffer.isEmpty()) {
            throw new BandOfferNotFoundException(bandOfferId);
        }
        if (optionalBandOffer.get().getStatus() != BandOfferStatus.PENDING) {
            throw new CannotManipulateOfferException(optionalBandOffer.get().getStatus());
        }

        UUID sagaId = UUID.randomUUID();
        jmsTemplate.convertAndSend(BAND_OFFER_ACCEPT_OK_EVENT,
                new LinkArtistToBandCommand(
                        optionalBandOffer.get().getBandId(),
                        optionalBandOffer.get().getInvitedMusicianId(),
                        sagaId
                ),
                message -> {
                    message.setJMSCorrelationID(sagaId.toString());
                    return message;
                });
        return optionalBandOffer.get();
    }

    @Transactional
    @JmsListener(destination = BAND_OFFER_ACCEPT_OK_EVENT)
    public void onLinkOk(BandOfferAcceptOkEvent e) {
        bandOfferService.acceptOffer(e.getBandOfferId());
    }

    @JmsListener(destination = BAND_OFFER_ACCEPT_FAILED_EVENT)
    public void onLinkFailed(BandOfferAcceptFailedEvent e) {
        log.warn("Band offer {} was not accepted, because artist {} could not be linked to band {}",
                e.getBandOfferId(), e.getInvitedMusicianId(), e.getBandId());
    }
}
