package cz.muni.fi.musiccatalogservice.controller.it;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import cz.muni.fi.musiccatalogservice.controller.it.config.DisableSecurityTestConfig;
import cz.muni.fi.musiccatalogservice.dto.AlbumDto;
import cz.muni.fi.musiccatalogservice.dto.SongDto;
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
        // Use minusDays instead of plusDays to make the date in the past
        testAlbum.setReleaseDate(LocalDateTime.now().minusDays(30));
        testAlbum = albumRepository.save(testAlbum);

        testSong = new Song();
        testSong.setName("Test Song");
        testSong.setBandId(1L);
        testSong.setDuration(180);
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
        AlbumDto albumDto = new AlbumDto();
        albumDto.setTitle("New Album");
        albumDto.setReleaseDate(LocalDateTime.now().minusDays(60));
        albumDto.setBandId(1L);

        mockMvc.perform(post("/api/albums")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(albumDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.title", is("New Album")))
                .andExpect(jsonPath("$.releaseDate").exists());
    }



    @Test
    void testCreateInvalidAlbum() throws Exception {
        AlbumDto albumDto = new AlbumDto();
        // Empty title and null releaseDate

        mockMvc.perform(post("/api/albums")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(albumDto)))
                .andExpect(status().isBadRequest());
    }



    @Test
    void testUpdateAlbum() throws Exception {
        AlbumDto albumDto = new AlbumDto();
        albumDto.setTitle("Updated Album");
        albumDto.setReleaseDate(LocalDateTime.now().minusDays(60));
        albumDto.setBandId(1L);

        mockMvc.perform(put("/api/albums/{id}", testAlbum.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(albumDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title", is("Updated Album")));
    }

    @Test
    void testUpdateInvalidAlbum() throws Exception {
        AlbumDto albumDto = new AlbumDto();
        albumDto.setTitle("Updated Album");
        albumDto.setReleaseDate(LocalDateTime.now().plusDays(90));

        mockMvc.perform(put("/api/albums/{id}", 999L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(albumDto)))
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

    @Test
    void testAddSongToAlbum() throws Exception {
        SongDto songDto = new SongDto();
        songDto.setName("New Album Song");
        songDto.setBandId(1L);
        songDto.setDuration(240);

        mockMvc.perform(post("/api/albums/{albumId}/songs", testAlbum.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(songDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name", is("New Album Song")))
                .andExpect(jsonPath("$.duration", is(240)))
                .andExpect(jsonPath("$.albumId", is(testAlbum.getId().intValue())));

        mockMvc.perform(get("/api/albums/{id}", testAlbum.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.songs", hasSize(2))) // Original song + newly added one
                .andExpect(jsonPath("$.songs[?(@.name == 'New Album Song')]").exists());
    }

    @Test
    void testRemoveSongFromAlbum() throws Exception {
        mockMvc.perform(get("/api/albums/{id}", testAlbum.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.songs", hasSize(1)))
                .andExpect(jsonPath("$.songs[0].id", is(testSong.getId().intValue())));

        mockMvc.perform(delete("/api/albums/{albumId}/songs/{songId}", testAlbum.getId(), testSong.getId()))
                .andExpect(status().isNoContent());

        mockMvc.perform(get("/api/albums/{id}", testAlbum.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.songs", hasSize(0)));

        mockMvc.perform(get("/api/songs/{id}", testSong.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(testSong.getId().intValue())))
                .andExpect(jsonPath("$.albumId").isEmpty());
    }

    @Test
    void testAddSongToNonExistentAlbum() throws Exception {
        SongDto songDto = new SongDto();
        songDto.setName("Test Song");
        songDto.setBandId(1L);
        songDto.setDuration(180);

        mockMvc.perform(post("/api/albums/{albumId}/songs", 999L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(songDto)))
                .andExpect(status().isNotFound());
    }

    @Test
    void testRemoveSongThatIsNotInAlbum() throws Exception {
        Song standaloneSong = new Song();
        standaloneSong.setName("Standalone Song");
        standaloneSong.setBandId(1L);
        standaloneSong.setDuration(180);
        standaloneSong = songRepository.save(standaloneSong);

        mockMvc.perform(delete("/api/albums/{albumId}/songs/{songId}", testAlbum.getId(), standaloneSong.getId()))
                .andExpect(status().isBadRequest());
    }
}

