package cz.muni.fi.userservice.facade;

import cz.muni.fi.userservice.TestDataFactory;
import cz.muni.fi.userservice.dto.ManagerDto;
import cz.muni.fi.userservice.dto.ManagerUpdateDto;
import cz.muni.fi.userservice.mapper.ManagerMapper;
import cz.muni.fi.userservice.model.Manager;
import cz.muni.fi.userservice.service.interfaces.ManagerService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

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
    void register_validManagerDto_returnsSavedManager() {
        Manager testManager = TestDataFactory.setUpTestManager1();
        ManagerDto testManagerDto = TestDataFactory.setUpTestManager1Dto();

        when(managerService.save(testManager)).thenReturn(testManager);
        when(managerMapper.toEntity(testManagerDto)).thenReturn(testManager);
        when(managerMapper.toDto(testManager)).thenReturn(testManagerDto);

        ManagerDto registered = managerFacadeImpl.register(testManagerDto);

        verify(managerService, times(1)).save(testManager);
        assertEquals(testManagerDto, registered);
    }

    @Test
    void update_validInput_updatesManager() {
        Manager testManager = TestDataFactory.setUpTestManager1();
        ManagerUpdateDto updateDto = new ManagerUpdateDto("Updated Company", Set.of(99L));
        Manager updatedManager = new Manager();
        updatedManager.setId(testManager.getId());
        updatedManager.setCompanyName("Updated Company");

        when(managerService.findById(testManager.getId())).thenReturn(testManager);
        when(managerMapper.updateManagerFromDto(updateDto, testManager)).thenReturn(updatedManager);
        when(managerService.updateManager(testManager.getId(), updatedManager)).thenReturn(updatedManager);
        when(managerMapper.toDto(updatedManager)).thenReturn(TestDataFactory.setUpTestManager1Dto());

        ManagerDto result = managerFacadeImpl.update(testManager.getId(), updateDto);

        assertNotNull(result);
        verify(managerService).updateManager(eq(testManager.getId()), eq(updatedManager));
    }

    @Test
    void update_emptyBandIds_allowed() {
        Manager testManager = TestDataFactory.setUpTestManager1();
        ManagerUpdateDto updateDto = new ManagerUpdateDto("Empty Band List", Set.of());
        Manager updatedManager = new Manager();
        updatedManager.setId(testManager.getId());
        updatedManager.setCompanyName("Empty Band List");

        when(managerService.findById(testManager.getId())).thenReturn(testManager);
        when(managerMapper.updateManagerFromDto(updateDto, testManager)).thenReturn(updatedManager);
        when(managerService.updateManager(testManager.getId(), updatedManager)).thenReturn(updatedManager);
        when(managerMapper.toDto(updatedManager)).thenReturn(TestDataFactory.setUpTestManager1Dto());

        ManagerDto result = managerFacadeImpl.update(testManager.getId(), updateDto);

        assertNotNull(result);
    }

    @Test
    void findById_validId_returnsFoundManager() {
        Manager testManager = TestDataFactory.setUpTestManager1();
        ManagerDto testManagerDto = TestDataFactory.setUpTestManager1Dto();

        when(managerService.findById(testManager.getId())).thenReturn(testManager);
        when(managerMapper.toDto(testManager)).thenReturn(testManagerDto);

        ManagerDto found = managerFacadeImpl.findById(testManager.getId());

        assertEquals(testManagerDto, found);
        verify(managerService, times(1)).findById(testManager.getId());
    }

    @Test
    void findAll_noManagerStored_returnsEmptyList() {
        when(managerService.findAll()).thenReturn(List.of());

        List<ManagerDto> found = managerFacadeImpl.findAll();

        assertEquals(0, found.size());
        verify(managerService, times(1)).findAll();
    }

    @Test
    void findAll_twoManagersStored_returnsList() {
        Manager testManager1 = TestDataFactory.setUpTestManager1();
        Manager testManager2 = TestDataFactory.setUpTestManager2();
        ManagerDto testManagerDto1 = TestDataFactory.setUpTestManager1Dto();
        ManagerDto testManagerDto2 = TestDataFactory.setUpTestManager2Dto();

        when(managerService.findAll()).thenReturn(List.of(testManager1, testManager2));
        when(managerMapper.toDto(testManager1)).thenReturn(testManagerDto1);
        when(managerMapper.toDto(testManager2)).thenReturn(testManagerDto2);

        List<ManagerDto> found = managerFacadeImpl.findAll();

        assertEquals(2, found.size());
        assertTrue(found.contains(testManagerDto1));
        assertTrue(found.contains(testManagerDto2));
        verify(managerService, times(1)).findAll();
    }

    @Test
    void findByBandIds_validSet_returnsList() {
        Set<Long> ids = Set.of(1L, 2L);
        Manager manager = TestDataFactory.setUpTestManager1();
        ManagerDto dto = TestDataFactory.setUpTestManager1Dto();

        when(managerService.findByManagedBandIds(ids)).thenReturn(List.of(manager));
        when(managerMapper.toDto(manager)).thenReturn(dto);

        List<ManagerDto> result = managerFacadeImpl.findByBandIds(ids);

        assertEquals(1, result.size());
        assertEquals(dto, result.getFirst());
    }

    @Test
    void deleteById_validId_callsService() {
        Long id = 1L;

        managerFacadeImpl.deleteById(id);

        verify(managerService).deleteById(id);
    }

    @Test
    void updateBandIds_validInput_returnsUpdatedDto() {
        Manager manager = TestDataFactory.setUpTestManager1();
        ManagerDto dto = TestDataFactory.setUpTestManager1Dto();
        Set<Long> ids = Set.of(1L, 2L);

        when(managerService.updateManagerBandIds(manager.getId(), ids)).thenReturn(manager);
        when(managerMapper.toDto(manager)).thenReturn(dto);

        ManagerDto result = managerFacadeImpl.updateBandIds(manager.getId(), ids);

        assertEquals(dto, result);
    }
}
