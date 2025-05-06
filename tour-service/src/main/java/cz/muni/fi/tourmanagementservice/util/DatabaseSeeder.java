package cz.muni.fi.tourmanagementservice.util;

import cz.muni.fi.tourmanagementservice.model.CityVisit;
import cz.muni.fi.tourmanagementservice.model.Tour;
import cz.muni.fi.tourmanagementservice.repository.CityVisitRepository;
import cz.muni.fi.tourmanagementservice.repository.TourRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

@Component
@RequiredArgsConstructor
public class DatabaseSeeder implements CommandLineRunner {

    private final CityVisitRepository cityVisitRepository;
    private final TourRepository tourRepository;

    private final Environment environment;


    @Override
    public void run(String... args) {
        boolean shouldSeed = Boolean.parseBoolean(environment.getProperty("app.db.seed", "false"));
        boolean shouldClear = Boolean.parseBoolean(environment.getProperty("app.db.clear", "false"));

        if (shouldClear) {
            tourRepository.deleteAll();
            cityVisitRepository.deleteAll();
            System.out.println("Database cleared.");
        }

        if (shouldSeed) {
            CityVisit cityVisit1 = new CityVisit();
            cityVisit1.setCityName("Prague");
            cityVisit1.setDateFrom(LocalDate.now());
            cityVisit1.setDateTo(LocalDate.now().plusDays(2));

            CityVisit cityVisit2 = new CityVisit();
            cityVisit2.setCityName("Brno");
            cityVisit2.setDateFrom(LocalDate.now().plusDays(3));
            cityVisit2.setDateTo(LocalDate.now().plusDays(5));

            CityVisit cityVisit3 = new CityVisit();
            cityVisit3.setCityName("Vienna");
            cityVisit3.setDateFrom(LocalDate.now().plusDays(6));
            cityVisit3.setDateTo(LocalDate.now().plusDays(8));

            CityVisit cityVisit4 = new CityVisit();
            cityVisit4.setCityName("Berlin");
            cityVisit4.setDateFrom(LocalDate.now().plusDays(9));
            cityVisit4.setDateTo(LocalDate.now().plusDays(11));


            Tour tour = new Tour();
            tour.setBandId(1L);
            tour.setTourName("Summer Tour 2023");

            Tour tour2 = new Tour();
            tour2.setBandId(2L);
            tour2.setTourName("Winter Tour 2023");


            cityVisitRepository.saveAll(List.of(cityVisit1, cityVisit2, cityVisit3, cityVisit4));
            tourRepository.saveAll(List.of(tour, tour2));
        }
    }
}
