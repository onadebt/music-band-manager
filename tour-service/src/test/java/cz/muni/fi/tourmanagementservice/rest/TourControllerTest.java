package cz.muni.fi.tourmanagementservice.rest;

import cz.muni.fi.tourmanagementservice.dto.CityVisitDto;
import cz.muni.fi.tourmanagementservice.dto.TourDto;
import cz.muni.fi.tourmanagementservice.facade.TourFacade;
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

    private TourDto tourDTO;
    private List<TourDto> tourDtoList;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);

        CityVisitDto cityVisitDTO = new CityVisitDto();
        cityVisitDTO.setId(1L);
        cityVisitDTO.setCityName("Prague");
        cityVisitDTO.setDateFrom(new Date());
        cityVisitDTO.setDateTo(new Date());

        tourDTO = new TourDto();
        tourDTO.setId(1L);
        tourDTO.setTourName("Europe Tour");
        tourDTO.setBandId(1L);
        tourDTO.setCityVisits(Arrays.asList(cityVisitDTO));

        TourDto tourDto2 = new TourDto();
        tourDto2.setId(2L);
        tourDto2.setTourName("American Tour");
        tourDto2.setBandId(1L);

        tourDtoList = Arrays.asList(tourDTO, tourDto2);
    }

    @Test
    public void getAllTours_containsTwoTours_returnsListWithTwoTours() {
        when(tourFacade.getAllTours()).thenReturn(tourDtoList);

        ResponseEntity<List<TourDto>> response = tourController.getAllTours();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(2, response.getBody().size());
        verify(tourFacade, times(1)).getAllTours();
    }

    @Test
    public void getToursByBand_twoToursMatch_returnsListWithTwoTours() {
        when(tourFacade.getToursByBand(anyLong())).thenReturn(tourDtoList);

        ResponseEntity<List<TourDto>> response = tourController.getToursByBand(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(2, response.getBody().size());
        verify(tourFacade, times(1)).getToursByBand(1L);
    }

    @Test
    public void getTourById_validId_returnsWantedTour() {
        when(tourFacade.getTourById(anyLong())).thenReturn(tourDTO);

        ResponseEntity<TourDto> response = tourController.getTourById(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Europe Tour", response.getBody().getTourName());
        verify(tourFacade, times(1)).getTourById(1L);
    }

    @Test
    public void createTour_validData_returnsCreatedTour() {
        when(tourFacade.createTour(any(TourDto.class))).thenReturn(tourDTO);

        ResponseEntity<TourDto> response = tourController.createTour(tourDTO);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals("Europe Tour", response.getBody().getTourName());
        verify(tourFacade, times(1)).createTour(tourDTO);
    }

    @Test
    public void updateTour_validData_returnsUpdatedTour() {
        when(tourFacade.updateTour(anyLong(), any(TourDto.class))).thenReturn(tourDTO);

        ResponseEntity<TourDto> response = tourController.updateTour(1L, tourDTO);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Europe Tour", response.getBody().getTourName());
        verify(tourFacade, times(1)).updateTour(1L, tourDTO);
    }

    @Test
    public void deleteTour_validId_verifiesDeleteOnFacadeCalled() {
        doNothing().when(tourFacade).deleteTour(anyLong());

        ResponseEntity<Void> response = tourController.deleteTour(1L);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(tourFacade, times(1)).deleteTour(1L);
    }

    @Test
    public void addCityVisitToTour_validInput_verifiesAddCityVisitOnFacadeCalled() {
        CityVisitDto cityVisitDTO = new CityVisitDto();
        cityVisitDTO.setCityName("Berlin");
        doNothing().when(tourFacade).addCityVisitToTour(anyLong(), any(CityVisitDto.class));

        ResponseEntity<Void> response = tourController.addCityVisitToTour(1L, cityVisitDTO);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        verify(tourFacade, times(1)).addCityVisitToTour(1L, cityVisitDTO);
    }

    @Test
    public void removeCityVisitFromTour_visitAndTourExist_verifiesRemoveOnFacadeCalled() {
        doNothing().when(tourFacade).removeCityVisitFromTour(anyLong(), anyLong());

        ResponseEntity<Void> response = tourController.removeCityVisitFromTour(1L, 1L);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(tourFacade, times(1)).removeCityVisitFromTour(1L, 1L);
    }
}
