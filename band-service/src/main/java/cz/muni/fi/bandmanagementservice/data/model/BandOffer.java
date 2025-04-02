package cz.muni.fi.bandmanagementservice.data.model;

import cz.muni.fi.bandmanagementservice.data.exceptions.InvalidOperationException;

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
        if (status != BandOfferStatus.PENDING){
            throw new InvalidOperationException("The offer has already been accepted or rejected");
        }
        status = BandOfferStatus.ACCEPTED;
    }

    public void rejectOffer(){
        if (status != BandOfferStatus.PENDING){
            throw new InvalidOperationException("The offer has already been accepted or rejected");
        }
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
