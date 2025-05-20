package cz.muni.fi.bandmanagementservice.rest.it;

import com.fasterxml.jackson.databind.ObjectMapper;
import cz.muni.fi.bandmanagementservice.model.Band;
import cz.muni.fi.bandmanagementservice.model.BandOffer;
import cz.muni.fi.bandmanagementservice.repository.BandOfferRepository;
import cz.muni.fi.bandmanagementservice.repository.BandRepository;
import cz.muni.fi.bandmanagementservice.rest.it.config.DisableSecurityTestConfig;
import cz.muni.fi.bandmanagementservice.saga.BandOfferSaga;
import cz.muni.fi.shared.enums.BandOfferStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Import(DisableSecurityTestConfig.class)
@Transactional
@ActiveProfiles("test")
class BandOfferControllerIT {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private BandOfferRepository bandOfferRepository;

    @Autowired
    private BandRepository bandRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private BandOfferSaga bandOfferSaga;


    @BeforeEach
    void setUp() {
        bandOfferRepository.deleteAll();
        bandRepository.deleteAll();
    }

    @Test
    void createBandOffer_validBandAndMusician_createsNewOffer() throws Exception {
        Long managerId = 3L;
        Band band = bandRepository.save(Band.builder()
                .name("Test Band")
                .musicalStyle("ROCK")
                .managerId(managerId)
                .build());

        Long musicianId = 2L;

        mockMvc.perform(post("/api/bands/offers")
                        .param("bandId", band.getId().toString())
                        .param("invitedMusicianId", musicianId.toString())
                        .param("offeringManagerId", managerId.toString()))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.bandId").value(band.getId()))
                .andExpect(jsonPath("$.invitedMusicianId").value(musicianId))
                .andExpect(jsonPath("$.offeringManagerId").value(managerId))
                .andExpect(jsonPath("$.status").value("PENDING"));

        assertThat(bandOfferRepository.findAll()).hasSize(1);
    }

    @Test
    void getBandOffer_existingOffer_returnsOffer() throws Exception {
        BandOffer offer = bandOfferRepository.save(
                new BandOffer(null, 1L, 2L, 3L));

        mockMvc.perform(get("/api/bands/offers/{offerId}", offer.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(offer.getId()))
                .andExpect(jsonPath("$.bandId").value(1L))
                .andExpect(jsonPath("$.invitedMusicianId").value(2L))
                .andExpect(jsonPath("$.offeringManagerId").value(3L));
    }

    @Test
    void acceptBandOffer_pendingOffer_changesStatusAndSendsEvent() throws Exception {
        Long musicianId = 2L;
        Long managerId = 3L;
        Band band = bandRepository.save(Band.builder()
                .name("Test Band 2")
                .musicalStyle("ROCK")
                .managerId(managerId)
                .build());

        BandOffer offer = bandOfferRepository.save(
                new BandOffer(null, band.getId(), musicianId, band.getManagerId())
        );
        BandOffer accepted = new BandOffer(offer.getId(), band.getId(), musicianId, band.getManagerId());
        accepted.acceptOffer();

        when(bandOfferSaga.startAcceptBandOffer(offer.getId())).thenReturn(accepted);

        mockMvc.perform(post("/api/bands/offers/{offerId}/accept", offer.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("ACCEPTED"));
        verify(bandOfferSaga).startAcceptBandOffer(offer.getId());
    }

    @Test
    void rejectBandOffer_pendingOffer_changesStatus() throws Exception {
        BandOffer offer = bandOfferRepository.save(
                new BandOffer(null, 1L, 2L, 3L));

        mockMvc.perform(post("/api/bands/offers/{offerId}/reject", offer.getId()))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.status").value("REJECTED"));

        BandOffer updatedOffer = bandOfferRepository.findById(offer.getId()).orElseThrow();
        assertThat(updatedOffer.getStatus()).isEqualTo(BandOfferStatus.REJECTED);
    }

    @Test
    void revokeBandOffer_existingOffer_deletesOffer() throws Exception {
        BandOffer offer = bandOfferRepository.save(
                new BandOffer(null, 1L, 2L, 3L));

        mockMvc.perform(post("/api/bands/offers/{offerId}/revokes", offer.getId()))
                .andExpect(status().isOk());

        assertThat(bandOfferRepository.findById(offer.getId())).isEmpty();
    }

    @Test
    void getAllBandOffers_offersExist_returnsAllOffers() throws Exception {
        BandOffer offer1 = bandOfferRepository.save(new BandOffer(null, 1L, 2L, 3L));
        BandOffer offer2 = bandOfferRepository.save(new BandOffer(null, 4L, 5L, 6L));

        mockMvc.perform(get("/api/bands/offers"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id").value(offer1.getId()))
                .andExpect(jsonPath("$[1].id").value(offer2.getId()));
    }

    @Test
    void getBandOffersByBand_bandId_filtersOffers() throws Exception {
        Long targetBandId = 1L;
        bandOfferRepository.save(new BandOffer(null, targetBandId, 2L, 3L));
        bandOfferRepository.save(new BandOffer(null, targetBandId, 4L, 5L));
        bandOfferRepository.save(new BandOffer(null, 99L, 6L, 7L));

        mockMvc.perform(get("/api/bands/offers/byBand/{bandId}", targetBandId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[*].bandId", everyItem(is(targetBandId.intValue()))));
    }

    @Test
    void getBandOffersByMusician_musicianId_filtersOffers() throws Exception {
        Long targetMusicianId = 2L;
        bandOfferRepository.save(new BandOffer(null, 1L, targetMusicianId, 3L));
        bandOfferRepository.save(new BandOffer(null, 4L, targetMusicianId, 5L));
        bandOfferRepository.save(new BandOffer(null, 6L, 99L, 7L));

        mockMvc.perform(get("/api/bands/offers/byMusician/{musicianId}", targetMusicianId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[*].invitedMusicianId", everyItem(is(targetMusicianId.intValue()))));
    }

    @Test
    void acceptBandOffer_alreadyAcceptedOffer_returnsOk() throws Exception {
        Long musicianId = 2L;
        Long managerId = 3L;

        Band band = bandRepository.save(Band.builder()
                .name("Band")
                .musicalStyle("ROCK")
                .managerId(managerId)
                .build());

        BandOffer offer = new BandOffer(null, band.getId(), musicianId, managerId);
        offer.setStatus(BandOfferStatus.ACCEPTED);
        bandOfferRepository.save(offer);

        mockMvc.perform(post("/api/bands/offers/{offerId}/accept", offer.getId()))
                .andExpect(status().isOk());
    }


    @Test
    void getBandOffer_nonExistentOffer_returnsNotFound() throws Exception {
        mockMvc.perform(get("/api/bands/offers/999"))
                .andExpect(status().isNotFound());
    }
}
