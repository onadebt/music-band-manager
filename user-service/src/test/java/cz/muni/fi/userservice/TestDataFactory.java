package cz.muni.fi.userservice;

import cz.muni.fi.userservice.dto.ArtistDTO;
import cz.muni.fi.userservice.dto.ManagerDTO;
import cz.muni.fi.userservice.model.Artist;
import cz.muni.fi.userservice.model.Manager;
import cz.muni.fi.userservice.model.Role;

import java.util.Set;

/**
 * @author Tomáš MAREK
 */
public class TestDataFactory {
    public static final Artist TEST_ARTIST_1 = setUpTestArtist1();
    public static final Artist TEST_ARTIST_2 = setUpTestArtist2();
    public static final ArtistDTO TEST_ARTIST_1_DTO = setUpTestArtist1Dto();
    public static final ArtistDTO TEST_ARTIST_2_DTO = setUpTestArtist2Dto();
    public static final Manager TEST_MANAGER_1 = setUpTestManager1();
    public static final Manager TEST_MANAGER_2 = setUpTestManager2();
    public static final ManagerDTO TEST_MANAGER_1_DTO = setUpTestManager1Dto();
    public static final ManagerDTO TEST_MANAGER_2_DTO = setUpTestManager2Dto();


    private static Manager setUpTestManager1() {
        Manager testManager = new Manager();
        testManager.setManagedBandIds(Set.of(1L, 2L, 3L));
        testManager.setCompanyName("Test Music Management Company");
        testManager.setId(3L);
        testManager.setUsername("xdoe7");
        testManager.setEmail("john_doe@email.com");
        testManager.setFirstName("John");
        testManager.setLastName("Doe");
        testManager.setRole(Role.MANAGER);
        testManager.setPassword("password123");
        return testManager;
    }

    private static ManagerDTO setUpTestManager1Dto(){
        ManagerDTO testManager = new ManagerDTO();
        testManager.setManagedBandIds(Set.of(1L, 2L, 3L));
        testManager.setCompanyName("Test Music Management Company");
        testManager.setId(3L);
        testManager.setUsername("xdoe7");
        testManager.setEmail("john_doe@email.com");
        testManager.setFirstName("John");
        testManager.setLastName("Doe");
        testManager.setRole(Role.MANAGER);
        testManager.setPassword("password123");
        return testManager;
    }

    private static Manager setUpTestManager2() {
        Manager testManager = new Manager();
        testManager.setManagedBandIds(Set.of(2L, 3L, 4L));
        testManager.setCompanyName("Managers of Music");
        testManager.setId(4L);
        testManager.setUsername("mustermann123");
        testManager.setEmail("mustermann_m@email.de");
        testManager.setFirstName("Max");
        testManager.setLastName("Mustermann");
        testManager.setRole(Role.MANAGER);
        testManager.setPassword("MustermannRules!");
        return testManager;
    }

    private static ManagerDTO setUpTestManager2Dto() {
        ManagerDTO testManager = new ManagerDTO();
        testManager.setManagedBandIds(Set.of(2L, 3L, 4L));
        testManager.setCompanyName("Managers of Music");
        testManager.setId(4L);
        testManager.setUsername("mustermann123");
        testManager.setEmail("mustermann_m@email.de");
        testManager.setFirstName("Max");
        testManager.setLastName("Mustermann");
        testManager.setRole(Role.MANAGER);
        testManager.setPassword("MustermannRules!");
        return testManager;
    }

    private static Artist setUpTestArtist1() {
        Artist testArtist = new Artist("Till Lindemann", "Born in 1963, Leipzig", "Singer", Set.of(1L, 2L, 3L));
        testArtist.setId(1L);
        testArtist.setUsername("xlindemann");
        testArtist.setEmail("lindemann_till@email.com");
        testArtist.setFirstName("Till");
        testArtist.setLastName("Lindemann");
        testArtist.setRole(Role.ARTIST);
        testArtist.setPassword("Password in clean? Really guys?");
        return testArtist;
    }

    private static ArtistDTO setUpTestArtist1Dto() {
        ArtistDTO artistDTO = new ArtistDTO();
        artistDTO.setId(1L);
        artistDTO.setUsername("xlindemann");
        artistDTO.setEmail("lindemann_till@email.com");
        artistDTO.setFirstName("Till");
        artistDTO.setLastName("Lindemann");
        artistDTO.setRole(Role.ARTIST);
        artistDTO.setPassword("Password in clean? Really guys?");
        artistDTO.setStageName("Till Lindemann");
        artistDTO.setBio("Born in 1963, Leipzig");
        artistDTO.setSkills("Singer");
        artistDTO.setBandIds(Set.of(1L, 2L, 3L));
        return artistDTO;
    }

    private static Artist setUpTestArtist2() {
        Artist testArtis = new Artist("Joakim Brodén", "Born in 1980, Falun", "Singer, Keyboard", Set.of(2L, 4L));
        testArtis.setId(2L);
        testArtis.setUsername("brodenj");
        testArtis.setEmail("joakim_broden@example.com");
        testArtis.setFirstName("Joakim");
        testArtis.setLastName("Brodén");
        testArtis.setRole(Role.ARTIST);
        testArtis.setPassword("Very strong password");
        return testArtis;
    }

    private static ArtistDTO setUpTestArtist2Dto() {
        ArtistDTO artistDTO = new ArtistDTO();
        artistDTO.setId(2L);
        artistDTO.setUsername("brodenj");
        artistDTO.setEmail("joakim_broden@example.com");
        artistDTO.setFirstName("Joakim");
        artistDTO.setLastName("Brodén");
        artistDTO.setRole(Role.ARTIST);
        artistDTO.setPassword("Very strong password");
        artistDTO.setStageName("Joakim Brodén");
        artistDTO.setBio("Born in 1980, Falun");
        artistDTO.setSkills("Singer, Keyboard");
        artistDTO.setBandIds(Set.of(2L, 4L));
        return artistDTO;
    }
}
