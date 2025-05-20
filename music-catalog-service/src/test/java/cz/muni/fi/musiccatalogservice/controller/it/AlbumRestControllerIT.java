package cz.muni.fi.musiccatalogservice.controller.it;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import cz.muni.fi.musiccatalogservice.controller.it.config.DisableSecurityTestConfig;
import cz.muni.fi.musiccatalogservice.dto.AlbumDto;
import cz.muni.fi.musiccatalogservice.model.Album;
import cz.muni.fi.musiccatalogservice.model.Song;
import cz.muni.fi.musiccatalogservice.repository.AlbumRepository;
import cz.muni.fi.musiccatalogservice.repository.SongRepository;
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
import java.time.LocalDateTime;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Import(DisableSecurityTestConfig.class)
@Transactional
class AlbumRestControllerIT {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private AlbumRepository albumRepository;

    @Autowired
    private SongRepository songRepository;

    private ObjectMapper objectMapper;
    private Album testAlbum;
    private Song testSong;

    @BeforeEach
    void setup() {
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd"));

        // Clean the database
        songRepository.deleteAll();
        albumRepository.deleteAll();

        // Create test album
        testAlbum = new Album();
        testAlbum.setTitle("Test Album");
        testAlbum.setBandId(1L);
        testAlbum.setReleaseDate(LocalDateTime.now().plusDays(30));
        testAlbum = albumRepository.save(testAlbum);

        // Create test song associated with the album
        testSong = new Song();
        testSong.setName("Test Song");
        testSong.setBandId(1L);
        testSong.setAlbum(testAlbum);
        testSong = songRepository.save(testSong);
    }


    @Test
    void testGetAllAlbums() throws Exception {
        mockMvc.perform(get("/api/albums"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].title", is("Test Album")))
                .andExpect(jsonPath("$[0].releaseDate").exists());
    }

    @Test
    void testGetAlbumById() throws Exception {
        mockMvc.perform(get("/api/albums/{id}", testAlbum.getId()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(testAlbum.getId().intValue())))
                .andExpect(jsonPath("$.title", is("Test Album")))
                .andExpect(jsonPath("$.releaseDate").exists());
    }

    @Test
    void testGetAlbumByInvalidId() throws Exception {
        mockMvc.perform(get("/api/albums/{id}", 999L))
                .andExpect(status().isNotFound());
    }


    @Test
    void testCreateAlbum() throws Exception {
        AlbumDto albumDTO = new AlbumDto();
        albumDTO.setTitle("New Album");
        albumDTO.setReleaseDate(LocalDateTime.now().minusDays(60));
        albumDTO.setBandId(1L);

        mockMvc.perform(post("/api/albums")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(albumDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.title", is("New Album")))
                .andExpect(jsonPath("$.releaseDate").exists());
    }



    @Test
    void testCreateInvalidAlbum() throws Exception {
        AlbumDto albumDTO = new AlbumDto();
        // Empty title and null releaseDate

        mockMvc.perform(post("/api/albums")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(albumDTO)))
                .andExpect(status().isBadRequest());
    }



    @Test
    void testUpdateAlbum() throws Exception {
        AlbumDto albumDTO = new AlbumDto();
        albumDTO.setTitle("Updated Album");
        albumDTO.setReleaseDate(LocalDateTime.now().minusDays(60));
        albumDTO.setBandId(1L);

        mockMvc.perform(put("/api/albums/{id}", testAlbum.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(albumDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title", is("Updated Album")));
    }

    @Test
    void testUpdateInvalidAlbum() throws Exception {
        AlbumDto albumDTO = new AlbumDto();
        albumDTO.setTitle("Updated Album");
        albumDTO.setReleaseDate(LocalDateTime.now().plusDays(90));

        mockMvc.perform(put("/api/albums/{id}", 999L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(albumDTO)))
                .andExpect(status().isBadRequest());
    }


    @Test
    void testDeleteAlbum() throws Exception {
        mockMvc.perform(delete("/api/albums/{id}", testAlbum.getId()))
                .andExpect(status().isNoContent());

        // Verify the album is deleted
        mockMvc.perform(get("/api/albums/{id}", testAlbum.getId()))
                .andExpect(status().isNotFound());
    }

    @Test
    void testDeleteInvalidAlbum() throws Exception {
        mockMvc.perform(delete("/api/albums/{id}", 999L))
                .andExpect(status().isNotFound());
    }


    @Test
    void testGetAlbumByBand() throws Exception {
        mockMvc.perform(get("/api/albums/band/{bandId}", 1L))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].title", is("Test Album")));
    }

    @Test
    void testGetEmptyListForInvalidBand() throws Exception {
        mockMvc.perform(get("/api/albums/band/{bandId}", 999L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)));
    }
}

