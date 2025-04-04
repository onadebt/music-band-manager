package cz.muni.fi.userservice.model;


import jakarta.persistence.*;
import lombok.EqualsAndHashCode;

import java.util.HashSet;
import java.util.Set;

@EqualsAndHashCode(callSuper = true)
@Entity
@DiscriminatorValue("ARTIST")
@Table(name = "artists")
public class Artist extends User {

    @Column
    private String stageName;

    @Column
    private String bio;

    @Column
    private String skills;

    @ElementCollection
    @CollectionTable(name = "artist_bands", joinColumns = @JoinColumn(name = "artist_id"))
    @Column(name = "band_id")
    private Set<Long> bandIds = new HashSet<>();

    public Artist() {
    }

    public Artist(String stageName, String bio, String skills, Set<Long> bandIds) {
        this.stageName = stageName;
        this.bio = bio;
        this.skills = skills;
        this.bandIds = bandIds;
    }

    public String getStageName() {
        return stageName;
    }

    public void setStageName(String stageName) {
        this.stageName = stageName;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public String getSkills() {
        return skills;
    }

    public void setSkills(String skills) {
        this.skills = skills;
    }

    public Set<Long> getBandIds() {
        return bandIds;
    }

    public void setBandIds(Set<Long> bandIds) {
        this.bandIds = bandIds;
    }
}
