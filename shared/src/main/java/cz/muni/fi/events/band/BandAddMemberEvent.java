package cz.muni.fi.events.band;

import lombok.*;

import java.io.Serial;
import java.io.Serializable;

@Builder
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class BandAddMemberEvent implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private Long bandId;
    private Long memberId;
}
