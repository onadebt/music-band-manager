package cz.muni.fi.bandmanagementservice.data.model;

import javax.validation.constraints.NotNull;

/**
 * @author Tomáš MAREK
 */
public class BandOffer {
    private Integer id;

    @NotNull
    private int bandId;

    @NotNull
    private int invitedMusicianId;

    @NotNull
    private int offeringManagerId;

    @NotNull
    private BandOfferStatus status = BandOfferStatus.PENDING;

    public BandOffer(Integer id, int bandId, int invitedMusicianId, int offeringManagerId) {
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

    public int getOfferingManagerId() {
        return offeringManagerId;
    }

    public void setOfferingManagerId(int offeringManagerId) {
        this.offeringManagerId = offeringManagerId;
    }

    public int getInvitedMusicianId() {
        return invitedMusicianId;
    }

    public void setInvitedMusicianId(int invitedMusicianId) {
        this.invitedMusicianId = invitedMusicianId;
    }

    public int getBandId() {
        return bandId;
    }

    public void setBandId(int bandId) {
        bandId = bandId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
