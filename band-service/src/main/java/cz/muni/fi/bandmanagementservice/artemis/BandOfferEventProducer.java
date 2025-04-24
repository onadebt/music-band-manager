package cz.muni.fi.bandmanagementservice.artemis;

import cz.muni.fi.events.bandoffer.BandOfferAcceptedEvent;
import lombok.AllArgsConstructor;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;
import cz.muni.fi.events.bandoffer.BandOfferEvents;

@Component
@AllArgsConstructor
public class BandOfferEventProducer {

    private final JmsTemplate jmsTemplate;

    public void sendOfferAcceptedEvent(BandOfferAcceptedEvent event) {
        jmsTemplate.convertAndSend(BandOfferEvents.BAND_OFFER_ACCEPTED_EVENT, event);
    }
}