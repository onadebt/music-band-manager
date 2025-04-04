package cz.muni.fi.userservice.dto;

import cz.muni.fi.userservice.model.Role;
import lombok.EqualsAndHashCode;

import java.util.HashSet;
import java.util.Set;

@EqualsAndHashCode(callSuper = true)
public class ManagerDTO extends UserDTO {
    private String companyName;
    private Set<Long> managedBandIds = new HashSet<>();

    public ManagerDTO() {
        this.setRole(Role.MANAGER);
    }

    public ManagerDTO(String companyName, Set<Long> managedBandIds) {
        super();
        this.setRole(Role.MANAGER);
        this.companyName = companyName;
        this.managedBandIds = managedBandIds;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public Set<Long> getManagedBandIds() {
        return managedBandIds;
    }

    public void setManagedBandIds(Set<Long> managedBandIds) {
        this.managedBandIds = managedBandIds;
    }


    @Override
    public void setRole(Role role) {
        if (role != Role.MANAGER) {
            throw new IllegalArgumentException("ArtistDTO can only have " + Role.MANAGER + " role");
        }
        super.setRole(role);
    }
}
