package cz.muni.fi.userservice.rest;

import cz.muni.fi.userservice.TestDataFactory;
import cz.muni.fi.userservice.dto.ManagerDto;
import cz.muni.fi.userservice.facade.ManagerFacadeImpl;
import cz.muni.fi.userservice.model.Manager;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Objects;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

/**
 * @author Tomáš MAREK
 */
@ExtendWith(MockitoExtension.class)
public class ManagerControllerTest {
    @InjectMocks
    private ManagerController managerController;

    @Mock
    private ManagerFacadeImpl managerFacadeImpl;

    @Test
    void register_validRequest_returnsCreatedArtisWithOkStatus() {
        // Arrange
        ManagerDto managerDto = TestDataFactory.setUpTestManager1Dto();
        Mockito.when(managerFacadeImpl.register(managerDto)).thenReturn(managerDto);
        // Act
        ResponseEntity<ManagerDto> response = managerController.register(managerDto);

        // Assert
        assertThat(response.getStatusCode().value()).isEqualTo(201);
        assertThat(response.hasBody()).isTrue();
        assertThat(response.getBody()).isEqualTo(managerDto);
        verify(managerFacadeImpl, times(1)).register(any());
    }

    @Test
    void getAllManagers_twoManagersStored_returnsListWithOkStatus() {
        // Arrange
        ManagerDto managerDto = TestDataFactory.setUpTestManager1Dto();
        ManagerDto managerDto2 = TestDataFactory.setUpTestManager2Dto();
        Mockito.when(managerFacadeImpl.findAll()).thenReturn(List.of(managerDto, managerDto2));

        // Act
        ResponseEntity<List<ManagerDto>> response = managerController.getAllManagers();

        // Assert
        assertThat(response.getStatusCode().value()).isEqualTo(200);
        assertThat(response.hasBody()).isTrue();
        assertThat(Objects.requireNonNull(response.getBody()).contains(managerDto)).isTrue();
        assertThat(Objects.requireNonNull(response.getBody()).contains(managerDto2)).isTrue();
    }

    @Test
    void getById_validRequest_returnsManagerWithOkStatus() {
        // Arrange
        Manager manager = TestDataFactory.setUpTestManager1();
        ManagerDto managerDto = TestDataFactory.setUpTestManager1Dto();
        Mockito.when(managerFacadeImpl.findById(manager.getId())).thenReturn(managerDto);

        // Act
        ResponseEntity<ManagerDto> response = managerController.getById(manager.getId());

        // Assert
        assertThat(response.getStatusCode().value()).isEqualTo(200);
        assertThat(response.hasBody()).isTrue();
        assertThat(response.getBody()).isEqualTo(managerDto);
    }

    @Test
    void deleteById_validId_returnsEmptyEntityWithOkStatus() {
        // Act
        Manager manager = TestDataFactory.setUpTestManager1();
        ResponseEntity<Void> response = managerController.deleteById(manager.getId());

        // Assert
        assertThat(response.getStatusCode().value()).isEqualTo(204);
        assertThat(response.hasBody()).isFalse();
        verify(managerFacadeImpl, times(1)).deleteById(manager.getId());
    }

    @Test
    void updateBands_validOperation_returnsUpdatedManagerWithOkStatus() {
        // Arrange
        ManagerDto managerDto = TestDataFactory.setUpTestManager1Dto();
        Mockito.when(managerFacadeImpl.updateBandIds(managerDto.getId(), managerDto.getManagedBandIds()))
                .thenReturn(managerDto);

        // Act
        ResponseEntity<ManagerDto> response = managerController.updateBands(managerDto.getId(), managerDto.getManagedBandIds());

        // Assert
        assertThat(response.getStatusCode().value()).isEqualTo(200);
        assertThat(response.hasBody()).isTrue();
        assertThat(response.getBody()).isEqualTo(managerDto);
    }

    @Test
    void getManagersByBandIds_twoManagersMatch_returnsListWithOkStatus() {
        // Arrange
        ManagerDto managerDto = TestDataFactory.setUpTestManager1Dto();
        ManagerDto managerDto2 = TestDataFactory.setUpTestManager2Dto();
        Set<Long> bandIds = Set.of(2L, 3L);
        Mockito.when(managerFacadeImpl.findByBandIds(bandIds)).thenReturn(List.of(managerDto, managerDto2));

        // Act
        ResponseEntity<List<ManagerDto>> response = managerController.getManagersByBandIds(Set.of(2L, 3L));

        // Assert
        assertThat(response.getStatusCode().value()).isEqualTo(200);
        assertThat(response.hasBody()).isTrue();
        assertEquals(2, Objects.requireNonNull(response.getBody()).size());
        assertThat(Objects.requireNonNull(response.getBody()).contains(managerDto)).isTrue();
        assertThat(Objects.requireNonNull(response.getBody()).contains(managerDto2)).isTrue();
    }
}
