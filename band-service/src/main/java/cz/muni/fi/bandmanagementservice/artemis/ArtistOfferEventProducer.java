package cz.muni.fi.bandmanagementservice.artemis;

import cz.muni.fi.events.bandoffer.BandOfferAcceptedEvent;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;
import cz.muni.fi.events.bandoffer.BandOfferEvents;

@Component
public class ArtistOfferEventProducer {

    private final JmsTemplate jmsTemplate;

    public ArtistOfferEventProducer(JmsTemplate jmsTemplate) {
        this.jmsTemplate = jmsTemplate;
    }

    public void sendOfferAcceptedEvent(BandOfferAcceptedEvent event) {
        jmsTemplate.convertAndSend(BandOfferEvents.BAND_OFFER_ACCEPTED_EVENT, event);
    }
}