package cz.muni.fi.userservice.rest.it;

import com.fasterxml.jackson.databind.ObjectMapper;
import cz.muni.fi.userservice.TestDataFactory;
import cz.muni.fi.userservice.dto.ManagerDto;
import cz.muni.fi.userservice.dto.ManagerUpdateDto;
import cz.muni.fi.userservice.model.Manager;
import cz.muni.fi.userservice.repository.ManagerRepository;
import cz.muni.fi.userservice.rest.it.config.DisableSecurityTestConfig;
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

import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @author Tomáš MAREK
 */
@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@Import(DisableSecurityTestConfig.class)
@ActiveProfiles("test")
class ManagerControllerIT {
    @Autowired
    MockMvc mockMvc;

    @Autowired
    ManagerRepository managerRepository;

    @Autowired
    ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        managerRepository.deleteAll();
    }

    @Test
    void register_persistsEntity() throws Exception {
        ManagerDto managerDto = TestDataFactory.setUpTestManager1Dto();
        managerDto.setId(null);
        mockMvc.perform(post("/api/managers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(managerDto)))
                .andExpect(status().isCreated());

        assertThat(managerRepository.count()).isEqualTo(1);
        assertThat(managerRepository.findByUsername(managerDto.getUsername())).isPresent();
    }

    @Test
    void register_nullDto_returnsBadRequest() throws Exception {
        mockMvc.perform(post("/api/managers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(null)))
                .andExpect(status().isBadRequest());

        System.out.println(managerRepository.count());
        assertThat(managerRepository.findAll().size()).isZero();
    }


    @Test
    void update_sourceDoesNotExists_returnNotFound() throws Exception {
        ManagerUpdateDto updateDto = new ManagerUpdateDto();
        Long emptyId = -123L;

        mockMvc.perform(put("/api/managers/{id}", emptyId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateDto)))
                .andExpect(status().isNotFound());

        assertThat(managerRepository.count()).isZero();
    }

    @Test
    void getAllManagers_noManagers_returnsEmptyList() throws Exception {
        mockMvc.perform(get("/api/managers"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isEmpty());
    }

    @Test
    void getAllManagers_twoManagers_returnsList() throws Exception {
        Manager manager1 = TestDataFactory.setUpTestManager1();
        Manager manager2 = TestDataFactory.setUpTestManager2();
        manager1.setId(null);
        manager2.setId(null);

        managerRepository.saveAll(List.of(manager1, manager2));

        mockMvc.perform(get("/api/managers"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[*].lastName",
                        containsInAnyOrder(manager1.getLastName(), manager2.getLastName())));
    }

    @Test
    void getById_validId_returnsEntityAndOk() throws Exception {
        Manager manager = saveManager(TestDataFactory.setUpTestManager1());

        mockMvc.perform(get("/api/managers/{id}", manager.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value(manager.getUsername()));
    }

    @Test
    void getById_emptyId_returnsNotFound() throws Exception {
        Long emptyId = -123L;
        mockMvc.perform(get("/api/managers/{id}", emptyId))
                .andExpect(status().isNotFound());
    }

    @Test
    void deleteById_entityExists_isRemoved() throws Exception {
        Manager manager = saveManager(TestDataFactory.setUpTestManager1());

        mockMvc.perform(delete("/api/managers/{id}", manager.getId()))
                .andExpect(status().isNoContent());

        assertThat(managerRepository.count()).isEqualTo(0);
    }

    @Test
    void deleteById_emptyId_returnsNotFound() throws Exception {
        Long emptyId = -123L;
        mockMvc.perform(delete("/api/managers/{id}", emptyId))
                .andExpect(status().isNotFound());
    }

    @Test
    void updateBands_validSet_returnsOkAndUpdatesRepository() throws Exception {
        Manager manager = saveManager(TestDataFactory.setUpTestManager1());
        Set<Long> newBands = Set.of(4L, 5L, 6L, 7L);

        mockMvc.perform(patch("/api/managers/bands/{managerId}", manager.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newBands)))
                .andExpect(status().isOk());

        Optional<Manager> updated = managerRepository.findById(manager.getId());
        assert updated.isPresent();
        assertEquals(updated.get().getManagedBandIds().size(), newBands.size());
        assertThat(updated.get().getManagedBandIds().containsAll(newBands)).isTrue();
    }

    @Test
    void updateBands_invalidBandId_returnsNotFound() throws Exception {
        Long emptyId = -123L;
        Set<Long> newBands = Set.of(4L, 5L, 6L, 7L);
        mockMvc.perform(patch("/api/managers/bands/{managerId}", emptyId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newBands)))
                .andExpect(status().isNotFound());
    }

    @Test
    void update_sourceExists_replacesEntity() throws Exception {
        Manager manager = TestDataFactory.setUpTestManager1();
        manager = saveManager(manager);
        ManagerUpdateDto updateDto = toManagerUpdateDto(manager);
        updateDto.setLastName("New Name");

        mockMvc.perform(put("/api/managers/{id}", manager.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateDto)))
                .andExpect(status().isOk());

        assertThat(managerRepository.count()).isEqualTo(1);
        Optional<Manager> updated = managerRepository.findById(manager.getId());
        assertThat(updated).isPresent();
        assertThat(updated.get().getLastName()).isEqualTo(updateDto.getLastName());
    }


    @Test
    void getManagersByBandIds_fitsAllManagers_returnOkAndAllManagers() throws Exception {
        Manager manager1 = TestDataFactory.setUpTestManager1();
        Manager manager2 = TestDataFactory.setUpTestManager2();
        manager1.setId(null);
        manager2.setId(null);
        manager1 = saveManager(manager1);
        manager2 = saveManager(manager2);

        mockMvc.perform(get("/api/managers/bands")
                        .param("bandIds", "2"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[*].username",
                        containsInAnyOrder(manager1.getUsername(), manager2.getUsername())));
    }

    @Test
    void getManagersByBandIds_fitsOneManager_returnOkAndManager() throws Exception{
        Manager manager1 = TestDataFactory.setUpTestManager1();
        Manager manager2 = TestDataFactory.setUpTestManager2();
        manager1.setId(null);
        manager2.setId(null);
        saveManager(manager1);
        saveManager(manager2);

        mockMvc.perform(get("/api/managers/bands")
                        .param("bandIds", "1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].username").value(manager1.getUsername()));

    }

    @Test
    void getManagersByBandIds_findsNoManagers_returnEmptyList() throws Exception {
        Manager manager1 = TestDataFactory.setUpTestManager1();
        Manager manager2 = TestDataFactory.setUpTestManager2();
        manager1.setId(null);
        manager2.setId(null);
        saveManager(manager1);
        saveManager(manager2);

        mockMvc.perform(get("/api/managers/bands")
                        .param("bandIds", "123"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isEmpty());
    }


    private Manager saveManager(Manager manager) {
        manager.setId(null);
        manager = managerRepository.save(manager);
        return manager;
    }


    private ManagerUpdateDto toManagerUpdateDto(Manager manager) {
        ManagerUpdateDto managerUpdateDto = new ManagerUpdateDto();
        managerUpdateDto.setManagedBandIds(manager.getManagedBandIds());
        managerUpdateDto.setCompanyName(manager.getCompanyName());

        managerUpdateDto.setUsername(manager.getUsername());
        managerUpdateDto.setEmail(manager.getEmail());
        managerUpdateDto.setFirstName(manager.getFirstName());
        managerUpdateDto.setLastName(manager.getLastName());
        managerUpdateDto.setRole(manager.getRole());
        managerUpdateDto.setUsername(manager.getUsername());

        return managerUpdateDto;
    }
}
