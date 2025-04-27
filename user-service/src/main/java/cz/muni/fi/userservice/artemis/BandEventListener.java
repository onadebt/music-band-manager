package cz.muni.fi.userservice.artemis;

import cz.muni.fi.events.band.BandAddMemberEvent;
import cz.muni.fi.events.band.BandEvents;
import cz.muni.fi.userservice.service.ArtistService;
import lombok.AllArgsConstructor;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@AllArgsConstructor
public class BandEventListener {
    private ArtistService artistService;

    @Transactional
    @JmsListener(destination = BandEvents.BAND_ADD_MEMBER_EVENT)
    public void handleBandAddMemberEvent(BandAddMemberEvent event) {
        artistService.linkArtistToBand(event.getMemberId(), event.getBandId());
    }

    @Transactional
    @JmsListener(destination = BandEvents.BAND_REMOVE_MEMBER_EVENT)
    public void handleBandRemoveMemberEvent(BandAddMemberEvent event) {
        artistService.unlinkArtistFromBand(event.getMemberId(), event.getBandId());
    }
}
