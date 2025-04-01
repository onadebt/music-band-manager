package cz.muni.fi.bandmanagementservice.data.repository;


import cz.muni.fi.bandmanagementservice.data.model.Band;

import java.util.Collection;
import java.util.Optional;

/**
 * @author Tomáš MAREK
 */
public interface BandRepository {
    public Collection<Band> getAllBands();

    public Optional<Band> getBandById(Long id);

    public void createBand(Band band);

    public void updateBand(Band band);
}
