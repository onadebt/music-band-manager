package cz.muni.fi.musiccatalogservice.controller;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import cz.muni.fi.musiccatalogservice.dto.SongDTO;
import cz.muni.fi.musiccatalogservice.model.Album;
import cz.muni.fi.musiccatalogservice.model.Song;
import cz.muni.fi.musiccatalogservice.repository.AlbumRepository;
import cz.muni.fi.musiccatalogservice.repository.SongRepository;
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
import java.time.LocalDateTime;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
public class SongRestControllerIT {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private SongRepository songRepository;

    @Autowired
    private AlbumRepository albumRepository;

    private ObjectMapper objectMapper;
    private Song testSong;
    private Album testAlbum;

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
        testAlbum.setReleaseDate(LocalDateTime.now().plusDays(30));
        testAlbum = albumRepository.save(testAlbum);

        // Create test song
        testSong = new Song();
        testSong.setName("Test Song");
        testSong.setBandId(1L);
        testSong.setAlbum(testAlbum);
        testSong = songRepository.save(testSong);
    }


    @Test
    void testGetAllSongs() throws Exception {
        mockMvc.perform(get("/api/songs"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].name", is("Test Song")))
                .andExpect(jsonPath("$[0].bandId", is(1)));
                //.andExpect(jsonPath("$[0].album.title", is("Test Album")));
    }


    @Test
    public void testGetSongById() throws Exception {
        mockMvc.perform(get("/api/songs/{id}", testSong.getId()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(testSong.getId().intValue())))
                .andExpect(jsonPath("$.name", is("Test Song")))
                .andExpect(jsonPath("$.bandId", is(1)));
                //.andExpect(jsonPath("$.album.title", is("Test Album")));

    }


    @Test
    public void testGetSongsByBand() throws Exception {
        mockMvc.perform(get("/api/songs/band/{bandId}", 1L))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].bandId", is(1)));
    }



    @Test
    public void testCreateSong() throws Exception {
        SongDTO songDTO = new SongDTO();
        songDTO.setName("New Song");
        songDTO.setBandId(2L);
        songDTO.setDuration(5);

        mockMvc.perform(post("/api/songs")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(songDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name", is("New Song")))
                .andExpect(jsonPath("$.bandId", is(2)));

    }


    @Test
    void testCreateInvalidSong() throws Exception {
        SongDTO songDTO = new SongDTO();
        // Empty name and null bandId

        mockMvc.perform(post("/api/songs")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(songDTO)))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testUpdateSong() throws Exception {
        SongDTO songDTO = new SongDTO();
        songDTO.setName("Updated Song Name");
        songDTO.setBandId(testSong.getBandId());
        songDTO.setDuration(5);

        String songJson = objectMapper.writeValueAsString(songDTO);

        mockMvc.perform(put("/api/songs/{id}", testSong.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(songJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is("Updated Song Name")));
    }


    @Test
    public void testDeleteSong() throws Exception {
        mockMvc.perform(delete("/api/songs/{id}", testSong.getId()))
                .andExpect(status().isNoContent());

        // Verify the song is deleted
        mockMvc.perform(get("/api/songs/{id}", testSong.getId()))
                .andExpect(status().isNotFound());

    }


    @Test
    public void testCreateSongWithInvalidData() throws Exception {
        SongDTO songDTO = new SongDTO();
        songDTO.setName("");
        songDTO.setBandId(null);

        String songJson = objectMapper.writeValueAsString(songDTO);

        mockMvc.perform(post("/api/songs")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(songJson))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testGetNonExistingSong() throws Exception {
        mockMvc.perform(get("/api/songs/{id}", 999L))
                .andExpect(status().isNotFound());
    }
}

