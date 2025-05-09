package cz.muni.fi.tourmanagementservice.rest.it;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import cz.muni.fi.tourmanagementservice.dto.CityVisitDto;
import cz.muni.fi.tourmanagementservice.model.CityVisit;
import cz.muni.fi.tourmanagementservice.repository.CityVisitRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Date;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
public class CityVisitControllerIT {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private CityVisitRepository cityVisitRepository;

    private ObjectMapper objectMapper;
    private CityVisit testCityVisit;

    @BeforeEach
    public void setup() {
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd"));

        cityVisitRepository.deleteAll();

        testCityVisit = new CityVisit();
        testCityVisit.setCityName("Vienna");
        testCityVisit.setDateFrom(LocalDate.now().plusDays(1));
        testCityVisit.setDateTo(LocalDate.now().plusDays(3));
        testCityVisit = cityVisitRepository.save(testCityVisit);
    }

    @Test
    public void testGetAllCityVisits() throws Exception {
        mockMvc.perform(get("/api/cityVisits"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(greaterThanOrEqualTo(1))))
                .andExpect(jsonPath("$[0].cityName", is("Vienna")));
    }

    @Test
    public void testGetCityVisitById() throws Exception {
        mockMvc.perform(get("/api/cityVisits/{id}", testCityVisit.getId()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(testCityVisit.getId().intValue())))
                .andExpect(jsonPath("$.cityName", is("Vienna")));
    }

    @Test
    public void testCreateCityVisit() throws Exception {
        CityVisitDto cityVisitDTO = new CityVisitDto();
        cityVisitDTO.setCityName("Paris");

        Date dateFrom = new Date(System.currentTimeMillis() + 10 * 24 * 60 * 60 * 1000);
        Date dateTo = new Date(System.currentTimeMillis() + 12 * 24 * 60 * 60 * 1000);

        cityVisitDTO.setDateFrom(dateFrom);
        cityVisitDTO.setDateTo(dateTo);

        String cityVisitJson = objectMapper.writeValueAsString(cityVisitDTO);

        mockMvc.perform(post("/api/cityVisits")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(cityVisitJson))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.cityName", is("Paris")));
    }

    @Test
    public void testUpdateCityVisit() throws Exception {
        CityVisitDto cityVisitDTO = new CityVisitDto();
        cityVisitDTO.setCityName("Updated City Name");

        Date dateFrom = new Date(System.currentTimeMillis() + 5 * 24 * 60 * 60 * 1000);
        Date dateTo = new Date(System.currentTimeMillis() + 7 * 24 * 60 * 60 * 1000);

        cityVisitDTO.setDateFrom(dateFrom);
        cityVisitDTO.setDateTo(dateTo);

        String cityVisitJson = objectMapper.writeValueAsString(cityVisitDTO);

        mockMvc.perform(put("/api/cityVisits/{id}", testCityVisit.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(cityVisitJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.cityName", is("Updated City Name")));
    }

    @Test
    public void testDeleteCityVisit() throws Exception {
        mockMvc.perform(delete("/api/cityVisits/{id}", testCityVisit.getId()))
                .andExpect(status().isNotFound());

        mockMvc.perform(get("/api/cityVisits/{id}", testCityVisit.getId()))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testCreateCityVisitWithInvalidData() throws Exception {
        CityVisitDto cityVisitDTO = new CityVisitDto();
        cityVisitDTO.setCityName("");

        Date dateFrom = new Date(System.currentTimeMillis() + 5 * 24 * 60 * 60 * 1000);
        Date dateTo = new Date(System.currentTimeMillis() + 7 * 24 * 60 * 60 * 1000);
        cityVisitDTO.setDateFrom(dateFrom);
        cityVisitDTO.setDateTo(dateTo);

        String cityVisitJson = objectMapper.writeValueAsString(cityVisitDTO);

        mockMvc.perform(post("/api/cityVisits")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(cityVisitJson))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testCreateCityVisitWithInvalidDates() throws Exception {
        CityVisitDto cityVisitDTO = new CityVisitDto();
        cityVisitDTO.setCityName("London");

        Date dateFrom = new Date(System.currentTimeMillis() - 5 * 24 * 60 * 60 * 1000);
        Date dateTo = new Date(System.currentTimeMillis() + 7 * 24 * 60 * 60 * 1000);

        cityVisitDTO.setDateFrom(dateFrom);
        cityVisitDTO.setDateTo(dateTo);

        String cityVisitJson = objectMapper.writeValueAsString(cityVisitDTO);

        mockMvc.perform(post("/api/cityVisits")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(cityVisitJson))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testGetNonExistingCityVisit() throws Exception {
        mockMvc.perform(get("/api/cityVisits/{id}", 999L))
                .andExpect(status().isNotFound());
    }
}