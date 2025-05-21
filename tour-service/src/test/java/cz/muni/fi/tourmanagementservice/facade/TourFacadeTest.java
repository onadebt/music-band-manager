package cz.muni.fi.tourmanagementservice.facade;

import cz.muni.fi.tourmanagementservice.dto.CityVisitDto;
import cz.muni.fi.tourmanagementservice.dto.TourDto;
import cz.muni.fi.tourmanagementservice.mapper.CityVisitMapper;
import cz.muni.fi.tourmanagementservice.mapper.TourMapper;
import cz.muni.fi.tourmanagementservice.model.CityVisit;
import cz.muni.fi.tourmanagementservice.model.Tour;
import cz.muni.fi.tourmanagementservice.service.TourService;
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

public class TourFacadeTest {

    @Mock
    private TourService tourService;

    @Mock
    private TourMapper tourMapper;

    @Mock
    private CityVisitMapper cityVisitMapper;

    @InjectMocks
    private TourFacade tourFacade;

    private Tour tour;
    private TourDto tourDTO;
    private CityVisit cityVisit;
    private CityVisitDto cityVisitDTO;
    private List<Tour> tourList;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);

        cityVisit = new CityVisit();
        cityVisit.setId(1L);
        cityVisit.setCityName("Prague");
        cityVisit.setDateFrom(LocalDate.now());
        cityVisit.setDateTo(LocalDate.now().plusDays(2));

        tour = new Tour();
        tour.setId(1L);
        tour.setTourName("Europe Tour");
        tour.setBandId(1L);
        tour.setCityVisits(Arrays.asList(cityVisit));

        Tour tour2 = new Tour();
        tour2.setId(2L);
        tour2.setTourName("American Tour");
        tour2.setBandId(1L);

        tourList = Arrays.asList(tour, tour2);

        cityVisitDTO = new CityVisitDto();
        cityVisitDTO.setId(1L);
        cityVisitDTO.setCityName("Prague");
        cityVisitDTO.setDateFrom(new Date());
        cityVisitDTO.setDateTo(new Date());

        tourDTO = new TourDto();
        tourDTO.setId(1L);
        tourDTO.setTourName("Europe Tour");
        tourDTO.setBandId(1L);
        tourDTO.setCityVisits(Arrays.asList(cityVisitDTO));
    }

    @Test
    public void testGetAllTours() {
        when(tourService.getAllTours()).thenReturn(tourList);
        when(tourMapper.toDTO(any(Tour.class))).thenReturn(tourDTO);

        List<TourDto> result = tourFacade.getAllTours();

        assertEquals(2, result.size());
        verify(tourService, times(1)).getAllTours();
        verify(tourMapper, times(2)).toDTO(any(Tour.class));
    }

    @Test
    public void testGetToursByBand() {
        when(tourService.getToursByBand(anyLong())).thenReturn(tourList);
        when(tourMapper.toDTO(any(Tour.class))).thenReturn(tourDTO);

        List<TourDto> result = tourFacade.getToursByBand(1L);

        assertEquals(2, result.size());
        verify(tourService, times(1)).getToursByBand(1L);
        verify(tourMapper, times(2)).toDTO(any(Tour.class));
    }

    @Test
    public void testGetTourById() {
        when(tourService.getTourById(anyLong())).thenReturn(tour);
        when(tourMapper.toDTO(any(Tour.class))).thenReturn(tourDTO);

        TourDto result = tourFacade.getTourById(1L);

        assertNotNull(result);
        assertEquals("Europe Tour", result.getTourName());
        verify(tourService, times(1)).getTourById(1L);
        verify(tourMapper, times(1)).toDTO(tour);
    }

    @Test
    public void testCreateTour() {
        when(tourMapper.toEntity(any(TourDto.class))).thenReturn(tour);
        when(tourService.createTour(any(Tour.class))).thenReturn(tour);
        when(tourMapper.toDTO(any(Tour.class))).thenReturn(tourDTO);

        TourDto result = tourFacade.createTour(tourDTO);

        assertNotNull(result);
        assertEquals("Europe Tour", result.getTourName());
        verify(tourMapper, times(1)).toEntity(tourDTO);
        verify(tourService, times(1)).createTour(tour);
        verify(tourMapper, times(1)).toDTO(tour);
    }

    @Test
    public void testUpdateTour() {
        when(tourMapper.toEntity(any(TourDto.class))).thenReturn(tour);
        when(tourService.updateTour(anyLong(), any(Tour.class))).thenReturn(tour);
        when(tourMapper.toDTO(any(Tour.class))).thenReturn(tourDTO);

        TourDto result = tourFacade.updateTour(1L, tourDTO);

        assertNotNull(result);
        assertEquals("Europe Tour", result.getTourName());
        verify(tourMapper, times(1)).toEntity(tourDTO);
        verify(tourService, times(1)).updateTour(1L, tour);
        verify(tourMapper, times(1)).toDTO(tour);
    }

    @Test
    public void testDeleteTour() {
        doNothing().when(tourService).deleteTour(anyLong());

        tourFacade.deleteTour(1L);

        verify(tourService, times(1)).deleteTour(1L);
    }

    @Test
    public void testAddCityVisitToTour() {
        when(cityVisitMapper.toEntity(any(CityVisitDto.class))).thenReturn(cityVisit);
        doNothing().when(tourService).addCityVisitToTour(anyLong(), any(CityVisit.class));

        tourFacade.addCityVisitToTour(1L, cityVisitDTO);

        verify(cityVisitMapper, times(1)).toEntity(cityVisitDTO);
        verify(tourService, times(1)).addCityVisitToTour(1L, cityVisit);
    }

    @Test
    public void testRemoveCityVisitFromTour() {
        doNothing().when(tourService).removeCityVisitFromTour(anyLong(), anyLong());

        tourFacade.removeCityVisitFromTour(1L, 1L);

        verify(tourService, times(1)).removeCityVisitFromTour(1L, 1L);
    }
}