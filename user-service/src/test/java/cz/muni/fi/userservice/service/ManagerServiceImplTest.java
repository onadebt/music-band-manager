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
public class ManagerServiceImplTest {
    @Mock
    ManagerRepository managerRepository;

    @InjectMocks
    ManagerServiceImpl managerServiceImpl;

    @Test
    void save_managerSaved_returnsSavedManager() {
        // Arrange
        Manager manager = TestDataFactory.setUpTestManager1();

        Mockito.when(managerRepository.save(manager)).thenReturn(manager);

        // Act
        Manager saved = managerServiceImpl.save(manager);

        // Assert
        assertEquals(manager, saved);
    }

    @Test
    void findById_managerFound_returnsManager() {
        // Arrange
        Manager manager = TestDataFactory.setUpTestManager1();
        Mockito.when(managerRepository.findById(manager.getId())).thenReturn(Optional.of(manager));

        // Act
        Manager found = managerServiceImpl.findById(manager.getId());

        // Assert
        assertEquals(manager, found);
    }

    @Test
    void findById_managerNotFound_throwsUserNotFoundException() {
        // Arrange
        Long invalidId = 42L;

        // Act & Assert
        assertThrows(UserNotFoundException.class, () -> managerServiceImpl.findById(invalidId));
    }


    @Test
    void findAll_noManagersStored_returnsEmptyList() {
        // Arrange
        Mockito.when(managerRepository.findAll()).thenReturn(List.of());

        // Act
        List<Manager> found = managerServiceImpl.findAll();

        // Assert
        assertEquals(0, found.size());
    }

    @Test
    void findAll_twoManagersStored_returnsList() {
        // Arrange
        Manager manager = TestDataFactory.setUpTestManager1();
        Manager manager2 = TestDataFactory.setUpTestManager2();
        Mockito.when(managerRepository.findAll()).thenReturn(List.of(manager, manager2));

        // Act
        List<Manager> found = managerServiceImpl.findAll();

        // Assert
        assertEquals(2, found.size());
        assertTrue(found.contains(manager));
        assertTrue(found.contains(manager2));
    }

    @Test
    void deleteById_managerPresent_noReturn() {
        // Arrange
        Manager manager = TestDataFactory.setUpTestManager1();
        Mockito.when(managerRepository.findById(manager.getId())).thenReturn(Optional.of(manager));

        // Act
        managerServiceImpl.deleteById(manager.getId());

        // Assert
        Mockito.verify(managerRepository, Mockito.times(1)).deleteById(manager.getId());
    }

    @Test
    void findByManagedBandIds_noArtisInBands_returnsEmptyList() {
        // Arrange
        Set<Long> emptyBandIds = Set.of(5L);
        Mockito.when(managerRepository.findByManagedBandIds(emptyBandIds)).thenReturn(List.of());

        // Act
        List<Manager> found = managerServiceImpl.findByManagedBandIds(emptyBandIds);

        // Assert
        assertEquals(0, found.size());
    }

    @Test
    void findByManagedBandIds_managersInBands_returnsList() {
        // Arrange
        Manager manager = TestDataFactory.setUpTestManager1();
        Manager manager2 = TestDataFactory.setUpTestManager2();
        Set<Long> bands = Set.of(2L, 3L);
        Mockito.when(managerRepository.findByManagedBandIds(bands)).thenReturn(List.of(manager, manager2));

        // Act
        List<Manager> found = managerServiceImpl.findByManagedBandIds(bands);

        // Assert
        assertEquals(2, found.size());
        assertTrue(found.contains(manager));
        assertTrue(found.contains(manager2));
    }

    @Test
    void findByManagedBandIds_noManagersInBands_returnsEmptyList() {
        // Arrange
        Set<Long> idsOfEmptyBands = Set.of();
        Mockito.when(managerRepository.findByManagedBandIds(idsOfEmptyBands)).thenReturn(List.of());

        // Act
        List<Manager> found = managerServiceImpl.findByManagedBandIds(idsOfEmptyBands);

        // Assert
        assertEquals(0, found.size());
    }

    @Test
    void findByManagedBandIds_emptyBands_returnsEmptyList() {
        // Arrange
        Set<Long> emptyBandIds = Set.of();
        Mockito.when(managerRepository.findByManagedBandIds(emptyBandIds)).thenReturn(List.of());

        // Act
        List<Manager> found = managerServiceImpl.findByManagedBandIds(emptyBandIds);

        // Assert
        assertEquals(0, found.size());
    }

    @Test
    void updateManagerByBandsIds_newBandAdded_returnsUpdatedManager() {
        // Arrange
        Manager manager = TestDataFactory.setUpTestManager1();
        Set<Long> bandIds = Set.of(1L, 2L, 3L);
        Mockito.when(managerRepository.findById(manager.getId())).thenReturn(Optional.of(manager));
        Mockito.when(managerRepository.save(manager)).thenReturn(manager);
        Set<Long> newBandIds = Set.of(1L, 2L, 3L, 4L);

        // Act
        Manager updated = managerServiceImpl.updateManagerBandIds(manager.getId(), newBandIds);

        // Assert
        assertEquals(manager, updated);
        assertEquals(newBandIds, updated.getManagedBandIds());
    }

    @Test
    void updateManagerByBandsIds_allBandsRemoved_returnsUpdatedManager() {
        // Arrange
        Manager manager = TestDataFactory.setUpTestManager1();
        Mockito.when(managerRepository.findById(manager.getId())).thenReturn(Optional.of(manager));
        Mockito.when(managerRepository.save(manager)).thenReturn(manager);
        Set<Long> emptyBands = Set.of();

        // Act
        Manager updated = managerServiceImpl.updateManagerBandIds(manager.getId(), emptyBands);

        // Assert
        assertEquals(manager, updated);
        assertEquals(emptyBands, updated.getManagedBandIds());
    }

    @Test
    void updateManagerByBandsIds_invalidManagerId_throwsUserNotFoundException() {
        // Arrange
        Long invalidId = 42L;
        Mockito.when(managerRepository.findById(invalidId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(UserNotFoundException.class, () -> managerServiceImpl.updateManagerBandIds(invalidId, Set.of(1L, 2L, 3L)));
    }
}
