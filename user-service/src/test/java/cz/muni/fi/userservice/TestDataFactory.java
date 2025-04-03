package cz.muni.fi.userservice;

import cz.muni.fi.userservice.model.Artist;
import cz.muni.fi.userservice.model.Role;

import java.util.Set;

/**
 * @author Tomáš MAREK
 */
public class TestDataFactory {
    public static final Artist TEST_ARTIST_1 = setUpTestArtist1();
    public static final Artist TEST_ARTIST_2 = setUpTestArtist2();

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
}
