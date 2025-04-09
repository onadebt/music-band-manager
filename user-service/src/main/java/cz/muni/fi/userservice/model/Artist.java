package cz.muni.fi.userservice.model;


import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@EqualsAndHashCode(callSuper = true)
@DiscriminatorValue("ARTIST")
@Table(name = "artists")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
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

}
