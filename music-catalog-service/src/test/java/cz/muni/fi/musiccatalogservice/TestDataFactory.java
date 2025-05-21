package cz.muni.fi.musiccatalogservice;

import cz.muni.fi.musiccatalogservice.dto.AlbumDto;
import cz.muni.fi.musiccatalogservice.dto.SongDto;
import cz.muni.fi.musiccatalogservice.model.Album;
import cz.muni.fi.musiccatalogservice.model.Song;

import java.time.LocalDateTime;

public class TestDataFactory {
    public static final Album TEST_ALBUM_1 = setUpTestAlbum1();
    public static final Album TEST_ALBUM_2 = setUpTestAlbum2();
    public static final AlbumDto TEST_ALBUM_1_DTO = setUpTestAlbum1Dto();
    public static final AlbumDto TEST_ALBUM_2_DTO = setUpTestAlbum2Dto();
    public static final Song TEST_SONG_1 = setUpTestSong1();
    public static final SongDto TEST_SONG_1_DTO = setUpTestSong1Dto();
    public static final Song TEST_SONG_2 = setUpTestSong2();
    public static final SongDto TEST_SONG_2_DTO = setUpTestSong2Dto();

    public static Album setUpTestAlbum1() {
        Album testAlbum = new Album();
        testAlbum.setId(1L);
        testAlbum.setBandId(1L);
        testAlbum.setTitle("Title");
        testAlbum.setReleaseDate(LocalDateTime.now());
        return testAlbum;
    }

    public static Album setUpTestAlbum2() {
        Album testAlbum = new Album();
        testAlbum.setId(2L);
        testAlbum.setBandId(2L);
        testAlbum.setTitle("Title2");
        testAlbum.setReleaseDate(LocalDateTime.now());
        return testAlbum;
    }

    public static AlbumDto setUpTestAlbum2Dto() {
        AlbumDto testAlbum = new AlbumDto();
        testAlbum.setId(2L);
        testAlbum.setBandId(2L);
        testAlbum.setTitle("Title");
        testAlbum.setReleaseDate(LocalDateTime.now());
        return testAlbum;
    }

    public static AlbumDto setUpTestAlbum1Dto() {
        AlbumDto testAlbum = new AlbumDto();
        testAlbum.setId(1L);
        testAlbum.setBandId(1L);
        testAlbum.setTitle("Title");
        testAlbum.setReleaseDate(LocalDateTime.now());
        return testAlbum;
    }

    public static Song setUpTestSong1() {
        Song song = new Song();
        song.setId(1L);
        song.setName("Test name");
        song.setDuration(42);
        return song;
    }

    public static SongDto setUpTestSong1Dto() {
        SongDto song = new SongDto();
        song.setId(1L);
        song.setName("Test name");
        song.setDuration(42);
        return song;
    }

    public static Song setUpTestSong2() {
        Song song = new Song();
        song.setId(2L);
        song.setName("Test name 2");
        song.setDuration(14);
        return song;
    }

    public static SongDto setUpTestSong2Dto() {
        SongDto song = new SongDto();
        song.setId(2L);
        song.setName("Test name 2");
        song.setDuration(14);
        return song;
    }

}

