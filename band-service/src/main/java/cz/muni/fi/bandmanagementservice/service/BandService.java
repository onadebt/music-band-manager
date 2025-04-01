package cz.muni.fi.bandmanagementservice.service;

import cz.muni.fi.bandmanagementservice.data.exceptions.InvalidBandException;
import cz.muni.fi.bandmanagementservice.data.model.Band;
import cz.muni.fi.bandmanagementservice.data.repository.BandRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * @author Tomáš MAREK
 */
@Service
public class BandService {
    private final BandRepository bandRepository;

    @Autowired
    public BandService(BandRepository bandRepository) {
        this.bandRepository = bandRepository;
    }

    public Band createBand(String name, String musicalStyle, Long managerId){
        Band newBand = new Band(null, name, musicalStyle, managerId);
        bandRepository.createBand(newBand);
        return newBand;
    }

    public Band getBand(Long id){
        Optional<Band> maybeBand = bandRepository.getBandById(id);
        if (maybeBand.isEmpty()){
            throw new InvalidBandException("Band with id %d does not exist".formatted(id));
        }
        return maybeBand.get();
    }

    public List<Band> getAllBands(){
        return bandRepository.getAllBands().stream().toList();
    }

    public void updateBand(Band band){
        if (bandRepository.getBandById(band.getId()).isEmpty()){
            throw new InvalidBandException("Band with id %d does not exists".formatted(band.getId()));
        }
        bandRepository.updateBand(band);
    }


}
