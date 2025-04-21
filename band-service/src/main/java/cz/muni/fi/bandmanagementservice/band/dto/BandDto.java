package cz.muni.fi.bandmanagementservice.band.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.Set;

/**
 * @author Tomáš MAREK
 */
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

    public Set<Long> getMembers() {
        return members;
    }

    public void setMembers(Set<Long> members) {
        this.members = members;
    }

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
