package cz.muni.fi.tourmanagementservice.model;


import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
@Entity
public class Tour {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    //@GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long bandId;

    private String tourName;

    public Tour() {
    }
}
