package cz.muni.fi.tourmanagementservice.rest;

import cz.muni.fi.tourmanagementservice.controller.CityVisitController;
import cz.muni.fi.tourmanagementservice.dto.CityVisitDTO;
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

    private CityVisitDTO cityVisitDTO;
    private List<CityVisitDTO> cityVisitDTOList;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);

        cityVisitDTO = new CityVisitDTO();
        cityVisitDTO.setId(1L);
        cityVisitDTO.setCityName("Prague");
        cityVisitDTO.setDateFrom(new Date());
        cityVisitDTO.setDateTo(new Date());

        CityVisitDTO cityVisitDTO2 = new CityVisitDTO();
        cityVisitDTO2.setId(2L);
        cityVisitDTO2.setCityName("Berlin");
        cityVisitDTO2.setDateFrom(new Date());
        cityVisitDTO2.setDateTo(new Date());

        cityVisitDTOList = Arrays.asList(cityVisitDTO, cityVisitDTO2);
    }

    @Test
    public void testGetAllCityVisits() {
        when(cityVisitFacade.getAllCityVisits()).thenReturn(cityVisitDTOList);

        ResponseEntity<List<CityVisitDTO>> response = cityVisitController.getAllCityVisits();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(2, response.getBody().size());
        verify(cityVisitFacade, times(1)).getAllCityVisits();
    }

    @Test
    public void testGetCityVisitById() {
        when(cityVisitFacade.getCityVisitById(anyLong())).thenReturn(cityVisitDTO);

        ResponseEntity<CityVisitDTO> response = cityVisitController.getCityVisitById(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Prague", response.getBody().getCityName());
        verify(cityVisitFacade, times(1)).getCityVisitById(1L);
    }

    @Test
    public void testCreateCityVisit() {
        when(cityVisitFacade.createCityVisit(any(CityVisitDTO.class))).thenReturn(cityVisitDTO);

        ResponseEntity<CityVisitDTO> response = cityVisitController.createCityVisit(cityVisitDTO);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals("Prague", response.getBody().getCityName());
        verify(cityVisitFacade, times(1)).createCityVisit(cityVisitDTO);
    }

    @Test
    public void testUpdateCityVisit() {
        when(cityVisitFacade.updateCityVisit(anyLong(), any(CityVisitDTO.class))).thenReturn(cityVisitDTO);

        ResponseEntity<CityVisitDTO> response = cityVisitController.updateCityVisit(1L, cityVisitDTO);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Prague", response.getBody().getCityName());
        verify(cityVisitFacade, times(1)).updateCityVisit(1L, cityVisitDTO);
    }

    @Test
    public void testDeleteCityVisit() {
        doNothing().when(cityVisitFacade).deleteCityVisit(anyLong());

        ResponseEntity<Void> response = cityVisitController.deleteCityVisit(1L);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        verify(cityVisitFacade, times(1)).deleteCityVisit(1L);
    }
}