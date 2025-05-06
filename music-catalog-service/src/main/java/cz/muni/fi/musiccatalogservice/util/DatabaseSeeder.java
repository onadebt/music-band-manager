package cz.muni.fi.musiccatalogservice.util;

import cz.muni.fi.musiccatalogservice.model.Album;
import cz.muni.fi.musiccatalogservice.model.Song;
import cz.muni.fi.musiccatalogservice.repository.AlbumRepository;
import cz.muni.fi.musiccatalogservice.repository.SongRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
@RequiredArgsConstructor
public class DatabaseSeeder implements CommandLineRunner {

    private final SongRepository songRepository;
    private final AlbumRepository albumRepository;

    private final Environment environment;


    @Override
    public void run(String... args) {
        boolean shouldSeed = Boolean.parseBoolean(environment.getProperty("app.db.seed", "false"));
        boolean shouldClear = Boolean.parseBoolean(environment.getProperty("app.db.clear", "false"));

        if (shouldClear) {
            albumRepository.deleteAll();
            songRepository.deleteAll();
            System.out.println("Database cleared.");
        }

        if (shouldSeed) {
            Song song1 = new Song();
            song1.setName("places to be");
            song1.setDuration(4);

            Song song2 = new Song();
            song2.setName("the less I know the better");
            song2.setDuration(3);

            Song song3 = new Song();
            song3.setName("LOOP IT AND LEAVE IT");
            song3.setDuration(4);

            Song song4 = new Song();
            song4.setName("adore u");
            song4.setDuration(3);

            Song song5 = new Song();
            song5.setName("Same person old mistakes");
            song5.setDuration(4);

            Song song6 = new Song();
            song6.setName("I recovered from this");
            song6.setDuration(3);


            Album album1 = new Album();
            album1.setTitle("ten days");
            album1.setReleaseDate(LocalDateTime.now());
            album1.setBandId(1L);

            Album album2 = new Album();
            album2.setTitle("Currents");
            album2.setReleaseDate(LocalDateTime.now());
            album2.setBandId(2L);

            Album album3 = new Album();
            album3.setTitle("I LAY MY LIFE DOWN FOR YOU");
            album3.setReleaseDate(LocalDateTime.now());
            album3.setBandId(3L);

            songRepository.saveAll(List.of(song1, song2, song3, song4, song5, song6));
            albumRepository.saveAll(List.of(album1, album2, album3));
        }
    }
}
