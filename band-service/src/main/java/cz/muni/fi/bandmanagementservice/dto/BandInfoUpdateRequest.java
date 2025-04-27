package cz.muni.fi.bandmanagementservice.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

/**
 * @author Tomáš MAREK
 */
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

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMusicalStyle() {
        return musicalStyle;
    }

    public void setMusicalStyle(String musicalStyle) {
        this.musicalStyle = musicalStyle;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public Long getManagerId() {
        return managerId;
    }

    public void setManagerId(Long managerId) {
        this.managerId = managerId;
    }
}
