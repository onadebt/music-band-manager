package cz.muni.fi.userservice.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import cz.muni.fi.userservice.TestDataFactory;
import cz.muni.fi.userservice.dto.ManagerDto;
import cz.muni.fi.userservice.dto.ArtistUpdateDto;
import cz.muni.fi.userservice.dto.ManagerUpdateDto;
import cz.muni.fi.userservice.model.Artist;
import cz.muni.fi.userservice.model.Manager;
import cz.muni.fi.userservice.repository.ManagerRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @author Tomáš MAREK
 */
@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class ManagerControllerIT {
    @Autowired
    MockMvc mockMvc;

    @Autowired
    ManagerRepository managerRepository;


    @Test
    void register_persistsEntity() throws Exception {
        // Arrange
        ManagerDto managerDto = TestDataFactory.TEST_MANAGER_1_DTO;
        managerDto.setId(null);
        // Act
        mockMvc.perform(post("/api/managers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(managerDto)))
                .andExpect(status().isCreated());

        // Assert
        assertThat(managerRepository.count()).isEqualTo(1);
        assertThat(managerRepository.findByUsername(TestDataFactory.TEST_MANAGER_1_DTO.getUsername())).isPresent();
    }

    @Test
    void register_nullDto_returnsBadRequest() throws Exception {
        // Act
        mockMvc.perform(post("/api/managers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(null)))
                .andExpect(status().isBadRequest());

        // Assert
        System.out.println(managerRepository.count());
        assertThat(managerRepository.findAll().size()).isZero();
    }


//    @Test
//    void updateManager_sourceExists_replacesEntity() throws Exception {
//        // Arrange
//        Manager manager = saveManager(TestDataFactory.TEST_MANAGER_1);
//
//        ManagerUpdateDto updateDto = toManagerUpdateDto(manager);
//        updateDto.setLastName("New Name");
//        // Act
//        //TODO throws UserAlreadyExistsException
//        mockMvc.perform(put("/api/managers/{id}", manager.getId())
//                .contentType(MediaType.APPLICATION_JSON)
//                .content(new ObjectMapper().writeValueAsString(updateDto)))
//                .andExpect(status().isOk());
//        // Assert
//        assertThat(managerRepository.count()).isEqualTo(1);
//        Optional<Manager> updated = managerRepository.findById(manager.getId());
//        assertThat(updated).isPresent();
//        assertThat(updated.get().getCompanyName()).isEqualTo("New Name");
//    }

    @Test
    void updateManager_sourceDoesNotExists_returnNotFound() throws Exception {
        // Arrange
        ManagerUpdateDto updateDto = new ManagerUpdateDto();
        Long emptyId = -123L;

        // Act
        mockMvc.perform(put("/api/managers/{id}", emptyId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(updateDto)))
                .andExpect(status().isNotFound());

        // Assert
        assertThat(managerRepository.count()).isZero();
    }

    @Test
    void getAllManagers_noArtists_returnsEmptyList() throws Exception{
        // Act
        mockMvc.perform(get("/api/managers"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isEmpty());
    }

    @Test
    void getAllManagers_twoArtists_returnsList() throws Exception{
        // Arrange
        Manager manager1 = saveManager(TestDataFactory.TEST_MANAGER_1);
        Manager manager2 = saveManager(TestDataFactory.TEST_MANAGER_2);

        // Act
        mockMvc.perform(get("/api/managers"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].lastName").value(manager1.getLastName()))
                .andExpect(jsonPath("$[1].lastName").value(manager2.getLastName()));
    }

    @Test
    void getById_validId_returnsEntityAndOk() throws Exception {
        // Arrange
        Manager manager = saveManager(TestDataFactory.TEST_MANAGER_1);

        // Act
        mockMvc.perform(get("/api/managers/{id}", manager.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value(manager.getUsername()));
    }

    @Test
    void getById_emptyId_returnsNotFound() throws Exception {
        // Arrange
        Long emptyId = -123L;
        // Act
        mockMvc.perform(get("/api/managers/{id}", emptyId))
                .andExpect(status().isNotFound());
    }

    @Test
    void deleteById_entityExists_isRemoved() throws Exception {
        // Arrange
        Manager manager = saveManager(TestDataFactory.TEST_MANAGER_1);

        // Act
        mockMvc.perform(delete("/api/managers/{id}", manager.getId()))
                .andExpect(status().isNoContent());

        // Assert
        assertThat(managerRepository.count()).isEqualTo(0);
    }

    @Test
    void deleteById_emptyId_returnsNotFound() throws Exception {
        // Arrange
        Long emptyId = -123L;
        // Act
        mockMvc.perform(delete("/api/managers/{id}", emptyId))
                .andExpect(status().isNotFound());
    }

    @Test
    void updateBands_validSet_returnsOkAndUpdatesRepository() throws Exception {
        // Arrange
        Manager manager = saveManager(TestDataFactory.TEST_MANAGER_1);
        Set<Long> newBands = Set.of(4L, 5L, 6L, 7L);

        // Act
        mockMvc.perform(post("/api/managers/bands/{artistId}", manager.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(newBands)))
                .andExpect(status().isOk());

        // Assert
        Optional<Manager> updated = managerRepository.findById(manager.getId());
        assert updated.isPresent();
        assertEquals(updated.get().getManagedBandIds().size(), newBands.size());
        assertThat(updated.get().getManagedBandIds().containsAll(newBands)).isTrue();
    }

    @Test
    void updateBands_invalidBandId_returnsNotFound() throws Exception{
        // Arrange
        Long emptyId = -123L;
        Set<Long> newBands = Set.of(4L, 5L, 6L, 7L);

        // Act
        mockMvc.perform(post("/api/managers/bands/{artistId}", emptyId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(newBands)))
                .andExpect(status().isNotFound());
    }


//    @Test
//    void getManagersByBandIds_fitsAllArtist_returnOkAndAllArtists() throws Exception {
//        // Arrange
//        Manager manager1 = saveManager(TestDataFactory.TEST_MANAGER_1);
//        Manager manager2 = saveManager(TestDataFactory.TEST_MANAGER_2);
//        Set<Long> bands = Set.of(2L);
//        // Act
//        mockMvc.perform(get("/api/managers/bands")
//                        .param("bandIds", String.valueOf(bands)))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$[0].username").value(manager1.getUsername()))
//                .andExpect(jsonPath("$[1].username").value(manager2.getUsername()));
//    }

//    @Test
//    void getManagersByBandIds_fitsOneArtis_returnOkAndArtist() throws Exception{
//        // Arrange
//        Manager manager1 = saveManager(TestDataFactory.TEST_MANAGER_1);
//        saveManager(TestDataFactory.TEST_MANAGER_2);
//        Set<Long> bands = Set.of(1L);
//
//        // Act
//        mockMvc.perform(get("/api/managers/bands")
//                        .param("bandIds", String.valueOf(bands)))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$[0].username").value(manager1.getUsername()));
//
//    }
//
//    @Test
//    void getManagersByBandIds_fitsNoArtist_returnEmptyList() throws Exception {
//        // Arrange
//        saveManager(TestDataFactory.TEST_MANAGER_1);
//        saveManager(TestDataFactory.TEST_MANAGER_2);
//        Set<Long> bands = Set.of(123L);
//        // Act
//        mockMvc.perform(get("/api/managers/bands")
//                        .param("bandIds", String.valueOf(bands)))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$").isEmpty());
//    }

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
