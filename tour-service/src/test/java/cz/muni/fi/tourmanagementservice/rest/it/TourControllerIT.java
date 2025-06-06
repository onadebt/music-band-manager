package cz.muni.fi.tourmanagementservice.rest.it;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import cz.muni.fi.tourmanagementservice.dto.CityVisitDto;
import cz.muni.fi.tourmanagementservice.dto.TourDto;
import cz.muni.fi.tourmanagementservice.model.CityVisit;
import cz.muni.fi.tourmanagementservice.model.Tour;
import cz.muni.fi.tourmanagementservice.repository.CityVisitRepository;
import cz.muni.fi.tourmanagementservice.repository.TourRepository;
import cz.muni.fi.tourmanagementservice.rest.it.config.DisableSecurityTestConfig;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Import(DisableSecurityTestConfig.class)
@Transactional
public class TourControllerIT {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private TourRepository tourRepository;

    @Autowired
    private CityVisitRepository cityVisitRepository;

    private ObjectMapper objectMapper;
    private Tour testTour;
    private CityVisit testCityVisit;

    @BeforeEach
    public void setup() {
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd"));

        tourRepository.deleteAll();
        cityVisitRepository.deleteAll();

        testCityVisit = new CityVisit();
        testCityVisit.setCityName("Prague");
        testCityVisit.setDateFrom(LocalDate.now().plusDays(1));
        testCityVisit.setDateTo(LocalDate.now().plusDays(3));
        testCityVisit = cityVisitRepository.save(testCityVisit);

        testTour = new Tour();
        testTour.setTourName("Europe Tour 2025");
        testTour.setBandId(1L);
        testTour.setCityVisits(new ArrayList<>());
        testTour.addCityVisit(testCityVisit);
        testTour = tourRepository.save(testTour);
    }

    @Test
    public void getAllTours_oneTourPresent_returnsOkAndListWithOneTour() throws Exception {
        mockMvc.perform(get("/api/tours"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(greaterThanOrEqualTo(1))))
                .andExpect(jsonPath("$[0].tourName", is("Europe Tour 2025")));
    }

    @Test
    public void getTourById_tourExists_returnOkAndWantedTour() throws Exception {
        mockMvc.perform(get("/api/tours/{id}", testTour.getId()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(testTour.getId().intValue())))
                .andExpect(jsonPath("$.tourName", is("Europe Tour 2025")))
                .andExpect(jsonPath("$.bandId", is(1)))
                .andExpect(jsonPath("$.cityVisits", hasSize(1)))
                .andExpect(jsonPath("$.cityVisits[0].cityName", is("Prague")));
    }

    @Test
    public void getToursByBand_oneTourMatched_returnsOkAndListWithOneTour() throws Exception {
        mockMvc.perform(get("/api/tours/band/{bandId}", 1L))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(greaterThanOrEqualTo(1))))
                .andExpect(jsonPath("$[0].bandId", is(1)));
    }

    @Test
    public void createTour_validData_returnsOkAndCreatedTour() throws Exception {
        TourDto tourDTO = new TourDto();
        tourDTO.setTourName("North America Tour 2025");
        tourDTO.setBandId(2L);

        String tourJson = objectMapper.writeValueAsString(tourDTO);

        mockMvc.perform(post("/api/tours")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(tourJson))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.tourName", is("North America Tour 2025")))
                .andExpect(jsonPath("$.bandId", is(2)));
    }

    @Test
    public void updateTour_validData_returnsOkAndUpdatedTour() throws Exception {
        TourDto tourDTO = new TourDto();
        tourDTO.setTourName("Updated Tour Name");
        tourDTO.setBandId(testTour.getBandId());

        String tourJson = objectMapper.writeValueAsString(tourDTO);

        mockMvc.perform(put("/api/tours/{id}", testTour.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(tourJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.tourName", is("Updated Tour Name")));
    }

    @Test
    public void deleteTour_tourExists_returnsNoContent() throws Exception {
        mockMvc.perform(delete("/api/tours/{id}", testTour.getId()))
                .andExpect(status().isNoContent());

        mockMvc.perform(get("/api/tours/{id}", testTour.getId()))
                .andExpect(status().isNotFound());
    }

    @Test
    public void addCityVisitToTour_validData_returnsOk() throws Exception {
        CityVisitDto cityVisitDTO = new CityVisitDto();
        cityVisitDTO.setCityName("Berlin");

        Date dateFrom = new Date(System.currentTimeMillis() + 5 * 24 * 60 * 60 * 1000);
        Date dateTo = new Date(System.currentTimeMillis() + 7 * 24 * 60 * 60 * 1000);

        cityVisitDTO.setDateFrom(dateFrom);
        cityVisitDTO.setDateTo(dateTo);

        String cityVisitJson = objectMapper.writeValueAsString(cityVisitDTO);

        mockMvc.perform(post("/api/tours/{tourId}/city-visit", testTour.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(cityVisitJson))
                .andExpect(status().isCreated());

        mockMvc.perform(get("/api/tours/{id}", testTour.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.cityVisits", hasSize(2)))
                .andExpect(jsonPath("$.cityVisits[*].cityName", hasItem("Berlin")));
    }

    @Test
    public void removeCityVisitFromTour_tourAndVisitExist_returnsNoContent() throws Exception {
        mockMvc.perform(delete("/api/tours/{tourId}/city-visit/{cityVisitId}",
                        testTour.getId(), testCityVisit.getId()))
                .andExpect(status().isNoContent());

        mockMvc.perform(get("/api/tours/{id}", testTour.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.cityVisits", hasSize(0)));
    }

    @Test
    public void createTour_invalidData_returnsBadRequest() throws Exception {
        TourDto tourDTO = new TourDto();
        tourDTO.setTourName("a");
        tourDTO.setBandId(null);

        String tourJson = objectMapper.writeValueAsString(tourDTO);

        mockMvc.perform(post("/api/tours")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(tourJson))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void getTour_invalidId_returnsNotFound() throws Exception {
        mockMvc.perform(get("/api/tours/{id}", 999L))
                .andExpect(status().isNotFound());
    }
}