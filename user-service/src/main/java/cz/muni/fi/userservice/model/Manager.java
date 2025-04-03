package cz.muni.fi.userservice.model;


import jakarta.persistence.*;
import lombok.EqualsAndHashCode;

import java.util.HashSet;
import java.util.Set;

@EqualsAndHashCode(callSuper = true)
@Entity
@DiscriminatorValue("MANAGER")
@Table(name = "managers")
public class Manager extends User {

    @ElementCollection
    @CollectionTable(name = "manager_bands", joinColumns = @JoinColumn(name = "manager_id"))
    @Column(name = "band_id")
    private Set<Long> managedBandIds = new HashSet<>();

    @Column
    private String companyName;

    public Manager() {
    }

    public Set<Long> getManagedBandIds() {
        return managedBandIds;
    }

    public void setManagedBandIds(Set<Long> managedBandIds) {
        this.managedBandIds = managedBandIds;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }
}
