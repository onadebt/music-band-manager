package cz.muni.fi.userservice.service;

import cz.muni.fi.userservice.TestDataFactory;
import cz.muni.fi.userservice.exception.UserNotFoundException;
import cz.muni.fi.userservice.model.Manager;
import cz.muni.fi.userservice.repository.ManagerRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Tomáš MAREK
 */
@ExtendWith(MockitoExtension.class)
public class ManagerServiceTest {
    @Mock
    ManagerRepository managerRepository;

    @InjectMocks
    ManagerService managerService;

    @Test
    void save_managerSaved_returnsSavedManager() {
        // Arrange
        Mockito.when(managerRepository.save(TestDataFactory.TEST_MANAGER_1)).thenReturn(TestDataFactory.TEST_MANAGER_1);

        // Act
        Manager saved = managerService.save(TestDataFactory.TEST_MANAGER_1);

        // Assert
        assertEquals(TestDataFactory.TEST_MANAGER_1, saved);
    }

    @Test
    void findById_managerFound_returnsManager() {
        // Arrange
        Mockito.when(managerRepository.findById(TestDataFactory.TEST_MANAGER_1.getId())).thenReturn(Optional.of(TestDataFactory.TEST_MANAGER_1));

        // Act
        Manager found = managerService.findById(TestDataFactory.TEST_MANAGER_1.getId());

        // Assert
        assertEquals(TestDataFactory.TEST_MANAGER_1, found);
    }

    @Test
    void findById_managerNotFound_throwsUserNotFoundException() {
        // Arrange
        Long invalidId = 42L;

        // Act & Assert
        assertThrows(UserNotFoundException.class, () -> managerService.findById(invalidId));
    }


    @Test
    void findAll_noManagersStored_returnsEmptyList() {
        // Arrange
        Mockito.when(managerRepository.findAll()).thenReturn(List.of());

        // Act
        List<Manager> found = managerService.findAll();

        // Assert
        assertEquals(0, found.size());
    }

    @Test
    void findAll_twoManagersStored_returnsList() {
        // Arrange
        Mockito.when(managerRepository.findAll()).thenReturn(List.of(TestDataFactory.TEST_MANAGER_1, TestDataFactory.TEST_MANAGER_2));

        // Act
        List<Manager> found = managerService.findAll();

        // Assert
        assertEquals(2, found.size());
        assertTrue(found.contains(TestDataFactory.TEST_MANAGER_1));
        assertTrue(found.contains(TestDataFactory.TEST_MANAGER_2));
    }

    @Test
    void deleteById_managerPresent_noReturn() {
        // Arrange
        Mockito.when(managerRepository.findById(TestDataFactory.TEST_MANAGER_1.getId())).thenReturn(Optional.of(TestDataFactory.TEST_MANAGER_1));

        // Act
        managerService.deleteById(TestDataFactory.TEST_MANAGER_1.getId());

        // Assert
        Mockito.verify(managerRepository, Mockito.times(1)).deleteById(TestDataFactory.TEST_MANAGER_1.getId());
    }

    @Test
    void findByManagedBandIds_noArtisInBands_returnsEmptyList() {
        // Arrange
        Set<Long> emptyBandIds = Set.of(5L);
        Mockito.when(managerRepository.findByManagedBandIds(emptyBandIds)).thenReturn(List.of());

        // Act
        List<Manager> found = managerService.findByManagedBandIds(emptyBandIds);

        // Assert
        assertEquals(0, found.size());
    }

    @Test
    void findByManagedBandIds_managersInBands_returnsList() {
        // Arrange
        Set<Long> bands = Set.of(2L, 3L);
        Mockito.when(managerRepository.findByManagedBandIds(bands)).thenReturn(List.of(TestDataFactory.TEST_MANAGER_1, TestDataFactory.TEST_MANAGER_2));

        // Act
        List<Manager> found = managerService.findByManagedBandIds(bands);

        // Assert
        assertEquals(2, found.size());
        assertTrue(found.contains(TestDataFactory.TEST_MANAGER_1));
        assertTrue(found.contains(TestDataFactory.TEST_MANAGER_2));
    }

    @Test
    void findByManagedBandIds_noManagersInBands_returnsEmptyList() {
        // Arrange
        Set<Long> idsOfEmptyBands = Set.of();
        Mockito.when(managerRepository.findByManagedBandIds(idsOfEmptyBands)).thenReturn(List.of());

        // Act
        List<Manager> found = managerService.findByManagedBandIds(idsOfEmptyBands);

        // Assert
        assertEquals(0, found.size());
    }

    @Test
    void findByManagedBandIds_emptyBands_returnsEmptyList() {
        // Arrange
        Set<Long> emptyBandIds = Set.of();
        Mockito.when(managerRepository.findByManagedBandIds(emptyBandIds)).thenReturn(List.of());

        // Act
        List<Manager> found = managerService.findByManagedBandIds(emptyBandIds);

        // Assert
        assertEquals(0, found.size());
    }

    @Test
    void updateManagerByBandsIds_newBandAdded_returnsUpdatedManager() {
        // Arrange
        Mockito.when(managerRepository.findById(TestDataFactory.TEST_MANAGER_1.getId())).thenReturn(Optional.of(TestDataFactory.TEST_MANAGER_1));
        Mockito.when(managerRepository.save(TestDataFactory.TEST_MANAGER_1)).thenReturn(TestDataFactory.TEST_MANAGER_1);
        Set<Long> newBandIds = Set.of(1L, 2L, 3L, 4L);

        // Act
        Manager updated = managerService.updateManagerBandIds(TestDataFactory.TEST_MANAGER_1.getId(), newBandIds);

        // Assert
        assertEquals(TestDataFactory.TEST_MANAGER_1, updated);
        assertEquals(newBandIds, updated.getManagedBandIds());
    }

    @Test
    void updateManagerByBandsIds_allBandsRemoved_returnsUpdatedManager() {
        // Arrange
        Mockito.when(managerRepository.findById(TestDataFactory.TEST_MANAGER_1.getId())).thenReturn(Optional.of(TestDataFactory.TEST_MANAGER_1));
        Mockito.when(managerRepository.save(TestDataFactory.TEST_MANAGER_1)).thenReturn(TestDataFactory.TEST_MANAGER_1);
        Set<Long> emptyBands = Set.of();

        // Act
        Manager updated = managerService.updateManagerBandIds(TestDataFactory.TEST_MANAGER_1.getId(), emptyBands);

        // Assert
        assertEquals(TestDataFactory.TEST_MANAGER_1, updated);
        assertEquals(emptyBands, updated.getManagedBandIds());
    }

    @Test
    void updateManagerByBandsIds_invalidManagerId_throwsUserNotFoundException() {
        // Arrange
        Long invalidId = 42L;
        Mockito.when(managerRepository.findById(invalidId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(UserNotFoundException.class, () -> managerService.updateManagerBandIds(invalidId, Set.of(1L, 2L, 3L)));
    }
}
