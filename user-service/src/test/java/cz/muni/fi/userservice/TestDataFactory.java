package cz.muni.fi.userservice;

import cz.muni.fi.userservice.model.Artist;

import java.util.Set;

/**
 * @author Tomáš MAREK
 */
public class TestDataFactory {
    public static final Artist TEST_ARTIST_1 = new Artist("Till Lindemann", "Born in 1963, Leipzig", "Singer", Set.of(1L, 2L, 3L));
    public static final Artist TEST_ARTIST_2 = new Artist("Joakim Brodén", "Born in 1980, Falun", "Singer, Keyboard", Set.of(2L, 4L));
    public static final Long TEST_ARTIST_1_ID = 1L;

}
