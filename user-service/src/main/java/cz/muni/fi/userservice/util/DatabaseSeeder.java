package cz.muni.fi.userservice.util;

import cz.muni.fi.shared.enm.Role;
import cz.muni.fi.userservice.model.Artist;
import cz.muni.fi.userservice.model.Manager;
import cz.muni.fi.userservice.repository.ArtistRepository;
import cz.muni.fi.userservice.repository.ManagerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class DatabaseSeeder implements CommandLineRunner {

    private final ArtistRepository artistRepository;
    private final ManagerRepository managerRepository;

    private final Environment environment;


    @Override
    public void run(String... args) {
        boolean shouldSeed = Boolean.parseBoolean(environment.getProperty("app.db.seed", "false"));
        boolean shouldClear = Boolean.parseBoolean(environment.getProperty("app.db.clear", "false"));

        if (shouldClear) {
            artistRepository.deleteAll();
            managerRepository.deleteAll();
            System.out.println("Database cleared.");
        }

        if (shouldSeed) {
            Artist tameImpala = new Artist();
            tameImpala.setRole(Role.ARTIST);
            tameImpala.setUsername("tameimpala");
            tameImpala.setPassword("password1");
            tameImpala.setEmail("tameimpala@mail.com");
            tameImpala.setFirstName("Tame");
            tameImpala.setLastName("Impala");
            tameImpala.setStageName("tame impala");
            tameImpala.setBio("An Australian psychedelic rock band led by Kevin Parker.");
            tameImpala.setSkills("every instrument known to a humankind");

            Artist jpegMafia = new Artist();
            jpegMafia.setRole(Role.ARTIST);
            jpegMafia.setUsername("jpegmafia");
            jpegMafia.setPassword("password2");
            jpegMafia.setEmail("jpegmafia@mail.com");
            jpegMafia.setFirstName("JPEG");
            jpegMafia.setLastName("MAFIA");
            jpegMafia.setStageName("JPEGMAFIA");
            jpegMafia.setBio("An American rapper and producer known for his experimental style.");
            jpegMafia.setSkills("rapping, producing, and mixing");

            Artist fredAgain = new Artist();
            fredAgain.setRole(Role.ARTIST);
            fredAgain.setUsername("fredagain");
            fredAgain.setPassword("password3");
            fredAgain.setEmail("fredagain@mail.com");
            fredAgain.setFirstName("Fred");
            fredAgain.setLastName("Again");
            fredAgain.setStageName("Fred Again");
            fredAgain.setBio("A British musician and producer known for his innovative approach to music.");
            fredAgain.setSkills("music production, DJing, and songwriting");


            Manager manager1 = new Manager();
            manager1.setRole(Role.MANAGER);
            manager1.setUsername("manager1");
            manager1.setPassword("password1");
            manager1.setEmail("manager1@mail.com");
            manager1.setFirstName("Manager");
            manager1.setLastName("One");
            manager1.setCompanyName("Manager Company 1");

            Manager manager2 = new Manager();
            manager2.setRole(Role.MANAGER);
            manager2.setUsername("manager2");
            manager2.setPassword("password2");
            manager2.setEmail("manager2@mail.com");
            manager2.setFirstName("Manager");
            manager2.setLastName("Two");
            manager2.setCompanyName("Manager Company 2");

            Manager manager3 = new Manager();
            manager3.setRole(Role.MANAGER);
            manager3.setUsername("manager3");
            manager3.setPassword("password3");
            manager3.setEmail("manager3@mail.com");
            manager3.setFirstName("Manager");
            manager3.setLastName("Three");
            manager3.setCompanyName("Manager Company 3");


            artistRepository.saveAll(List.of(tameImpala, jpegMafia, fredAgain));
            managerRepository.saveAll(List.of(manager1, manager2, manager3));
        }
    }
}
