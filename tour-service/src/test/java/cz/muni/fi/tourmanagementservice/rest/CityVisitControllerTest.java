package cz.muni.fi.tourmanagementservice.rest;

import cz.muni.fi.tourmanagementservice.controller.CityVisitController;
import cz.muni.fi.tourmanagementservice.dto.CityVisitDto;
import cz.muni.fi.tourmanagementservice.facades.CityVisitFacade;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

public class CityVisitControllerTest {

    @Mock
    private CityVisitFacade cityVisitFacade;

    @InjectMocks
    private CityVisitController cityVisitController;

    private CityVisitDto cityVisitDTO;
    private List<CityVisitDto> cityVisitDtoList;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);

        cityVisitDTO = new CityVisitDto();
        cityVisitDTO.setId(1L);
        cityVisitDTO.setCityName("Prague");
        cityVisitDTO.setDateFrom(new Date());
        cityVisitDTO.setDateTo(new Date());

        CityVisitDto cityVisitDto2 = new CityVisitDto();
        cityVisitDto2.setId(2L);
        cityVisitDto2.setCityName("Berlin");
        cityVisitDto2.setDateFrom(new Date());
        cityVisitDto2.setDateTo(new Date());

        cityVisitDtoList = Arrays.asList(cityVisitDTO, cityVisitDto2);
    }

    @Test
    public void getAllCityVisits_containsTwoVisits_returnsListWithTwoVisits() {
        when(cityVisitFacade.getAllCityVisits()).thenReturn(cityVisitDtoList);

        ResponseEntity<List<CityVisitDto>> response = cityVisitController.getAllCityVisits();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(2, response.getBody().size());
        verify(cityVisitFacade, times(1)).getAllCityVisits();
    }

    @Test
    public void getCityVisitById_validId_returnsVisit() {
        when(cityVisitFacade.getCityVisitById(anyLong())).thenReturn(cityVisitDTO);

        ResponseEntity<CityVisitDto> response = cityVisitController.getCityVisitById(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Prague", response.getBody().getCityName());
        verify(cityVisitFacade, times(1)).getCityVisitById(1L);
    }

    @Test
    public void createCityVisit_validData_returnsCreatedVisit() {
        when(cityVisitFacade.createCityVisit(any(CityVisitDto.class))).thenReturn(cityVisitDTO);

        ResponseEntity<CityVisitDto> response = cityVisitController.createCityVisit(cityVisitDTO);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals("Prague", response.getBody().getCityName());
        verify(cityVisitFacade, times(1)).createCityVisit(cityVisitDTO);
    }

    @Test
    public void updateCityVisit_validData_returnsUpdatedVisit() {
        when(cityVisitFacade.updateCityVisit(anyLong(), any(CityVisitDto.class))).thenReturn(cityVisitDTO);

        ResponseEntity<CityVisitDto> response = cityVisitController.updateCityVisit(1L, cityVisitDTO);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Prague", response.getBody().getCityName());
        verify(cityVisitFacade, times(1)).updateCityVisit(1L, cityVisitDTO);
    }

    @Test
    public void deleteCityVisit_validId_verifyDeleteOnFacadeCalled() {
        doNothing().when(cityVisitFacade).deleteCityVisit(anyLong());

        ResponseEntity<Void> response = cityVisitController.deleteCityVisit(1L);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        verify(cityVisitFacade, times(1)).deleteCityVisit(1L);
    }
}