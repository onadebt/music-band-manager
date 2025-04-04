package cz.muni.fi.tourmanagementservice.model;



import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter // Add Lombok getter for all fields
@Setter // Add Lombok setter for all fields
public class Tour {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    //@GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long bandId;

    private String tourName;

    //private Map<String, List<Date>> cityVisit;

    public Tour() {
    }
}
