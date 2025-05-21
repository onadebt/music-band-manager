package cz.muni.fi.events.bandoffer;

import lombok.*;

import java.io.Serial;
import java.io.Serializable;
import java.util.UUID;


@Builder
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class BandOfferAcceptFailedEvent implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private Long bandOfferId;
    private Long bandId;
    private Long invitedMusicianId;
    private Long offeringManagerId;
    private UUID sagaId;
}
