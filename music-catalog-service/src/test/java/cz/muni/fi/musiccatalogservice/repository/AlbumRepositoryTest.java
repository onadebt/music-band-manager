package cz.muni.fi.musiccatalogservice.repository;

import cz.muni.fi.musiccatalogservice.TestDataFactory;
import cz.muni.fi.musiccatalogservice.model.Album;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
public class AlbumRepositoryTest {

    @Autowired
    private AlbumRepository albumRepository;

    @BeforeEach
    void setUp() {
        albumRepository.deleteAll();
    }

    @Test
    void findByBandId_validBandId_returnsAlbums() {
        // Arrange
        Album album1 = TestDataFactory.setUpTestAlbum1();
        album1.setId(null);

        albumRepository.save(album1);

        // Act
        var albumsByBand = albumRepository.findByBandId(album1.getBandId());

        // Assert
        assertEquals(1, albumsByBand.size());
    }

    @Test
    void findByBandId_invalidBandId_returnsEmptyList() {
        // Arrange
        Long invalidBandId = -999L;

        // Act
        var albumsByBand = albumRepository.findByBandId(invalidBandId);

        // Assert
        assertEquals(0, albumsByBand.size());
    }
}
