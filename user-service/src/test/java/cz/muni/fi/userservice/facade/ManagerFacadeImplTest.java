package cz.muni.fi.userservice.facade;

import cz.muni.fi.userservice.TestDataFactory;
import cz.muni.fi.userservice.dto.ManagerDto;
import cz.muni.fi.userservice.mappers.ManagerMapper;
import cz.muni.fi.userservice.model.Manager;
import cz.muni.fi.userservice.service.interfaces.ManagerService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

/**
 * @author Tomáš MAREK
 */
@ExtendWith(MockitoExtension.class)
public class ManagerFacadeImplTest {
    @InjectMocks
    private ManagerFacadeImpl managerFacadeImpl;

    @Mock
    private ManagerService managerService;

    @Mock
    private ManagerMapper managerMapper;

    @Test
    void register_nullManagerDto_throwsIllegalArgumentException() {
        // Act / Assert
        assertThrows(IllegalArgumentException.class, () -> managerFacadeImpl.register(null));
        verify(managerService, Mockito.times(0)).save(any());
    }

    @Test
    void register_validManagerDto_returnsSavedManager() {
        // Arrange
        Manager testManager = TestDataFactory.setUpTestManager1();
        ManagerDto testManagerDto = TestDataFactory.setUpTestManager1Dto();

        Mockito.when(managerService.save(testManager)).thenReturn(testManager);
        Mockito.when(managerMapper.toEntity(testManagerDto)).thenReturn(testManager);
        Mockito.when(managerMapper.toDto(testManager)).thenReturn(testManagerDto);

        // Act
        ManagerDto registered = managerFacadeImpl.register(testManagerDto);

        // Assert
        verify(managerService, times(1)).save(testManager);
        assertEquals(testManagerDto, registered);
    }

    @Test
    void findById_inputNull_throwsIllegalArgumentException() {
        // Act / Assert
        assertThrows(IllegalArgumentException.class, () -> managerFacadeImpl.findById(null));
        verify(managerService, Mockito.times(0)).findById(any());
    }

    @Test
    void findById_invalidId_throwsIllegalArgumentException() {
        // Arrange
        Long invalidId = -1L;
        Mockito.when(managerService.findById(invalidId)).thenReturn(null);

        // Act / Assert
        assertThrows(IllegalArgumentException.class, () -> managerFacadeImpl.findById(invalidId));
    }

    @Test
    void findById_validId_returnsFoundManager() {
        Manager testManager = TestDataFactory.setUpTestManager1();
        ManagerDto testManagerDto = TestDataFactory.setUpTestManager1Dto();

        // Arrange
        Mockito.when(managerService.findById(testManager.getId())).thenReturn(testManager);
        Mockito.when(managerMapper.toDto(testManager)).thenReturn(testManagerDto);

        // Act
        ManagerDto found = managerFacadeImpl.findById(testManager.getId());

        // Assert
        assertEquals(testManagerDto, found);
        verify(managerService, times(1)).findById(testManager.getId());
    }

    @Test
    void findAll_noManagerStored_returnsEmptyList() {
        // Arrange
        Mockito.when(managerService.findAll()).thenReturn(List.of());

        // Act
        List<ManagerDto> found = managerFacadeImpl.findAll();

        // Assert
        assertEquals(0, found.size());
        verify(managerService, times(1)).findAll();
    }

    @Test
    void findAll_twoManagersStored_returnsList() {
        Manager testManager1 = TestDataFactory.setUpTestManager1();
        Manager testManager2 = TestDataFactory.setUpTestManager2();
        ManagerDto testManagerDto1 = TestDataFactory.setUpTestManager1Dto();
        ManagerDto testManagerDto2 = TestDataFactory.setUpTestManager2Dto();

        // Arrange
        Mockito.when(managerService.findAll()).thenReturn(List.of(testManager1, testManager2));
        Mockito.when(managerMapper.toDto(testManager1)).thenReturn(testManagerDto1);
        Mockito.when(managerMapper.toDto(testManager2)).thenReturn(testManagerDto2);

        // Act
        List<ManagerDto> found = managerFacadeImpl.findAll();

        // Assert
        assertEquals(2, found.size());
        verify(managerService, times(1)).findAll();
        assertTrue(found.contains(testManagerDto1));
        assertTrue(found.contains(testManagerDto2));
    }

    @Test
    void findByBandIds_nullArgument_throwsIllegalArgumentException() {
        // Act / Assert
        assertThrows(IllegalArgumentException.class, () -> managerFacadeImpl.findByBandIds(null));
        verify(managerService, times(0)).findByManagedBandIds(any());
    }

    @Test
    void findByBandIds_twoManagersFound_returnsList() {
        Manager testManager1 = TestDataFactory.setUpTestManager1();
        Manager testManager2 = TestDataFactory.setUpTestManager2();
        ManagerDto testManagerDto1 = TestDataFactory.setUpTestManager1Dto();
        ManagerDto testManagerDto2 = TestDataFactory.setUpTestManager2Dto();

        // Arrange
        Set<Long> bandIds = Set.of(1L, 2L);
        Mockito.when(managerService.findByManagedBandIds(bandIds)).thenReturn(List.of(testManager1, testManager2));
        Mockito.when(managerMapper.toDto(testManager1)).thenReturn(testManagerDto1);
        Mockito.when(managerMapper.toDto(testManager2)).thenReturn(testManagerDto2);

        // Act
        List<ManagerDto> found = managerFacadeImpl.findByBandIds(bandIds);

        // Assert
        verify(managerService, times(1)).findByManagedBandIds(bandIds);
        assertTrue(found.contains(testManagerDto1));
        assertTrue(found.contains(testManagerDto2));
    }

    @Test
    void deleteById_nullId_throwsIllegalArgumentException() {
        // Act / Assert
        assertThrows(IllegalArgumentException.class, () -> managerFacadeImpl.deleteById(null));
        verify(managerService, times(0)).deleteById(null);
    }

    @Test
    void deleteById_invalidId_throwsIllegalArgumentException() {
        // Arrange
        Long invalidId = -1L;
        Mockito.when(managerService.findById(invalidId)).thenReturn(null);

        // Act / Assert
        assertThrows(IllegalArgumentException.class, () -> managerFacadeImpl.deleteById(invalidId));
        verify(managerService, times(1)).findById(invalidId);
    }

    @Test
    void deleteById_validId_callsManagerServiceDelete() {
        Manager testManager = TestDataFactory.setUpTestManager1();

        // Act
        managerService.deleteById(testManager.getId());

        // Assert
        verify(managerService, times(1)).deleteById(testManager.getId());
    }

    @Test
    void updateBandIds_nullBandIds_throwsIllegalArgumentException() {
        // Act / Assert
        assertThrows(IllegalArgumentException.class, () -> managerFacadeImpl.updateBandIds(1L, null));
        verify(managerService, times(0)).updateManagerBandIds(any(), any());
    }

    @Test
    void updateBandIds_nullArtisId_throwsIllegalArgumentException() {
        // Act / Assert
        assertThrows(IllegalArgumentException.class, () -> managerFacadeImpl.updateBandIds(null, Set.of()));
        verify(managerService, times(0)).updateManagerBandIds(any(), any());
    }

    @Test
    void updateBandsIds_changedBandIds_returnsUpdatedManager() {
        Manager testManager = TestDataFactory.setUpTestManager1();
        ManagerDto testManagerDto = TestDataFactory.setUpTestManager1Dto();

        // Arrange
        Set<Long> bandIds = Set.of(1L, 2L);
        Mockito.when(managerService.updateManagerBandIds(testManager.getId(), bandIds)).thenReturn(testManager);
        Mockito.when(managerMapper.toDto(testManager)).thenReturn(testManagerDto);

        // Act
        ManagerDto updated = managerFacadeImpl.updateBandIds(testManager.getId(), bandIds);

        // Assert
        assertEquals(testManagerDto, updated);
        verify(managerService, times(1)).updateManagerBandIds(testManager.getId(), bandIds);
    }
}
