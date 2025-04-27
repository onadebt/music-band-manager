package cz.muni.fi.tourmanagementservice.service;

import cz.muni.fi.tourmanagementservice.exception.ResourceNotFoundException;
import cz.muni.fi.tourmanagementservice.model.CityVisit;
import cz.muni.fi.tourmanagementservice.repository.CityVisitRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CityVisitServiceTest {

    @Mock
    private CityVisitRepository cityVisitRepository;

    @InjectMocks
    private CityVisitService cityVisitService;

    private CityVisit cityVisit;

    @BeforeEach
    void setUp() {
        cityVisit = new CityVisit();
        cityVisit.setId(1L);
        cityVisit.setCityName("Prague");
        cityVisit.setDateFrom(LocalDate.of(2025, 5, 15));
        cityVisit.setDateTo(LocalDate.of(2025, 5, 17));
    }

    @Test
    void getAllCityVisits_ShouldReturnAllCityVisits() {
        List<CityVisit> cityVisits = Arrays.asList(cityVisit);
        when(cityVisitRepository.findAll()).thenReturn(cityVisits);

        List<CityVisit> result = cityVisitService.getAllCityVisits();

        assertEquals(1, result.size());
        assertEquals("Prague", result.get(0).getCityName());
        verify(cityVisitRepository, times(1)).findAll();
    }

    @Test
    void getCityVisitById_ShouldReturnCityVisit_WhenExists() {
        when(cityVisitRepository.findById(1L)).thenReturn(Optional.of(cityVisit));

        CityVisit result = cityVisitService.getCityVisitById(1L);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("Prague", result.getCityName());
        verify(cityVisitRepository, times(1)).findById(1L);
    }

    @Test
    void getCityVisitById_ShouldThrowException_WhenCityVisitDoesNotExist() {
        when(cityVisitRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> cityVisitService.getCityVisitById(99L));
        verify(cityVisitRepository, times(1)).findById(99L);
    }

    @Test
    void createCityVisit_ShouldReturnSavedCityVisit() {
        when(cityVisitRepository.save(any(CityVisit.class))).thenReturn(cityVisit);
        CityVisit result = cityVisitService.createCityVisit(cityVisit);

        assertNotNull(result);
        assertEquals("Prague", result.getCityName());
        verify(cityVisitRepository, times(1)).save(cityVisit);
    }

    @Test
    void updateCityVisit_ShouldUpdateCityVisitFields() {
        CityVisit updatedCityVisit = new CityVisit();
        updatedCityVisit.setCityName("Berlin");
        updatedCityVisit.setDateFrom(LocalDate.of(2025, 6, 10));
        updatedCityVisit.setDateTo(LocalDate.of(2025, 6, 12));

        when(cityVisitRepository.findById(1L)).thenReturn(Optional.of(cityVisit));
        when(cityVisitRepository.save(any(CityVisit.class))).thenAnswer(invocation -> invocation.getArgument(0));

        CityVisit result = cityVisitService.updateCityVisit(1L, updatedCityVisit);

        assertNotNull(result);
        assertEquals("Berlin", result.getCityName());
        assertEquals(LocalDate.of(2025, 6, 10), result.getDateFrom());
        assertEquals(LocalDate.of(2025, 6, 12), result.getDateTo());
        verify(cityVisitRepository, times(1)).findById(1L);
        verify(cityVisitRepository, times(1)).save(cityVisit);
    }

    @Test
    void deleteCityVisit_ShouldDeleteCityVisit() {
        when(cityVisitRepository.findById(1L)).thenReturn(Optional.of(cityVisit));
        doNothing().when(cityVisitRepository).deleteById(1L);

        cityVisitService.deleteCityVisit(1L);

        verify(cityVisitRepository, times(1)).findById(1L);
        verify(cityVisitRepository, times(1)).deleteById(1L);
    }

    @Test
    void deleteCityVisit_ShouldThrowException_WhenCityVisitDoesNotExist() {
        when(cityVisitRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> cityVisitService.deleteCityVisit(99L));
        verify(cityVisitRepository, times(1)).findById(99L);
        verify(cityVisitRepository, never()).deleteById(anyLong());
    }
}