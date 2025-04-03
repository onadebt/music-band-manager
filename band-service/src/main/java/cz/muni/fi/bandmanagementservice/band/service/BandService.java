package cz.muni.fi.bandmanagementservice.band.service;

import cz.muni.fi.bandmanagementservice.band.data.model.BandInfoUpdate;
import cz.muni.fi.bandmanagementservice.band.exceptions.InvalidOperationException;
import cz.muni.fi.bandmanagementservice.band.data.model.Band;
import cz.muni.fi.bandmanagementservice.band.data.repository.BandRepository;
import cz.muni.fi.bandmanagementservice.band.exceptions.ResourceNotFoundException;
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
        return bandRepository.createBand(newBand);
    }

    public Band getBand(Long id){
        Optional<Band> maybeBand = bandRepository.getBandById(id);
        if (maybeBand.isEmpty()){
            throw new ResourceNotFoundException("Band with id %d does not exist".formatted(id));
        }
        return maybeBand.get();
    }

    public List<Band> getAllBands(){
        return bandRepository.getAllBands().stream().toList();
    }

    public Band updateBand(BandInfoUpdate bandInfoUpdate){
        Band updatedBand = new Band(bandInfoUpdate.id(), bandInfoUpdate
                .name(), bandInfoUpdate.musicalStyle(), bandInfoUpdate.managerId(),
                bandInfoUpdate.logoUrl());;
        if (bandRepository.getBandById(updatedBand.getId()).isEmpty()){
            throw new ResourceNotFoundException("Band with id %d does not exists".formatted(updatedBand.getId()));
        }
        Band originalBand = bandRepository.getBandById(updatedBand.getId()).get();
        updatedBand.setMembers(originalBand.getMembers());
        return bandRepository.updateBand(updatedBand);
    }


}
