package cz.muni.fi.events.bandoffer;

import lombok.*;

import java.io.Serializable;

@Builder
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class BandOfferAcceptedEvent implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long id;
    private Long bandId;
    private Long invitedMusicianId;
    private Long offeringManagerId;
}
