package cz.muni.fi.bandmanagementservice.artemis;

import cz.muni.fi.events.band.BandAddMemberEvent;
import cz.muni.fi.events.band.BandEvents;
import cz.muni.fi.events.band.BandRemoveMemberEvent;
import lombok.AllArgsConstructor;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class BandEventProducer {
    private final JmsTemplate jmsTemplate;

    public void sendBandAddMemberEvent(BandAddMemberEvent event) {
        jmsTemplate.convertAndSend(BandEvents.BAND_ADD_MEMBER_EVENT, event);
    }

    public void sendBandRemoveMemberEvent(BandRemoveMemberEvent event) {
        jmsTemplate.convertAndSend(BandEvents.BAND_REMOVE_MEMBER_EVENT, event);
    }
}
