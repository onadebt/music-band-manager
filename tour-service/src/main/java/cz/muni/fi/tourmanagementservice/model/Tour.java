package cz.muni.fi.tourmanagementservice.model;



import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Entity
@Getter // Add Lombok getter for all fields
@Setter // Add Lombok setter for all fields
public class Tour {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long bandId;

    private String tourName;

    //private Map<String, List<Date>> cityVisit;

    public Tour() {
    }
}
