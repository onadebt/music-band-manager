package cz.muni.fi.bandmanagementservice.band.dto;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * @author Tomáš MAREK
 */
@Schema(title = "Offer to join a band", description = "Object containing info regarding an offer to join a band")
public class BandOfferDto {

    @Schema(description = "id of band offer")
    private Long id;

    @Schema(description = "id of offering band")
    private Long bandId;

    @Schema(description = "id of invited musician")
    private Long invitedMusicianId;

    @Schema(description = "id of offer manager")
    private Long offeringManagerId;

    public enum StatusEnum {
        PENDING(),
        ACCEPTED(),
        REJECTED();
    }

    @Schema(description = "status of the offer")
    private StatusEnum status;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getBandId() {
        return bandId;
    }

    public void setBandId(Long bandId) {
        this.bandId = bandId;
    }

    public Long getInvitedMusicianId() {
        return invitedMusicianId;
    }

    public void setInvitedMusicianId(Long invitedMusicianId) {
        this.invitedMusicianId = invitedMusicianId;
    }

    public Long getOfferingManagerId() {
        return offeringManagerId;
    }

    public void setOfferingManagerId(Long offeringManagerId) {
        this.offeringManagerId = offeringManagerId;
    }

    public StatusEnum getStatus() {
        return status;
    }

    public void setStatus(StatusEnum status) {
        this.status = status;
    }
}
