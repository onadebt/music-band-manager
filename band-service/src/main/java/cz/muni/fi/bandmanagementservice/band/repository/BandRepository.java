package cz.muni.fi.bandmanagementservice.band.repository;


import cz.muni.fi.bandmanagementservice.band.model.Band;

import java.util.Collection;
import java.util.Optional;

/**
 * @author Tomáš MAREK
 */
public interface BandRepository {
    public Collection<Band> getAllBands();

    public Optional<Band> getBandById(Long id);

    public Band createBand(Band band);

    public Band updateBand(Band band);

    public void deleteBand(Band band);
}
