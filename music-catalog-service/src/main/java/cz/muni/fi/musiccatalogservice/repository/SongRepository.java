package cz.muni.fi.musiccatalogservice.repository;

import cz.muni.fi.musiccatalogservice.model.Song;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface SongRepository extends JpaRepository<Song, Long> {
    List<Song> findByAlbumId(Long albumId);
    List<Song> findByBandId(Long bandId);
}