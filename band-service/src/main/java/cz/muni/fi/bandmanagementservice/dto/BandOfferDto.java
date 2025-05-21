package cz.muni.fi.bandmanagementservice.dto;

import cz.muni.fi.enums.BandOfferStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author Tomáš MAREK
 */
@Getter
@Setter
@NoArgsConstructor
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

    @Schema(description = "status of the offer")
    private BandOfferStatus status;
}
