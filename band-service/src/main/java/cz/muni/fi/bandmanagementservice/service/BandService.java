package cz.muni.fi.bandmanagementservice.service;

import cz.muni.fi.bandmanagementservice.exceptions.BandAlreadyExistsException;
import cz.muni.fi.bandmanagementservice.exceptions.BandNotFoundException;
import cz.muni.fi.bandmanagementservice.exceptions.MusicianAlreadyInBandException;
import cz.muni.fi.bandmanagementservice.exceptions.MusicianNotInBandException;
import cz.muni.fi.bandmanagementservice.model.Band;
import cz.muni.fi.bandmanagementservice.repository.BandRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * @author Tomáš MAREK
 */
@Transactional
@Service
public class BandService {
    private final BandRepository bandRepository;

    @Autowired
    public BandService(BandRepository bandRepository) {
        this.bandRepository = bandRepository;
    }

    public Band createBand(String name, String musicalStyle, Long managerId) {
        Optional<Band> sameNameBand = bandRepository.findByName(name);
        if (sameNameBand.isPresent()) {
            throw new BandAlreadyExistsException("Band with name \"" + name + "\" already exists");
        }
        Band newBand = new Band(null, name, musicalStyle, managerId);
        return bandRepository.save(newBand);
    }

    @Transactional(readOnly = true)
    public Band getBand(Long id) {
        Optional<Band> maybeBand = bandRepository.findById(id);
        if (maybeBand.isEmpty()) {
            throw new BandNotFoundException(id);
        }
        return maybeBand.get();
    }

    @Transactional(readOnly = true)
    public List<Band> getAllBands() {
        return bandRepository.findAll().stream().toList();
    }

    public Band updateBand(Long id, Band band) {
        Band bandToUpdate = getBand(id);

        bandToUpdate.setName(band.getName());
        bandToUpdate.setMusicalStyle(band.getMusicalStyle());
        bandToUpdate.setLogoUrl(band.getLogoUrl());
        bandToUpdate.setManagerId(band.getManagerId());
        bandToUpdate.setMembers(band.getMembers());

        return bandRepository.save(bandToUpdate);
    }

    public Band removeMember(Long bandId, Long memberId) {
        Band band = getBand(bandId);
        if (!band.getMembers().contains(memberId)) {
            throw new MusicianNotInBandException(bandId, memberId);
        }
        band.removeMember(memberId);

        return bandRepository.save(band);
    }

    public Band addMember(Long bandId, Long memberId) {
        Band band = getBand(bandId);
        if (band.getMembers().contains(memberId)) {
            throw new MusicianAlreadyInBandException(bandId, memberId);
        }
        band.addMember(memberId);

        return bandRepository.save(band);
    }
}
