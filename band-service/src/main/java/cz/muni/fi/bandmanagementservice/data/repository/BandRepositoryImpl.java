package cz.muni.fi.bandmanagementservice.data.repository;

import cz.muni.fi.bandmanagementservice.data.model.Band;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * @author Tomáš MAREK
 */
public class BandRepositoryImpl implements BandRepository {
    private Long nextId = 1L;
    private final Map<Long, Band> bands = new HashMap<>();

    @Override
    public Collection<Band> getAllBands() {
        return bands.values();
    }

    @Override
    public Optional<Band> getBandById(Long id) {
        if (bands.containsKey(id)) {
            return Optional.of(bands.get(id));
        }
        return Optional.empty();
    }

    @Override
    public void createBand(Band band) {
        band.setId(nextId++);
        bands.put(band.getId(), band);
    }

    @Override
    public void updateBand(Band band) {
        bands.put(band.getId(), band);
    }
}
