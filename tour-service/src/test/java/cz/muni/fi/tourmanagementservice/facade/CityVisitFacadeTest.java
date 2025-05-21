package cz.muni.fi.tourmanagementservice.facade;

import cz.muni.fi.tourmanagementservice.dto.CityVisitDto;
import cz.muni.fi.tourmanagementservice.facades.CityVisitFacade;
import cz.muni.fi.tourmanagementservice.mapper.CityVisitMapper;
import cz.muni.fi.tourmanagementservice.model.CityVisit;
import cz.muni.fi.tourmanagementservice.service.CityVisitService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

public class CityVisitFacadeTest {

    @Mock
    private CityVisitService cityVisitService;

    @Mock
    private CityVisitMapper cityVisitMapper;

    @InjectMocks
    private CityVisitFacade cityVisitFacade;

    private CityVisit cityVisit;
    private CityVisitDto cityVisitDTO;
    private List<CityVisit> cityVisitList;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);

        cityVisit = new CityVisit();
        cityVisit.setId(1L);
        cityVisit.setCityName("Prague");
        cityVisit.setDateFrom(LocalDate.now());
        cityVisit.setDateTo(LocalDate.now().plusDays(2));

        CityVisit cityVisit2 = new CityVisit();
        cityVisit2.setId(2L);
        cityVisit2.setCityName("Berlin");
        cityVisit2.setDateFrom(LocalDate.now());
        cityVisit2.setDateTo(LocalDate.now().plusDays(3));

        cityVisitList = Arrays.asList(cityVisit, cityVisit2);

        cityVisitDTO = new CityVisitDto();
        cityVisitDTO.setId(1L);
        cityVisitDTO.setCityName("Prague");
        cityVisitDTO.setDateFrom(new Date());
        cityVisitDTO.setDateTo(new Date());
    }

    @Test
    public void getAllCityVisits_twoPresent_returnsListWithTwoVisits() {
        when(cityVisitService.getAllCityVisits()).thenReturn(cityVisitList);
        when(cityVisitMapper.toDTO(any(CityVisit.class))).thenReturn(cityVisitDTO);

        List<CityVisitDto> result = cityVisitFacade.getAllCityVisits();

        assertEquals(2, result.size());
        verify(cityVisitService, times(1)).getAllCityVisits();
        verify(cityVisitMapper, times(2)).toDTO(any(CityVisit.class));
    }

    @Test
    public void tetCityVisitById_visitExists_returnsVisit() {
        when(cityVisitService.getCityVisitById(anyLong())).thenReturn(cityVisit);
        when(cityVisitMapper.toDTO(any(CityVisit.class))).thenReturn(cityVisitDTO);

        CityVisitDto result = cityVisitFacade.getCityVisitById(1L);

        assertNotNull(result);
        assertEquals("Prague", result.getCityName());
        verify(cityVisitService, times(1)).getCityVisitById(1L);
        verify(cityVisitMapper, times(1)).toDTO(cityVisit);
    }

    @Test
    public void createCityVisit_validRequest_returnsVisit() {
        when(cityVisitMapper.toEntity(any(CityVisitDto.class))).thenReturn(cityVisit);
        when(cityVisitService.createCityVisit(any(CityVisit.class))).thenReturn(cityVisit);
        when(cityVisitMapper.toDTO(any(CityVisit.class))).thenReturn(cityVisitDTO);

        CityVisitDto result = cityVisitFacade.createCityVisit(cityVisitDTO);

        assertNotNull(result);
        assertEquals("Prague", result.getCityName());
        verify(cityVisitMapper, times(1)).toEntity(cityVisitDTO);
        verify(cityVisitService, times(1)).createCityVisit(cityVisit);
        verify(cityVisitMapper, times(1)).toDTO(cityVisit);
    }

    @Test
    public void updateCityVisit_validRequest_returnsUpdatedVisit() {
        when(cityVisitService.getCityVisitById(anyLong())).thenReturn(cityVisit);
        doNothing().when(cityVisitMapper).updateEntityFromDto(any(CityVisitDto.class), any(CityVisit.class));
        when(cityVisitService.createCityVisit(any(CityVisit.class))).thenReturn(cityVisit);
        when(cityVisitMapper.toDTO(any(CityVisit.class))).thenReturn(cityVisitDTO);

        CityVisitDto result = cityVisitFacade.updateCityVisit(1L, cityVisitDTO);

        assertNotNull(result);
        assertEquals("Prague", result.getCityName());
        verify(cityVisitService, times(1)).getCityVisitById(1L);
        verify(cityVisitMapper, times(1)).updateEntityFromDto(cityVisitDTO, cityVisit);
        verify(cityVisitService, times(1)).createCityVisit(cityVisit);
        verify(cityVisitMapper, times(1)).toDTO(cityVisit);
    }

    @Test
    public void deleteCityVisit_visitExists_callsServiceToDelete() {
        doNothing().when(cityVisitService).deleteCityVisit(anyLong());

        cityVisitFacade.deleteCityVisit(1L);

        verify(cityVisitService, times(1)).deleteCityVisit(1L);
    }
}