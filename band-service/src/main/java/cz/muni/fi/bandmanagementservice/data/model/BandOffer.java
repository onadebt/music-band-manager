package cz.muni.fi.bandmanagementservice.data.model;

import javax.validation.constraints.NotNull;

/**
 * @author Tomáš MAREK
 */
public class BandOffer {
    private Long id;

    @NotNull
    private Long bandId;

    @NotNull
    private Long invitedMusicianId;

    @NotNull
    private Long offeringManagerId;

    @NotNull
    private BandOfferStatus status = BandOfferStatus.PENDING;

    public BandOffer(Long id, Long bandId, Long invitedMusicianId, Long offeringManagerId) {
        this.id = id;
        this.bandId = bandId;
        this.invitedMusicianId = invitedMusicianId;
        this.offeringManagerId = offeringManagerId;
    }

    public void acceptOffer(){
        status = BandOfferStatus.ACCEPTED;
    }

    public void rejectOffer(){
        status = BandOfferStatus.REJECTED;
    }

    public Long getOfferingManagerId() {
        return offeringManagerId;
    }

    public void setOfferingManagerId(Long offeringManagerId) {
        this.offeringManagerId = offeringManagerId;
    }

    public Long getInvitedMusicianId() {
        return invitedMusicianId;
    }

    public void setInvitedMusicianId(Long invitedMusicianId) {
        this.invitedMusicianId = invitedMusicianId;
    }

    public Long getBandId() {
        return bandId;
    }

    public void setBandId(Long bandId) {
        this.bandId = bandId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
