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
@Schema(title = "Request for change of band information", description = "Object containing newly updated band info, except members which are updated through dedicated api")
public class BandInfoUpdateDto {
    @Schema(description = "name of the band", example = "Rammstein")
    private String name;

    @Schema(description = "musical style played by the band", example = "industrial metal")
    private String musicalStyle;

    @Schema(description = "url of band logo")
    private String logoUrl;

    @Schema(description = "id of the band manager")
    private Long managerId;

    @Schema(description = "ids of the band members")
    private Set<Long> members;
}
