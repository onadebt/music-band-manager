package cz.muni.fi.bandmanagementservice.band.data.model;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * @author Tomáš MAREK
 */
public class Band {
    private Long id;

    @NotNull
    private String name;

    @NotNull
    private String musicalStyle;

    @NotNull
    private Long managerId;

    private String logoUrl;
    private final Collection<Long> members = new ArrayList<>();

    public Band(Long id, String name, String musicalStyle, Long managerId) {
        this.id = id;
        this.name = name;
        this.musicalStyle = musicalStyle;
        this.managerId = managerId;
    }

    public Band(Long id, String name, String musicalStyle, Long managerId, String logoUrl) {
        this(id, name, musicalStyle, managerId);
        this.logoUrl = logoUrl;
    }

    public Collection<Long> getMembers() {
        return Collections.unmodifiableCollection(members);
    }

    public void setMembers(Collection<Long> members) {
        this.members.clear();
        this.members.addAll(members);
    }

    public void addMember(Long memberId) {
        this.members.add(memberId);
    }

    public Long getManagerId() {
        return managerId;
    }

    public void setManagerId(Long managerId) {
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

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public boolean equals(Object object) {
        if (object == null || getClass() != object.getClass()) return false;
        Band band = (Band) object;
        return Objects.equals(managerId, band.managerId) && Objects.equals(name, band.name) && Objects.equals(musicalStyle, band.musicalStyle) && Objects.equals(logoUrl, band.logoUrl) && Objects.equals(members, band.members);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, musicalStyle, logoUrl, managerId, members);
    }
}
