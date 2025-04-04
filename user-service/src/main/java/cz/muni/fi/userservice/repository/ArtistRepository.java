package cz.muni.fi.userservice.repository;

import cz.muni.fi.userservice.model.Artist;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface ArtistRepository extends JpaRepository<Artist, Long> {
    Optional<Artist> findByUsername(String username);

//    List<Artist> findByBandIds (Set<Long> bandIds);

    @Query("SELECT DISTINCT a FROM Artist a JOIN a.bandIds b WHERE b IN :bandIds")
    List<Artist> findByBandIds(@Param("bandIds") Set<Long> bandIds);
}
