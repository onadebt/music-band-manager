package cz.muni.fi.bandmanagementservice.band.facade;

import cz.muni.fi.bandmanagementservice.band.model.BandInfoUpdate;
import cz.muni.fi.bandmanagementservice.band.mappers.BandMapper;
import cz.muni.fi.bandmanagementservice.band.service.BandService;
import cz.muni.fi.bandmanagementservice.band.dto.BandDto;
import cz.muni.fi.bandmanagementservice.band.dto.BandInfoUpdateRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

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
        return BandMapper.mapToDto(bandService.createBand(name, musicalStyle, managerId));
    }

    public BandDto getBand(Long id) {
        return BandMapper.mapToDto(bandService.getBand(id));
    }

    public BandDto updateBand(BandInfoUpdateRequest request){
        BandInfoUpdate bandInfoUpdate = BandMapper.mapFromInfoUpdateRequest(request);
        return BandMapper.mapToDto(bandService.updateBand(bandInfoUpdate));
    }

    public List<BandDto> getAllBands(){
        return bandService.getAllBands().stream().map(BandMapper::mapToDto).collect(Collectors.toList());
    }

    public BandDto removeMember(Long bandId, Long memberId) {
        return BandMapper.mapToDto(bandService.removeMember(bandId, memberId));
    }
}
