package cz.muni.fi.bandmanagementservice.model;

import cz.muni.fi.bandmanagementservice.exceptions.InvalidOperationException;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

/**
 * @author Tomáš MAREK
 */
@Entity
@Table(name = "band_offers")
public class BandOffer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long bandId;

    @Column(nullable = false)
    private Long invitedMusicianId;

    @Column(nullable = false)
    private Long offeringManagerId;

    @Column(nullable = false)
    private BandOfferStatus status = BandOfferStatus.PENDING;

    public BandOffer(Long id, Long bandId, Long invitedMusicianId, Long offeringManagerId) {
        this.id = id;
        this.bandId = bandId;
        this.invitedMusicianId = invitedMusicianId;
        this.offeringManagerId = offeringManagerId;
    }

    public BandOffer() {
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

    public BandOfferStatus getStatus() {
        return status;
    }

    public void setStatus(BandOfferStatus status) {
        this.status = status;
    }
}
