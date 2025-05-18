package cz.muni.fi.bandmanagementservice.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author Tomáš MAREK
 */
@Getter
@Setter
@NoArgsConstructor
@Schema(title = "Request for change of band information", description = "Object containing newly updated band info, except members which are updated through dedicated api")
public class BandInfoUpdateRequest {
    @Schema(description = "band id")
    @NotNull
    private Long id;

    @Schema(description = "name of the band", example = "Rammstein")
    private String name;

    @Schema(description = "musical style played by the band", example = "industrial metal")
    private String musicalStyle;

    @Schema(description = "url of band logo")
    private String logo;

    @Schema(description = "id of the band manager")
    private Long managerId;
}
