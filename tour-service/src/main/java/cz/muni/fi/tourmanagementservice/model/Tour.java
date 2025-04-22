package cz.muni.fi.tourmanagementservice.model;


import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

import java.util.Objects;


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

    public Tour(Long id, Long bandId, String tourName) {
        this.id = id;
        this.bandId = bandId;
        this.tourName = tourName;
    }

    public Tour() {
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Tour tour = (Tour) o;
        return Objects.equals(id, tour.id) && Objects.equals(bandId, tour.bandId) && Objects.equals(tourName, tour.tourName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, bandId, tourName);
    }

    @Override
    public String toString() {
        return "Tour{" +
                "id=" + id +
                ", bandId=" + bandId +
                ", tourName='" + tourName + '\'' +
                '}';
    }
}
