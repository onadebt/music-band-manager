package cz.muni.fi.musiccatalogservice.repository;

import cz.muni.fi.musiccatalogservice.TestDataFactory;
import cz.muni.fi.musiccatalogservice.model.Album;
import cz.muni.fi.musiccatalogservice.model.Song;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
class SongRepositoryTest {

    @Autowired
    private AlbumRepository albumRepository;

    @Autowired
    private SongRepository songRepository;

    @Test
    void findByAlbumId_validAlbumId_returnsSongs() {
        // Arrange
        Album album = TestDataFactory.setUpTestAlbum1();
        Song song1 = TestDataFactory.setUpTestSong1();
        Song song2 = TestDataFactory.setUpTestSong2();
        album.setId(null);
        song1.setId(null);
        song2.setId(null);

        album.addSong(song1);
        album.addSong(song2);

        albumRepository.save(album);

        // Act
        var songsByAlbum = songRepository.findByAlbumId(album.getId());

        // Assert
        assertEquals(2, songsByAlbum.size());
        assertTrue(songsByAlbum.stream().anyMatch(s -> s.getName().equals(song1.getName())));
    }

    @Test
    void findByAlbumId_invalidAlbumId_returnsEmptyList() {
        // Arrange
        Long invalidAlbumId = -999L;

        // Act
        var songsByAlbum = songRepository.findByAlbumId(invalidAlbumId);

        // Assert
        assertTrue(songsByAlbum.isEmpty());
    }

    @Test
    void findByBandId_validBandId_returnsSongs() {
        // Arrange
        Song song = TestDataFactory.setUpTestSong1();
        song.setId(null);

        songRepository.save(song);

        // Act
        var songsByBand = songRepository.findByBandId(song.getBandId());

        // Assert
        assertEquals(1, songsByBand.size());
        assertTrue(songsByBand.stream().anyMatch(s -> s.getName().equals(song.getName())));
    }

    @Test
    void findByBandId_invalidBandId_returnsEmptyList() {
        // Arrange
        Long invalidBandId = -999L;

        // Act
        var songsByBand = songRepository.findByBandId(invalidBandId);

        // Assert
        assertTrue(songsByBand.isEmpty());
    }
}

