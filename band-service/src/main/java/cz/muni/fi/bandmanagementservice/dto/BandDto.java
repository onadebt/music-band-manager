package cz.muni.fi.bandmanagementservice.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

/**
 * @author Tomáš MAREK
 */
@Getter
@Setter
@NoArgsConstructor
@Schema(title = "A musical band", description = "Object representing a musical band, with its manager and members")
public class BandDto {
    @Schema(description = "band id", example = "1")
    private Long id;

    @Schema(description = "name of the band", example = "Rammstein")
    private String name;

    @Schema(description = "musical style played by the band", example = "industrial metal")
    private String musicalStyle;

    @Schema(description = "url of band logo", nullable = true)
    private String logo = null;

    @Schema(description = "id of the band manager", example = "2")
    private Long managerId;

    @Schema(description = "array of member id's")
    private Set<Long> members;
}
