package cz.muni.fi.userservice.facade;

import cz.muni.fi.userservice.TestDataFactory;
import cz.muni.fi.userservice.dto.ManagerDto;
import cz.muni.fi.userservice.mappers.ManagerMapper;
import cz.muni.fi.userservice.service.interfaces.IManagerService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

/**
 * @author Tomáš MAREK
 */
@ExtendWith(MockitoExtension.class)
public class ManagerFacadeTest {
    @InjectMocks
    private ManagerFacade managerFacade;

    @Mock
    private IManagerService managerService;

    @Mock
    private ManagerMapper managerMapper;

    @Test
    void register_nullManagerDto_throwsIllegalArgumentException() {
        // Act / Assert
        assertThrows(IllegalArgumentException.class, () -> managerFacade.register(null));
        verify(managerService, Mockito.times(0)).save(any());
    }

    @Test
    void register_validManagerDto_returnsSavedManager() {
        // Arrange
        Mockito.when(managerService.save(TestDataFactory.TEST_MANAGER_1)).thenReturn(TestDataFactory.TEST_MANAGER_1);
        Mockito.when(managerMapper.toEntity(TestDataFactory.TEST_MANAGER_1_DTO)).thenReturn(TestDataFactory.TEST_MANAGER_1);
        Mockito.when(managerMapper.toDTO(TestDataFactory.TEST_MANAGER_1)).thenReturn(TestDataFactory.TEST_MANAGER_1_DTO);

        // Act
        ManagerDto registered = managerFacade.register(TestDataFactory.TEST_MANAGER_1_DTO);

        // Assert
        verify(managerService, times(1)).save(TestDataFactory.TEST_MANAGER_1);
        assertEquals(TestDataFactory.TEST_MANAGER_1_DTO, registered);
    }

    @Test
    void findById_inputNull_throwsIllegalArgumentException() {
        // Act / Assert
        assertThrows(IllegalArgumentException.class, () -> managerFacade.findById(null));
        verify(managerService, Mockito.times(0)).findById(any());
    }

    @Test
    void findById_invalidId_throwsIllegalArgumentException() {
        // Arrange
        Long invalidId = -1L;
        Mockito.when(managerService.findById(invalidId)).thenReturn(null);

        // Act / Assert
        assertThrows(IllegalArgumentException.class, () -> managerFacade.findById(invalidId));
    }

    @Test
    void findById_validId_returnsFoundManager() {
        // Arrange
        Mockito.when(managerService.findById(TestDataFactory.TEST_MANAGER_1.getId())).thenReturn(TestDataFactory.TEST_MANAGER_1);
        Mockito.when(managerMapper.toDTO(TestDataFactory.TEST_MANAGER_1)).thenReturn(TestDataFactory.TEST_MANAGER_1_DTO);
        // Act
        ManagerDto found = managerFacade.findById(TestDataFactory.TEST_MANAGER_1.getId());

        // Assert
        assertEquals(TestDataFactory.TEST_MANAGER_1_DTO, found);
        verify(managerService, times(1)).findById(TestDataFactory.TEST_MANAGER_1.getId());
    }

    @Test
    void findAll_noManagerStored_returnsEmptyList() {
        // Arrange
        Mockito.when(managerService.findAll()).thenReturn(List.of());

        // Act
        List<ManagerDto> found = managerFacade.findAll();

        // Assert
        assertEquals(0, found.size());
        verify(managerService, times(1)).findAll();
    }

    @Test
    void findAll_twoManagersStored_returnsList() {
        // Arrange
        Mockito.when(managerService.findAll()).thenReturn(List.of(TestDataFactory.TEST_MANAGER_1, TestDataFactory.TEST_MANAGER_2));
        Mockito.when(managerMapper.toDTO(TestDataFactory.TEST_MANAGER_1)).thenReturn(TestDataFactory.TEST_MANAGER_1_DTO);
        Mockito.when(managerMapper.toDTO(TestDataFactory.TEST_MANAGER_2)).thenReturn(TestDataFactory.TEST_MANAGER_2_DTO);

        // Act
        List<ManagerDto> found = managerFacade.findAll();

        // Assert
        assertEquals(2, found.size());
        verify(managerService, times(1)).findAll();
        assertTrue(found.contains(TestDataFactory.TEST_MANAGER_1_DTO));
        assertTrue(found.contains(TestDataFactory.TEST_MANAGER_2_DTO));
    }

    @Test
    void findByBandIds_nullArgument_throwsIllegalArgumentException() {
        // Act / Assert
        assertThrows(IllegalArgumentException.class, () -> managerFacade.findByBandIds(null));
        verify(managerService, times(0)).findByManagedBandIds(any());
    }

    @Test
    void findByBandIds_twoManagersFound_returnsList() {
        // Arrange
        Set<Long> bandIds = Set.of(1L, 2L);
        Mockito.when(managerService.findByManagedBandIds(bandIds)).thenReturn(List.of(TestDataFactory.TEST_MANAGER_1, TestDataFactory.TEST_MANAGER_2));
        Mockito.when(managerMapper.toDTO(TestDataFactory.TEST_MANAGER_1)).thenReturn(TestDataFactory.TEST_MANAGER_1_DTO);
        Mockito.when(managerMapper.toDTO(TestDataFactory.TEST_MANAGER_2)).thenReturn(TestDataFactory.TEST_MANAGER_2_DTO);

        // Act
        List<ManagerDto> found = managerFacade.findByBandIds(bandIds);

        // Assert
        verify(managerService, times(1)).findByManagedBandIds(bandIds);
        assertTrue(found.contains(TestDataFactory.TEST_MANAGER_1_DTO));
        assertTrue(found.contains(TestDataFactory.TEST_MANAGER_2_DTO));
    }

    @Test
    void deleteById_nullId_throwsIllegalArgumentException() {
        // Act / Assert
        assertThrows(IllegalArgumentException.class, () -> managerFacade.deleteById(null));
        verify(managerService, times(0)).deleteById(null);
    }

    @Test
    void deleteById_invalidId_throwsIllegalArgumentException() {
        // Arrange
        Long invalidId = -1L;
        Mockito.when(managerService.findById(invalidId)).thenReturn(null);

        // Act / Assert
        assertThrows(IllegalArgumentException.class, () -> managerFacade.deleteById(invalidId));
        verify(managerService, times(1)).findById(invalidId);
    }

    @Test
    void deleteById_validId_callsManagerServiceDelete() {
        // Act
        managerService.deleteById(TestDataFactory.TEST_MANAGER_1.getId());

        // Assert
        verify(managerService, times(1)).deleteById(TestDataFactory.TEST_MANAGER_1.getId());
    }

    @Test
    void updateBandIds_nullBandIds_throwsIllegalArgumentException() {
        // Act / Assert
        assertThrows(IllegalArgumentException.class, () -> managerFacade.updateBandIds(1L, null));
        verify(managerService, times(0)).updateManagerBandIds(any(), any());
    }

    @Test
    void updateBandIds_nullArtisId_throwsIllegalArgumentException() {
        // Act / Assert
        assertThrows(IllegalArgumentException.class, () -> managerFacade.updateBandIds(null, Set.of()));
        verify(managerService, times(0)).updateManagerBandIds(any(), any());
    }

    @Test
    void updateBandsIds_changedBandIds_returnsUpdatedManager() {
        // Arrange
        Set<Long> bandIds = Set.of(1L, 2L);
        Mockito.when(managerService.updateManagerBandIds(TestDataFactory.TEST_MANAGER_1.getId(), bandIds)).thenReturn(TestDataFactory.TEST_MANAGER_1);
        Mockito.when(managerMapper.toDTO(TestDataFactory.TEST_MANAGER_1)).thenReturn(TestDataFactory.TEST_MANAGER_1_DTO);

        // Act
        ManagerDto updated = managerFacade.updateBandIds(TestDataFactory.TEST_MANAGER_1.getId(), bandIds);

        // Assert
        assertEquals(TestDataFactory.TEST_MANAGER_1_DTO, updated);
        verify(managerService, times(1)).updateManagerBandIds(TestDataFactory.TEST_MANAGER_1.getId(), bandIds);
    }

}
