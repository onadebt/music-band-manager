package cz.muni.fi.userservice.rest.it;

import com.fasterxml.jackson.databind.ObjectMapper;
import cz.muni.fi.userservice.TestDataFactory;
import cz.muni.fi.userservice.dto.ArtistDto;
import cz.muni.fi.userservice.dto.ArtistUpdateDto;
import cz.muni.fi.userservice.model.Artist;
import cz.muni.fi.userservice.repository.ArtistRepository;
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

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.hasSize;
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
class ArtistControllerIT {
    @Autowired
    MockMvc mockMvc;

    @Autowired
    ArtistRepository artistRepository;

    @Autowired
    ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        artistRepository.deleteAll();
    }

    @Test
    void register_validArtist_persistsEntity() throws Exception {
        // Arrange
        long before = artistRepository.count();
        ArtistDto registered = TestDataFactory.setUpTestArtist1Dto();
        registered.setId(null);

        // Act
        mockMvc.perform(post("/api/artists")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(registered)))
                .andExpect(status().isOk());

        // Assert
        assertThat(artistRepository.count()).isEqualTo(before + 1);
        assertThat(artistRepository.findByUsername(registered.getUsername())).isPresent();
    }

    @Test
    void register_nullDto_returnsBadRequest() throws Exception {
        // Arrange
        long before = artistRepository.count();

        // Act
        mockMvc.perform(post("/api/artists")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());

        // Assert
        assertThat(artistRepository.count()).isEqualTo(before);
    }

    @Test
    void update_sourceDoesNotExists_returnNotFound() throws Exception {
        // Arrange
        ArtistUpdateDto updateDto = new ArtistUpdateDto();
        Long emptyId = -123L;
        long before = artistRepository.count();

        // Act
        mockMvc.perform(put("/api/artists/{id}", emptyId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateDto)))
                .andExpect(status().isNotFound());

        // Assert
        assertThat(artistRepository.count()).isEqualTo(before);
    }

    @Test
    void getAllArtists_noArtists_returnsEmptyList() throws Exception {
        // Act
        mockMvc.perform(get("/api/artists"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isEmpty());
    }

    @Test
    void getAllArtists_twoArtists_returnsList() throws Exception {
        Artist artist1 = TestDataFactory.setUpTestArtist1();
        Artist artist2 = TestDataFactory.setUpTestArtist2();
        artist1.setId(null);
        artist2.setId(null);

        artistRepository.saveAll(List.of(artist1, artist2));

        // Act
        mockMvc.perform(get("/api/artists"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[*].lastName",
                        containsInAnyOrder(artist1.getLastName(), artist2.getLastName())));
    }

    @Test
    void getArtistById_validId_returnsEntityAndOk() throws Exception {
        // Arrange
        Artist artist = TestDataFactory.setUpTestArtist1();
        artist.setId(null);
        artist = artistRepository.save(artist);

        // Act
        mockMvc.perform(get("/api/artists/{id}", artist.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.stageName").value(artist.getStageName()));
    }

    @Test
    void getArtistById_emptyId_returnsNotFound() throws Exception {
        // Arrange
        Long emptyId = -123L;
        // Act
        mockMvc.perform(get("/api/artists/{id}", emptyId))
                .andExpect(status().isNotFound());
    }

    @Test
    void deleteArtist_entityExists_isRemoved() throws Exception {
        // Arrange
        long before = artistRepository.count();

        Artist artist = TestDataFactory.setUpTestArtist1();
        artist.setId(null);
        artist = artistRepository.save(artist);

        // Act
        mockMvc.perform(delete("/api/artists/{id}", artist.getId()))
                .andExpect(status().isNoContent());

        // Assert
        assertThat(artistRepository.count()).isEqualTo(before);
    }

    @Test
    void deleteArtist_emptyId_returnsNotFound() throws Exception {
        // Arrange
        Long emptyId = -123L;
        // Act
        mockMvc.perform(delete("/api/artists/{id}", emptyId))
                .andExpect(status().isNotFound());
    }


    @Test
    void getArtistByUsername_validUsername_returnEntityAndOk() throws Exception {
        // Arrange
        Artist artist = saveArtist(TestDataFactory.setUpTestArtist1());

        // Act
        mockMvc.perform(get("/api/artists/username/{username}", artist.getUsername()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value(artist.getUsername()))
                .andExpect(jsonPath("$.lastName").value(artist.getLastName()));
    }

    @Test
    void getArtistByUsername_invalidUsername_returnNotFound() throws Exception {
        String invalidUsername = "invalidUsername";
        // Act
        mockMvc.perform(get("/api/artists/username/{username}", invalidUsername))
                .andExpect(status().isNotFound());
    }

    @Test
    void updateBands_validSet_returnsOkAndUpdatesRepository() throws Exception {
        // Arrange
        Artist artist = saveArtist(TestDataFactory.setUpTestArtist1());
        Set<Long> newBands = Set.of(4L, 5L, 6L, 7L);

        // Act
        mockMvc.perform(patch("/api/artists/bands/{artistId}", artist.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newBands)))
                .andExpect(status().isOk());

        // Assert
        Optional<Artist> updated = artistRepository.findById(artist.getId());
        assert updated.isPresent();
        assertEquals(updated.get().getBandIds().size(), newBands.size());
        assertThat(updated.get().getBandIds().containsAll(newBands)).isTrue();
    }

    @Test
    void updateBands_invalidBandId_returnsNotFound() throws Exception {
        // Arrange
        Long emptyId = -123L;
        Set<Long> newBands = Set.of(4L, 5L, 6L, 7L);

        // Act
        mockMvc.perform(patch("/api/artists/bands/{artistId}", emptyId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newBands)))
                .andExpect(status().isNotFound());
    }

    @Test
    void update_sourceExists_replacesEntity() throws Exception {
        // Arrange
        Artist artist = TestDataFactory.setUpTestArtist1();
        artist = saveArtist(artist);
        ArtistUpdateDto updateDto = toArtistUpdateDto(artist);
        updateDto.setStageName("New Name");

        // Act
        mockMvc.perform(put("/api/artists/{id}", artist.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateDto)))
                .andExpect(status().isOk());

        // Assert
        assertThat(artistRepository.count()).isEqualTo(1);
        Optional<Artist> updated = artistRepository.findById(artist.getId());
        assertThat(updated).isPresent();
        assertThat(updated.get().getStageName()).isEqualTo("New Name");
    }


    @Test
    void getArtistsByBandIds_findsAllArtist_returnOkAndAllArtists() throws Exception {
        // Arrange
        Artist artist1 = TestDataFactory.setUpTestArtist1();
        Artist artist2 = TestDataFactory.setUpTestArtist2();
        artist1.setId(null);
        artist2.setId(null);
        artist1 = saveArtist(artist1);
        artist2 = saveArtist(artist2);

        // Act
        mockMvc.perform(get("/api/artists/bands")
                        .param("bandIds", "2"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[*].username",
                        containsInAnyOrder(artist1.getUsername(), artist2.getUsername())));
    }

    @Test
    void getArtistsByBandIds_fitsOneArtis_returnOkAndArtist() throws Exception{
        // Arrange
        Artist artist1 = TestDataFactory.setUpTestArtist1();
        artist1.setId(null);
        artist1 = saveArtist(artist1);

        // Act
        mockMvc.perform(get("/api/artists/bands")
                        .param("bandIds", "1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].username").value(artist1.getUsername()));
    }

    @Test
    void getArtistsByBandIds_fitsNoArtist_returnEmptyList() throws Exception {
        // Arrange
        Artist artist1 = TestDataFactory.setUpTestArtist1();
        Artist artist2 = TestDataFactory.setUpTestArtist2();
        artist1.setId(null);
        artist2.setId(null);
        artist1 = saveArtist(artist1);
        artist2 = saveArtist(artist2);

        // Act
        mockMvc.perform(get("/api/artists/bands")
                        .param("bandIds", "123"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isEmpty());
    }

    @Test
    void linkArtistToBand_validId_returnOkAndArtist() throws Exception {
        // Arrange
        Artist artist = TestDataFactory.setUpTestArtist1();
        artist.setId(null);
        artist.setBandIds(new HashSet<>());
        artist = saveArtist(artist);
        Long bandId = 123L;

        // Act & Assert
        mockMvc.perform(patch("/api/artists/link/{artistId}/{bandId}", artist.getId(), bandId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.bandIds", hasSize(1)))
                .andExpect(jsonPath("$.bandIds[0]").value(bandId));
    }

    @Test
    void linkArtistToBand_invalidId_returnNotFound() throws Exception {
        // Arrange
        Long emptyId = -123L;
        Long bandId = 123L;

        // Act & Assert
        mockMvc.perform(patch("/api/artists/link/{artistId}/{bandId}", emptyId, bandId))
                .andExpect(status().isNotFound());
    }

    @Test
    void unlinkArtistFromBand_validId_returnOkAndArtist() throws Exception {
        // Arrange
        Artist artist = TestDataFactory.setUpTestArtist1();
        artist.setId(null);
        artist.setBandIds(new HashSet<>(Set.of(123L, 456L)));
        artist = saveArtist(artist);
        Long bandId = 123L;

        // Act & Assert
        mockMvc.perform(patch("/api/artists/unlink/{artistId}/{bandId}", artist.getId(), bandId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.bandIds", hasSize(1)))
                .andExpect(jsonPath("$.bandIds[0]").value(456L));
    }

    @Test
    void unlinkArtistFromBand_invalidId_returnNotFound() throws Exception {
        // Arrange
        Long emptyId = -123L;
        Long bandId = 123L;

        // Act & Assert
        mockMvc.perform(patch("/api/artists/unlink/{artistId}/{bandId}", emptyId, bandId))
                .andExpect(status().isNotFound());
    }


    private Artist saveArtist(Artist artist) {
        artist.setId(null);
        artist = artistRepository.save(artist);
        return artist;
    }


    private ArtistUpdateDto toArtistUpdateDto(Artist artist) {
        ArtistUpdateDto artistUpdateDto = new ArtistUpdateDto();
        artistUpdateDto.setStageName(artist.getStageName());
        artistUpdateDto.setBio(artist.getBio());
        artistUpdateDto.setSkills(artist.getSkills());
        artistUpdateDto.setBandIds(artist.getBandIds());

        artistUpdateDto.setUsername(artist.getUsername());
        artistUpdateDto.setEmail(artist.getEmail());
        artistUpdateDto.setFirstName(artist.getFirstName());
        artistUpdateDto.setLastName(artist.getLastName());
        artistUpdateDto.setRole(artist.getRole());
        artistUpdateDto.setUsername(artist.getUsername());

        return artistUpdateDto;
    }
}
