package cz.muni.fi.musiccatalogservice.repository;

import cz.muni.fi.musiccatalogservice.model.Album;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface AlbumRepository extends JpaRepository<Album, Long> {
    List<Album> findByBandId(Long bandId);
}