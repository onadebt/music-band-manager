package cz.muni.fi.bandmanagementservice.data.model;

import javax.validation.constraints.NotNull;

/**
 * @author Tomáš MAREK
 */
public class BandOffer {
    @NotNull
    private int id;

    @NotNull
    private int BandId;

    @NotNull
    private int invitedMusicianId;

    @NotNull
    private int offeringManagerId;

    @NotNull
    private BandOfferStatus status;

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
        return BandId;
    }

    public void setBandId(int bandId) {
        BandId = bandId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
