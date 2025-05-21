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
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Collections;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * @author Tomáš MAREK
 */
@Entity
@Builder
@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode
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

    public void addMember(Long memberId) {
        this.members.add(memberId);
    }

    public void removeMember(Long memberId) {
        this.members.remove(memberId);
    }

    public Set<Long> getMembers() {
        return Collections.unmodifiableSet(members);
    }

    public void setMembers(Set<Long> members) {
        this.members.clear();
        this.members.addAll(members);
    }
}
