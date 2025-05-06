package cz.muni.fi.bandmanagementservice.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import cz.muni.fi.bandmanagementservice.artemis.BandEventProducer;
import cz.muni.fi.bandmanagementservice.dto.BandInfoUpdateRequest;
import cz.muni.fi.bandmanagementservice.model.Band;
import cz.muni.fi.bandmanagementservice.repository.BandRepository;
import cz.muni.fi.events.band.BandAddMemberEvent;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class BandRestControllerIT {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    BandRepository bandRepository;

    @MockitoBean
    BandEventProducer bandEventProducer;

    @BeforeEach
    void setUp() {
        bandRepository.deleteAll();
    }

    @Test
    void createBand_persistsEntity() throws Exception {
        mockMvc.perform(post("/api/bands")
                        .param("name", "Band")
                        .param("musicalStyle", "ROCK")
                        .param("managerId", "42"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("Band"));

        assertThat(bandRepository.findByName("Band")).isPresent();
    }

    @Test
    void getBand_returnsEntity() throws Exception {
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
    void getBand_notFound() throws Exception {
        mockMvc.perform(get("/api/bands/999"))
                .andExpect(status().isNotFound());
    }

    @Test
    void updateBand_updatesEntity() throws Exception {
        var band = bandRepository.save(Band.builder()
                .name("Band")
                .musicalStyle("ROCK")
                .managerId(42L)
                .build());

        BandInfoUpdateRequest request = new BandInfoUpdateRequest();
        request.setId(band.getId());
        request.setName("Updated Band");
        request.setMusicalStyle("Mongolian rap");
        request.setManagerId(41L);

        mockMvc.perform(patch("/api/bands")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(request)))
                .andExpect(status().isOk());
    }

    @Test
    void updateBand_notFound() throws Exception {
        BandInfoUpdateRequest request = new BandInfoUpdateRequest();
        request.setId(999L);
        request.setName("Updated Band");
        request.setMusicalStyle("POP");

        mockMvc.perform(patch("/api/bands")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(request)))
                .andExpect(status().isNotFound());
    }

    @Test
    void getAllBands_returnsAllEntities() throws Exception {
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
    void getAllBands_empty() throws Exception {
        mockMvc.perform(get("/api/bands"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isEmpty());
    }

    @Test
    void addMember_addsMember_AndPublishesEvent() throws Exception {
        Long newMemberId = 43L;
        Band band = bandRepository.save(Band.builder()
                .name("Band")
                .musicalStyle("ROCK")
                .managerId(42L)
                .build());

        mockMvc.perform(patch("/api/bands/{bandId}/members/{memberId}", band.getId(), newMemberId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.members").isArray())
                .andExpect(jsonPath("$.members[0]").value(newMemberId.intValue()));

        Band updatedBand = bandRepository.findById(band.getId()).orElseThrow();
        assertThat(updatedBand.getMembers().contains(newMemberId)).isTrue();

        ArgumentCaptor<BandAddMemberEvent> eventCaptor = ArgumentCaptor.forClass(BandAddMemberEvent.class);
        verify(bandEventProducer).sendBandAddMemberEvent(eventCaptor.capture());
        assertThat(eventCaptor.getValue().getBandId()).isEqualTo(band.getId());
        assertThat(eventCaptor.getValue().getMemberId()).isEqualTo(newMemberId);
    }

    @Test
    void addMember_notFound() throws Exception {
        mockMvc.perform(patch("/api/bands/999/members")
                        .param("memberId", "43"))
                .andExpect(status().isNotFound());
    }

    @Test
    void removeMember_removesMember_AndPublishesEvent() throws Exception {
        var band = bandRepository.save(Band.builder()
                .name("Band")
                .musicalStyle("ROCK")
                .managerId(42L)
                .build());
        band.addMember(43L);

        mockMvc.perform(delete("/api/bands/" + band.getId() + "/members/43"))
                .andExpect(status().isNoContent());

        assertThat(bandRepository.findById(band.getId()).get().getMembers().contains(43L)).isFalse();
        verify(bandEventProducer).sendBandRemoveMemberEvent(any());
    }

    @Test
    void removeMember_notFound() throws Exception {
        mockMvc.perform(delete("/api/bands/999/members/43"))
                .andExpect(status().isNotFound());
    }
}
