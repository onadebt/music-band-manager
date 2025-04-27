package cz.muni.fi.bandmanagementservice.model;

import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Builder;

import java.util.Collections;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * @author Tomáš MAREK
 */
@Entity
@Builder
@Table(name = "bands")
public class Band {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String musicalStyle;

    @Column(nullable = false)
    private Long managerId;

    @Column
    private String logoUrl;

    @ElementCollection
    @CollectionTable(name = "bands_members")
    @Column(name = "members_id")
    private final Set<Long> members = new HashSet<>();

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

    public Band() {
    }

    public Set<Long> getMembers() {
        return Collections.unmodifiableSet(members);
    }

    public void setMembers(Set<Long> members) {
        this.members.clear();
        this.members.addAll(members);
    }

    public void addMember(Long memberId) {
        this.members.add(memberId);
    }

    public void removeMember(Long memberId) {
        this.members.remove(memberId);
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
