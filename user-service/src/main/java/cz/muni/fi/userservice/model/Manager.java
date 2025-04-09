package cz.muni.fi.userservice.model;


import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@EqualsAndHashCode(callSuper = true)
@DiscriminatorValue("MANAGER")
@Table(name = "managers")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
public class Manager extends User {

    @ElementCollection
    @CollectionTable(name = "manager_bands", joinColumns = @JoinColumn(name = "manager_id"))
    @Column(name = "band_id")
    private Set<Long> managedBandIds = new HashSet<>();

    @Column
    private String companyName;
}
