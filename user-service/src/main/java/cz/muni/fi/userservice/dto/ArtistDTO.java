package cz.muni.fi.userservice.dto;

import cz.muni.fi.userservice.model.Role;
import lombok.EqualsAndHashCode;

import java.util.Set;

@EqualsAndHashCode(callSuper = true)
public class ArtistDTO extends UserDTO {
    private String stageName;
    private String bio;
    private String skills;
    private Set<Long> bandIds;

    public ArtistDTO() {
        this.setRole(Role.ARTIST);
    }

    public ArtistDTO(String stageName, String bio, String skills, Set<Long> bandIds) {
        super();
        this.setRole(Role.ARTIST);
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

    @Override
    public void setRole(Role role) {
        if (role != Role.ARTIST) {
            throw new IllegalArgumentException("ArtistDTO can only have" + Role.ARTIST +" role");
        }
        super.setRole(role);
    }
}
