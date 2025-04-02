package cz.muni.fi.bandmanagementservice.facade;

import cz.muni.fi.bandmanagementservice.data.model.Band;
import cz.muni.fi.bandmanagementservice.data.model.BandInfoUpdate;
import cz.muni.fi.bandmanagementservice.mappers.BandMapper;
import cz.muni.fi.bandmanagementservice.service.BandService;
import cz.muni.fi.bandmanagementservice.band.model.BandDto;
import cz.muni.fi.bandmanagementservice.band.model.BandInfoUpdateRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author Tomáš MAREK
 */
@Component
public class BandFacade {
    private final BandService bandService;

    @Autowired
    public BandFacade(BandService bandService) {
        this.bandService = bandService;
    }

    public BandDto createBand(String name, String musicalStyle, Long managerId) {
        Band newBand = bandService.createBand(name, musicalStyle, managerId);
        return BandMapper.mapToDto(newBand);
    }

    public BandDto getBand(Long id) {
        return BandMapper.mapToDto(bandService.getBand(id));
    }

    public BandDto updateBand(Long id, BandInfoUpdateRequest request){
        BandInfoUpdate bandInfoUpdate = BandMapper.mapFromInfoUpdateRequest(request);
        bandService.updateBand(bandInfoUpdate);
        return BandMapper.mapToDto(bandService.getBand(id));
    }
}
