package cz.muni.fi.tourmanagementservice;

import cz.muni.fi.tourmanagementservice.dto.CityVisitDto;
import cz.muni.fi.tourmanagementservice.model.CityVisit;
import cz.muni.fi.tourmanagementservice.model.Tour;

import java.time.Instant;
import java.time.LocalDate;
import java.util.Date;

public class TestDataFactory {

    public static CityVisit setUpCityVisit1() {
        CityVisit cityVisit = new CityVisit();
        cityVisit.setId(1L);
        cityVisit.setCityName("Prague");
        cityVisit.setDateFrom(LocalDate.of(2023, 6, 1));
        cityVisit.setDateTo(LocalDate.of(2023, 6, 2));
        return cityVisit;
    }

    public static CityVisitDto setUpCityVisit1Dto() {
        CityVisitDto cityVisitDto = new CityVisitDto();
        cityVisitDto.setId(1L);
        cityVisitDto.setCityName("Prague");
        cityVisitDto.setDateFrom(Date.from(Instant.ofEpochMilli(1685577600000L))); // 2023-06-01
        cityVisitDto.setDateTo(Date.from(Instant.ofEpochMilli(1685664000000L))); // 2023-06-02
        return cityVisitDto;
    }

    public static CityVisit setUpCityVisit2() {
        CityVisit cityVisit = new CityVisit();
        cityVisit.setId(2L);
        cityVisit.setCityName("Berlin");
        cityVisit.setDateFrom(LocalDate.of(2023, 6, 3));
        cityVisit.setDateTo(LocalDate.of(2023, 6, 4));
        return cityVisit;
    }

    public static CityVisitDto setUpCityVisit2Dto() {
        CityVisitDto cityVisitDto = new CityVisitDto();
        cityVisitDto.setId(2L);
        cityVisitDto.setCityName("Berlin");
        cityVisitDto.setDateFrom(Date.from(Instant.ofEpochMilli(1685750400000L))); // 2023-06-03
        cityVisitDto.setDateTo(Date.from(Instant.ofEpochMilli(1685836800000L))); // 2023-06-04
        return cityVisitDto;
    }

    public static Tour setUpTour1() {
        Tour tour = new Tour();
        tour.setId(1L);
        tour.setTourName("Test name");
        return tour;
    }

    public static Tour setUpTourDto1() {
        Tour tour = new Tour();
        tour.setId(1L);
        tour.setTourName("Test name");
        return tour;
    }

    public static Tour setUpTour2() {
        Tour tour = new Tour();
        tour.setId(2L);
        tour.setTourName("Test name 2");
        return tour;
    }

    public static Tour setUpTourDto2() {
        Tour tour = new Tour();
        tour.setId(2L);
        tour.setTourName("Test name 2");
        return tour;
    }
}
