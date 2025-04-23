package cz.muni.fi.userservice.dto;

import cz.muni.fi.shared.enm.Role;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
public class ArtistDto extends UserDto {
    private String stageName;
    private String bio;
    private String skills;
    private Set<Long> bandIds;

    public ArtistDto() {
        this.setRole(Role.ARTIST);
    }

    @Override
    public void setRole(Role role) {
        if (role != Role.ARTIST) {
            throw new IllegalArgumentException("ArtistDTO can only have" + Role.ARTIST +" role");
        }
        super.setRole(role);
    }
}
