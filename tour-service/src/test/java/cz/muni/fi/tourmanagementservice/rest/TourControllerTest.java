package cz.muni.fi.tourmanagementservice.rest;

import cz.muni.fi.tourmanagementservice.controller.TourController;
import cz.muni.fi.tourmanagementservice.dto.CityVisitDTO;
import cz.muni.fi.tourmanagementservice.dto.TourDTO;
import cz.muni.fi.tourmanagementservice.facades.TourFacade;
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

public class TourControllerTest {

    @Mock
    private TourFacade tourFacade;

    @InjectMocks
    private TourController tourController;

    private TourDTO tourDTO;
    private List<TourDTO> tourDTOList;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);

        CityVisitDTO cityVisitDTO = new CityVisitDTO();
        cityVisitDTO.setId(1L);
        cityVisitDTO.setCityName("Prague");
        cityVisitDTO.setDateFrom(new Date());
        cityVisitDTO.setDateTo(new Date());

        tourDTO = new TourDTO();
        tourDTO.setId(1L);
        tourDTO.setTourName("Europe Tour");
        tourDTO.setBandId(1L);
        tourDTO.setCityVisits(Arrays.asList(cityVisitDTO));

        TourDTO tourDTO2 = new TourDTO();
        tourDTO2.setId(2L);
        tourDTO2.setTourName("American Tour");
        tourDTO2.setBandId(1L);

        tourDTOList = Arrays.asList(tourDTO, tourDTO2);
    }

    @Test
    public void testGetAllTours() {
        when(tourFacade.getAllTours()).thenReturn(tourDTOList);

        ResponseEntity<List<TourDTO>> response = tourController.getAllTours();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(2, response.getBody().size());
        verify(tourFacade, times(1)).getAllTours();
    }

    @Test
    public void testGetToursByBand() {
        when(tourFacade.getToursByBand(anyLong())).thenReturn(tourDTOList);

        ResponseEntity<List<TourDTO>> response = tourController.getToursByBand(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(2, response.getBody().size());
        verify(tourFacade, times(1)).getToursByBand(1L);
    }

    @Test
    public void testGetTourById() {
        when(tourFacade.getTourById(anyLong())).thenReturn(tourDTO);

        ResponseEntity<TourDTO> response = tourController.getTourById(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Europe Tour", response.getBody().getTourName());
        verify(tourFacade, times(1)).getTourById(1L);
    }

    @Test
    public void testCreateTour() {
        when(tourFacade.createTour(any(TourDTO.class))).thenReturn(tourDTO);

        ResponseEntity<TourDTO> response = tourController.createTour(tourDTO);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals("Europe Tour", response.getBody().getTourName());
        verify(tourFacade, times(1)).createTour(tourDTO);
    }

    @Test
    public void testUpdateTour() {
        when(tourFacade.updateTour(anyLong(), any(TourDTO.class))).thenReturn(tourDTO);

        ResponseEntity<TourDTO> response = tourController.updateTour(1L, tourDTO);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Europe Tour", response.getBody().getTourName());
        verify(tourFacade, times(1)).updateTour(1L, tourDTO);
    }

    @Test
    public void testDeleteTour() {
        doNothing().when(tourFacade).deleteTour(anyLong());

        ResponseEntity<Void> response = tourController.deleteTour(1L);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(tourFacade, times(1)).deleteTour(1L);
    }

    @Test
    public void testAddCityVisitToTour() {
        CityVisitDTO cityVisitDTO = new CityVisitDTO();
        cityVisitDTO.setCityName("Berlin");
        doNothing().when(tourFacade).addCityVisitToTour(anyLong(), any(CityVisitDTO.class));

        ResponseEntity<Void> response = tourController.addCityVisitToTour(1L, cityVisitDTO);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        verify(tourFacade, times(1)).addCityVisitToTour(1L, cityVisitDTO);
    }

    @Test
    public void testRemoveCityVisitFromTour() {
        doNothing().when(tourFacade).removeCityVisitFromTour(anyLong(), anyLong());

        ResponseEntity<Void> response = tourController.removeCityVisitFromTour(1L, 1L);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(tourFacade, times(1)).removeCityVisitFromTour(1L, 1L);
    }
}
