package cz.muni.fi.userservice.dto;

import cz.muni.fi.shared.enm.Role;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Setter
@Getter
@EqualsAndHashCode(callSuper = true)
public class ManagerDto extends UserDto {
    private String companyName;
    private Set<Long> managedBandIds = new HashSet<>();

    public ManagerDto() {
        this.setRole(Role.MANAGER);
    }

    @Override
    public void setRole(Role role) {
        if (role != Role.MANAGER) {
            throw new IllegalArgumentException("ArtistDTO can only have " + Role.MANAGER + " role");
        }
        super.setRole(role);
    }
}
