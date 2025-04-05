package cz.muni.fi.bandmanagementservice.band.data.repository;

import cz.muni.fi.bandmanagementservice.band.exceptions.DataStorageException;
import cz.muni.fi.bandmanagementservice.band.data.model.Band;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * @author Tomáš MAREK
 */
@Component
public class InMemoryBandRepository implements BandRepository {
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
    public Band createBand(Band band) {
        if (band.getId() != null) {
            throw new DataStorageException("Cannot create band which already has id");
        }
        band.setId(nextId++);
        bands.put(band.getId(), band);
        return band;
    }

    @Override
    public Band updateBand(Band band) {
        verifyBandExists(band);
        bands.put(band.getId(), band);
        return band;
    }

    @Override
    public void deleteBand(Band band) {
        verifyBandExists(band);
        bands.remove(band.getId());
    }

    private void verifyBandExists(Band band){
        if (!bands.containsKey(band.getId())) {
            throw new DataStorageException("Updated band with id %d not found!".formatted(band.getId()));
        }
    }
}
