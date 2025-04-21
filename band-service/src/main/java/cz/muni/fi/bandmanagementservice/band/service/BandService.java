package cz.muni.fi.bandmanagementservice.band.service;

import cz.muni.fi.bandmanagementservice.band.model.BandInfoUpdate;
import cz.muni.fi.bandmanagementservice.band.exceptions.InvalidOperationException;
import cz.muni.fi.bandmanagementservice.band.model.Band;
import cz.muni.fi.bandmanagementservice.band.repository.BandRepository;
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
        return bandRepository.save(newBand);
    }

    public Band getBand(Long id){
        Optional<Band> maybeBand = bandRepository.findById(id);
        if (maybeBand.isEmpty()){
            throw new ResourceNotFoundException("Band with id %d does not exist".formatted(id));
        }
        return maybeBand.get();
    }

    public List<Band> getAllBands(){
        return bandRepository.findAll().stream().toList();
    }

    public Band updateBand(BandInfoUpdate bandInfoUpdate){
        Band updatedBand = new Band(bandInfoUpdate.id(), bandInfoUpdate
                .name(), bandInfoUpdate.musicalStyle(), bandInfoUpdate.managerId(),
                bandInfoUpdate.logoUrl());
        Optional<Band> updated = bandRepository.findById(bandInfoUpdate.id());
        if (updated.isEmpty()){
            throw new ResourceNotFoundException("Band with id %d does not exists".formatted(updatedBand.getId()));
        }
        updatedBand.setMembers(updated.get().getMembers());
        return bandRepository.save(updatedBand);
    }

    public Band removeMember(Long bandId, Long memberId){
        Band band = getBand(bandId);
        if (!band.getMembers().contains(memberId)){
            throw new InvalidOperationException("Member with id %d is not part of band %d".formatted(memberId, bandId));
        }
        band.removeMember(memberId);
        return bandRepository.save(band);
    }


}
