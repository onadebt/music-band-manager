package cz.muni.fi.bandmanagementservice.facade;

import cz.muni.fi.bandmanagementservice.model.Band;
import cz.muni.fi.bandmanagementservice.mapper.BandMapper;
import cz.muni.fi.bandmanagementservice.service.BandService;
import cz.muni.fi.bandmanagementservice.dto.BandDto;
import cz.muni.fi.bandmanagementservice.dto.BandInfoUpdateDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Tomáš MAREK
 */
@Service
@Transactional
public class BandFacade {
    private final BandService bandService;
    private final BandMapper bandMapper;


    @Autowired
    public BandFacade(BandService bandService, BandMapper bandMapper) {
        this.bandService = bandService;
        this.bandMapper = bandMapper;
    }

    public BandDto createBand(String name, String musicalStyle, Long managerId) {
        Band band = bandService.createBand(name, musicalStyle, managerId);
        return bandMapper.toDto(band);
    }

    @Transactional(readOnly = true)
    public BandDto getBand(Long id) {
        return bandMapper.toDto(bandService.getBand(id));
    }

    public BandDto updateBand(Long id, BandInfoUpdateDto request){
        Band band = bandMapper.toEntity(request);
        return bandMapper.toDto(bandService.updateBand(id, band));
    }

    @Transactional(readOnly = true)
    public List<BandDto> getAllBands(){
        return bandService.getAllBands().stream().map(bandMapper::toDto).collect(Collectors.toList());
    }

    public BandDto removeMember(Long bandId, Long memberId) {
        return bandMapper.toDto(bandService.removeMember(bandId, memberId));
    }

    public BandDto addMember(Long bandId, Long memberId) {
        return bandMapper.toDto(bandService.addMember(bandId, memberId));
    }
}
