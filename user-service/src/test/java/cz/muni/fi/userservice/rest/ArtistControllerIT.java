package cz.muni.fi.userservice.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import cz.muni.fi.userservice.TestDataFactory;
import cz.muni.fi.userservice.artemis.BandOfferEventListener;
import cz.muni.fi.userservice.dto.ArtistDto;
import cz.muni.fi.userservice.dto.ArtistUpdateDto;
import cz.muni.fi.userservice.mappers.ArtistMapper;
import cz.muni.fi.userservice.model.Artist;
import cz.muni.fi.userservice.repository.ArtistRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
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
class ArtistControllerIT {
    @Autowired
    MockMvc mockMvc;

    @Autowired
    ArtistRepository artistRepository;

    @Autowired
    ArtistMapper artistMapper;

    @MockitoBean
    BandOfferEventListener bandOfferEventListener;

    @Test
    void register_persistsEntity() throws Exception {
        // Act
        mockMvc.perform(post("/api/artists")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(TestDataFactory.TEST_ARTIST_1_DTO_NO_ID)))
                .andExpect(status().isOk());

        // Assert
        assertThat(artistRepository.count()).isEqualTo(1);
        assertThat(artistRepository.findByUsername(TestDataFactory.TEST_ARTIST_1_DTO.getUsername())).isPresent();
    }

    @Test
    void register_nullDto_returnsBadRequest() throws Exception {
        // Arrange

        // Act
        mockMvc.perform(post("/api/artists")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(null)))
                .andExpect(status().isBadRequest());

        // Assert
        System.out.println(artistRepository.count());
        assertThat(artistRepository.findAll().size()).isZero();
    }


//    @Test
//    void update_sourceExists_replacesEntity() throws Exception {
//        // Arrange
//        Artist artist = TestDataFactory.TEST_ARTIST_2;
//        artist.setId(null);
//        artist = artistRepository.save(artist);
//
//        ArtistUpdateDto updateDto = toArtistUpdateDto(artist);
//        updateDto.setStageName("New Name");
//        // Act
//        //TODO return code (updates, but returns 404)
//        mockMvc.perform(put("/api/artists/{id}", artist.getId())
//                .contentType(MediaType.APPLICATION_JSON)
//                .content(new ObjectMapper().writeValueAsString(updateDto)))
//                .andExpect(status().isOk());
//
//        // Assert
//        assertThat(artistRepository.count()).isEqualTo(1);
//        Optional<Artist> updated = artistRepository.findById(artist.getId());
//        assertThat(updated).isPresent();
//        assertThat(updated.get().getStageName()).isEqualTo("New Name");
//    }

    @Test
    void update_sourceDoesNotExists_returnNotFound() throws Exception {
        // Arrange
        ArtistUpdateDto updateDto = new ArtistUpdateDto();
        Long emptyId = -123L;

        // Act
        mockMvc.perform(put("/api/artists/{id}", emptyId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(updateDto)))
                .andExpect(status().isNotFound());

        // Assert
        assertThat(artistRepository.count()).isZero();
    }

    @Test
    void getAllArtists_noArtists_returnsEmptyList() throws Exception{
        // Act
        mockMvc.perform(get("/api/artists"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isEmpty());
    }

    @Test
    void getAllArtists_twoArtists_returnsList() throws Exception{
        // Arrange
        Artist artist1 = TestDataFactory.TEST_ARTIST_1;
        Artist artist2 = TestDataFactory.TEST_ARTIST_2;
        artist1.setId(null);
        artist2.setId(null);

        artistRepository.save(artist1);
        artistRepository.save(artist2);

        // Act
        mockMvc.perform(get("/api/artists"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].lastName").value("Lindemann"))
                .andExpect(jsonPath("$[1].lastName").value("Brodén"));
    }

    @Test
    void getArtistById_validId_returnsEntityAndOk() throws Exception {
        // Arrange
        Artist artist = TestDataFactory.TEST_ARTIST_1;
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
        Artist artist = TestDataFactory.TEST_ARTIST_1;
        artist.setId(null);
        artist = artistRepository.save(artist);

        // Act
        mockMvc.perform(delete("/api/artists/{id}", artist.getId()))
                .andExpect(status().isNoContent());

        // Assert
        assertThat(artistRepository.count()).isEqualTo(0);
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
        Artist artist = saveArtist(TestDataFactory.TEST_ARTIST_1);

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
        Artist artist = saveArtist(TestDataFactory.TEST_ARTIST_1);
        Set<Long> newBands = Set.of(4L, 5L, 6L, 7L);

        // Act
        mockMvc.perform(post("/api/artists/bands/{artistId}", artist.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(newBands)))
                .andExpect(status().isOk());

        // Assert
        Optional<Artist> updated = artistRepository.findById(artist.getId());
        assert updated.isPresent();
        assertEquals(updated.get().getBandIds().size(), newBands.size());
        assertThat(updated.get().getBandIds().containsAll(newBands)).isTrue();
    }

    @Test
    void updateBands_invalidBandId_returnsNotFound() throws Exception{
        // Arrange
        Long emptyId = -123L;
        Set<Long> newBands = Set.of(4L, 5L, 6L, 7L);

        // Act
        mockMvc.perform(post("/api/artists/bands/{artistId}", emptyId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(newBands)))
                .andExpect(status().isNotFound());
    }


//    @Test
//    void getArtistsByBandIds_fitsAllArtist_returnOkAndAllArtists() throws Exception {
//        // Arrange
//        Artist artist1 = saveArtist(TestDataFactory.TEST_ARTIST_1);
//        Artist artist2 = saveArtist(TestDataFactory.TEST_ARTIST_2);
//        Set<Long> bands = Set.of(2L);
//        // Act
//        mockMvc.perform(get("/api/artists/bands")
//                        .param("bandIds", String.valueOf(bands)))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$[0].username").value(artist1.getUsername()))
//                .andExpect(jsonPath("$[1].username").value(artist2.getUsername()));
//    }
//
//    @Test
//    void getArtistsByBandIds_fitsOneArtis_returnOkAndArtist() throws Exception{
//        // Arrange
//        Artist artist1 = saveArtist(TestDataFactory.TEST_ARTIST_1);
//        saveArtist(TestDataFactory.TEST_ARTIST_2);
//        Set<Long> bands = Set.of(1L);
//
//        // Act
//        mockMvc.perform(get("/api/artists/bands")
//                        .param("bandIds", String.valueOf(bands)))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$[0].username").value(artist1.getUsername()));
//
//    }
//
//    @Test
//    void getArtistsByBandIds_fitsNoArtist_returnEmptyList() throws Exception {
//        // Arrange
//        saveArtist(TestDataFactory.TEST_ARTIST_1);
//        saveArtist(TestDataFactory.TEST_ARTIST_2);
//        Set<Long> bands = Set.of(123L);
//        // Act
//        mockMvc.perform(get("/api/artists/bands")
//                        .param("bandIds", String.valueOf(bands)))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$").isEmpty());
//    }

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
