package cz.muni.fi.tourmanagementservice.model;


import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;


@Data
@Entity
public class Tour {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long bandId;

    private String tourName;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private List<CityVisit> cityVisits = new ArrayList<>();

    public Tour() {
    }

    public void addCityVisit(CityVisit cityVisit) {
        cityVisits.add(cityVisit);
    }

    public void removeCityVisit(CityVisit cityVisit) {
        cityVisits.remove(cityVisit);
    }

    @Override
    public String toString() {
        return "Tour{" +
                "id=" + id +
                ", bandId=" + bandId +
                ", tourName='" + tourName + '\'' +
                ", cityVisits=" + cityVisits +
                '}';
    }

}
