package cz.muni.fi.bandmanagementservice.data.model;

import io.swagger.v3.oas.models.security.SecurityScheme;

import javax.validation.constraints.NotNull;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

/**
 * @author Tomáš MAREK
 */
public class Band {
    private Integer id;

    @NotNull
    private String name;

    @NotNull
    private String musicalStyle;

    @NotNull
    private int managerId;

    private String logoUrl;
    private Collection<Integer> members = List.of();

    public Band(Integer id, String name, String musicalStyle, int managerId) {
        this.id = id;
        this.name = name;
        this.musicalStyle = musicalStyle;
        this.managerId = managerId;
    }

    public Collection<Integer> getMembers() {
        return members;
    }

    public void setMembers(List<Integer> members) {
        this.members = members;
    }

    public void addMember(int memberId) {
        this.members.add(memberId);
    }

    public int getManagerId() {
        return managerId;
    }

    public void setManagerId(int managerId) {
        this.managerId = managerId;
    }

    public String getLogoUrl() {
        return logoUrl;
    }

    public void setLogoUrl(String logoUrl) {
        this.logoUrl = logoUrl;
    }

    public String getMusicalStyle() {
        return musicalStyle;
    }

    public void setMusicalStyle(String musicalStyle) {
        this.musicalStyle = musicalStyle;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public boolean equals(Object object) {
        if (object == null || getClass() != object.getClass()) return false;
        Band band = (Band) object;
        return managerId == band.managerId && Objects.equals(name, band.name) && Objects.equals(musicalStyle, band.musicalStyle) && Objects.equals(logoUrl, band.logoUrl) && Objects.equals(members, band.members);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, musicalStyle, logoUrl, managerId, members);
    }
}
