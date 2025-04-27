package cz.muni.fi.musiccatalogservice;

import cz.muni.fi.musiccatalogservice.dto.AlbumDTO;
import cz.muni.fi.musiccatalogservice.dto.SongDTO;
import cz.muni.fi.musiccatalogservice.model.Album;
import cz.muni.fi.musiccatalogservice.model.Song;

import java.time.LocalDateTime;

public class TestDataFactory {
    public static final Album TEST_ALBUM_1 = setUpTestAlbum1();
    public static final Album TEST_ALBUM_2 = setUpTestAlbum2();
    public static final AlbumDTO TEST_ALBUM_1_DTO = setUpTestAlbum1Dto();
    public static final AlbumDTO TEST_ALBUM_2_DTO = setUpTestAlbum2Dto();
    public static final Song TEST_SONG_1 = setUpTestSong1();
    public static final SongDTO TEST_SONG_1_DTO = setUpTestSong1Dto();
    public static final Song TEST_SONG_2 = setUpTestSong2();
    public static final SongDTO TEST_SONG_2_DTO = setUpTestSong2Dto();

    private static Album setUpTestAlbum1() {
        Album testAlbum = new Album();
        testAlbum.setId(1L);
        testAlbum.setBandId(1L);
        testAlbum.setTitle("Title");
        testAlbum.setReleaseDate(LocalDateTime.now());
        return testAlbum;
    }

    private static Album setUpTestAlbum2() {
        Album testAlbum = new Album();
        testAlbum.setId(2L);
        testAlbum.setBandId(2L);
        testAlbum.setTitle("Title2");
        testAlbum.setReleaseDate(LocalDateTime.now());
        return testAlbum;
    }

    private static AlbumDTO setUpTestAlbum2Dto() {
        AlbumDTO testAlbum = new AlbumDTO();
        testAlbum.setId(2L);
        testAlbum.setBandId(2L);
        testAlbum.setTitle("Title");
        testAlbum.setReleaseDate(LocalDateTime.now());
        return testAlbum;
    }

    private static AlbumDTO setUpTestAlbum1Dto() {
        AlbumDTO testAlbum = new AlbumDTO();
        testAlbum.setId(1L);
        testAlbum.setBandId(1L);
        testAlbum.setTitle("Title");
        testAlbum.setReleaseDate(LocalDateTime.now());
        return testAlbum;
    }

    private static Song setUpTestSong1() {
        Song song = new Song();
        song.setId(1L);
        song.setName("Test name");
        song.setDuration(42);
        return song;
    }

    private static SongDTO setUpTestSong1Dto() {
        SongDTO song = new SongDTO();
        song.setId(1L);
        song.setName("Test name");
        song.setDuration(42);
        return song;
    }

    private static Song setUpTestSong2() {
        Song song = new Song();
        song.setId(2L);
        song.setName("Test name 2");
        song.setDuration(14);
        return song;
    }

    private static SongDTO setUpTestSong2Dto() {
        SongDTO song = new SongDTO();
        song.setId(2L);
        song.setName("Test name 2");
        song.setDuration(14);
        return song;
    }

}

