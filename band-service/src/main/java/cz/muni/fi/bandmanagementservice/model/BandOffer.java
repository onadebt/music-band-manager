package cz.muni.fi.bandmanagementservice.model;

import cz.muni.fi.bandmanagementservice.exceptions.InvalidOperationException;
import cz.muni.fi.shared.enm.BandOfferStatus;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author Tomáš MAREK
 */
@Entity
@Table(name = "band_offers")
@NoArgsConstructor
@Getter
@Setter
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

}
