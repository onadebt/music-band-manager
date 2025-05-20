package cz.muni.fi.bandmanagementservice.facade;

import cz.muni.fi.bandmanagementservice.mappers.BandInfoUpdateMapper;
import cz.muni.fi.bandmanagementservice.model.Band;
import cz.muni.fi.bandmanagementservice.model.BandInfoUpdate;
import cz.muni.fi.bandmanagementservice.mappers.BandMapper;
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
    private final BandInfoUpdateMapper bandInfoUpdateMapper;

    @Autowired
    public BandFacade(BandService bandService, BandMapper bandMapper, BandInfoUpdateMapper bandInfoUpdateMapper) {
        this.bandService = bandService;
        this.bandMapper = bandMapper;
        this.bandInfoUpdateMapper = bandInfoUpdateMapper;
    }

    public BandDto createBand(String name, String musicalStyle, Long managerId) {
        Band band = bandService.createBand(name, musicalStyle, managerId);
        return bandMapper.toDto(band);
    }

    @Transactional(readOnly = true)
    public BandDto getBand(Long id) {
        return bandMapper.toDto(bandService.getBand(id));
    }

    public BandDto updateBand(BandInfoUpdateDto request){
        BandInfoUpdate bandInfoUpdate = bandInfoUpdateMapper.toEntity(request);
        return bandMapper.toDto(bandService.updateBand(bandInfoUpdate));
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
