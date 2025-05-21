package cz.muni.fi.bandmanagementservice.rest.it;

import com.fasterxml.jackson.databind.ObjectMapper;
import cz.muni.fi.bandmanagementservice.TestDataFactory;
import cz.muni.fi.bandmanagementservice.dto.BandInfoUpdateDto;
import cz.muni.fi.bandmanagementservice.exceptions.BandNotFoundException;
import cz.muni.fi.bandmanagementservice.model.Band;
import cz.muni.fi.bandmanagementservice.repository.BandRepository;
import cz.muni.fi.bandmanagementservice.rest.it.config.DisableSecurityTestConfig;
import cz.muni.fi.bandmanagementservice.saga.BandMemberSaga;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Set;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Import(DisableSecurityTestConfig.class)
@Transactional
@ActiveProfiles("test")
class BandControllerIT {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    BandRepository bandRepository;

    @MockitoBean
    BandMemberSaga bandMemberSaga;

    @BeforeEach
    void setUp() {
        bandRepository.deleteAll();
    }


    @Test
    void createBand_validRequest_persistsEntity() throws Exception {
        mockMvc.perform(post("/api/bands")
                        .param("name", "Band")
                        .param("musicalStyle", "ROCK")
                        .param("managerId", "42"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("Band"));

        assertThat(bandRepository.findByName("Band")).isPresent();
    }

    @Test
    void testCreateBand_namedAlreadyUsed_badRequest() throws Exception {
        String usedName = "Used Name";
        bandRepository.save(new Band(null, usedName, "Rock", 1L));
        long before = bandRepository.count();

        mockMvc.perform(post("/api/bands")
                        .param("name", usedName)
                        .param("musicalStyle", "ROCK")
                        .param("managerId", "42"))
                .andExpect(status().isBadRequest());

        assertThat(before).isEqualTo(bandRepository.count());
    }

    @Test
    void getBand_existingBand_returnsEntity() throws Exception {
        var band = bandRepository.save(Band.builder()
                .name("Band")
                .musicalStyle("ROCK")
                .managerId(42L)
                .build());

        mockMvc.perform(get("/api/bands/" + band.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Band"));
    }

    @Test
    void getBand_nonExistingBand_returnsNotFound() throws Exception {
        mockMvc.perform(get("/api/bands/999"))
                .andExpect(status().isNotFound());
    }

    @Test
    void updateBand_existingBand_updatesEntity() throws Exception {
        var band = bandRepository.save(Band.builder()
                .name("Band")
                .musicalStyle("ROCK")
                .managerId(42L)
                .build());

        BandInfoUpdateDto request = new BandInfoUpdateDto();
        request.setName("Updated Band");
        request.setMusicalStyle("Mongolian rap");
        request.setManagerId(41L);

        mockMvc.perform(patch("/api/bands/{bandId}", band.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(request)))
                .andExpect(status().isOk());
    }

    @Test
    void updateBand_nonExistingBand_returnsNotFound() throws Exception {
        BandInfoUpdateDto request = TestDataFactory.setUpBandInfoUpdateDto1();

        mockMvc.perform(patch("/api/bands/{bandId}", 999L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(request)))
                .andExpect(status().isNotFound());
    }

    @Test
    void getAllBands_multipleBands_returnsAllEntities() throws Exception {
        bandRepository.save(Band.builder()
                .name("Band1")
                .musicalStyle("ROCK")
                .managerId(42L)
                .build());
        bandRepository.save(Band.builder()
                .name("Band2")
                .musicalStyle("POP")
                .managerId(43L)
                .build());

        mockMvc.perform(get("/api/bands"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Band1"))
                .andExpect(jsonPath("$[1].name").value("Band2"));
    }

    @Test
    void getAllBands_noBands_returnsEmptyList() throws Exception {
        mockMvc.perform(get("/api/bands"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isEmpty());
    }

    @Test
    void addMember_validBandAndMember_returnsUpdatedBand() throws Exception {
        Band band = bandRepository.save(Band.builder()
                .name("Band")
                .musicalStyle("ROCK")
                .managerId(42L)
                .build());

        Long newMemberId = 999L;

        mockMvc.perform(patch("/api/bands/{bandId}/members/{memberId}", band.getId(), newMemberId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.members").isArray());
    }

    @Test
    void addMember_invalidBandAndMember_returnsNotFound() throws Exception {
        Long bandId = 999L;
        Long memberId = 43L;
        when(bandMemberSaga.startAddMember(bandId, memberId))
                .thenThrow(new BandNotFoundException(bandId));

        mockMvc.perform(patch("/api/bands/{bandId}/members/{memberId}", bandId, memberId))
                .andExpect(status().isNotFound());
    }

    @Test
    void removeMember_validBandAndMember_returnsNoContent() throws Exception {
        Band band = Band.builder()
                .name("Band")
                .musicalStyle("ROCK")
                .managerId(42L)
                .build();
        band.setMembers(new HashSet<>(Set.of(43L, 44L)));
        band = bandRepository.save(band);

        mockMvc.perform(delete("/api/bands/{bandId}/members/{memberId}", band.getId(), 43L))
                .andExpect(status().isNoContent());
    }

    @Test
    void removeMember_invalidBandAndMember_returnsNotFound() throws Exception {
        Long bandId = 999L;
        Long memberId = 43L;

        when(bandMemberSaga.startRemoveMember(bandId, memberId))
                .thenThrow(new BandNotFoundException(bandId));

        mockMvc.perform(delete("/api/bands/{bandId}/members/{memberId}", bandId, memberId))
                .andExpect(status().isNotFound());
    }
}
