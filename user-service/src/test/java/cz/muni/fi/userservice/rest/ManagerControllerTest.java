package cz.muni.fi.userservice.rest;

import cz.muni.fi.userservice.TestDataFactory;
import cz.muni.fi.userservice.dto.ManagerDto;
import cz.muni.fi.userservice.facade.ManagerFacade;
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
    private ManagerFacade managerFacade;

    @Test
    void register_validRequest_returnsCreatedArtisWithOkStatus() {
        // Arrange
        Mockito.when(managerFacade.register(TestDataFactory.TEST_MANAGER_1_DTO)).thenReturn(TestDataFactory.TEST_MANAGER_1_DTO);
        // Act
        ResponseEntity<ManagerDto> response = managerController.register(TestDataFactory.TEST_MANAGER_1_DTO);

        // Assert
        assertThat(response.getStatusCode().value()).isEqualTo(201);
        assertThat(response.hasBody()).isTrue();
        assertThat(response.getBody()).isEqualTo(TestDataFactory.TEST_MANAGER_1_DTO);
        verify(managerFacade, times(1)).register(any());
    }

    @Test
    void getAllManagers_twoManagersStored_returnsListWithOkStatus() {
        // Arrange
        Mockito.when(managerFacade.findAll()).thenReturn(List.of(TestDataFactory.TEST_MANAGER_1_DTO, TestDataFactory.TEST_MANAGER_2_DTO));

        // Act
        ResponseEntity<List<ManagerDto>> response = managerController.getAllManagers();

        // Assert
        assertThat(response.getStatusCode().value()).isEqualTo(200);
        assertThat(response.hasBody()).isTrue();
        assertThat(Objects.requireNonNull(response.getBody()).contains(TestDataFactory.TEST_MANAGER_1_DTO)).isTrue();
        assertThat(Objects.requireNonNull(response.getBody()).contains(TestDataFactory.TEST_MANAGER_2_DTO)).isTrue();
    }

    @Test
    void getById_validRequest_returnsManagerWithOkStatus() {
        // Arrange
        Mockito.when(managerFacade.findById(TestDataFactory.TEST_MANAGER_1.getId())).thenReturn(TestDataFactory.TEST_MANAGER_1_DTO);

        // Act
        ResponseEntity<ManagerDto> response = managerController.getById(TestDataFactory.TEST_MANAGER_1.getId());

        // Assert
        assertThat(response.getStatusCode().value()).isEqualTo(200);
        assertThat(response.hasBody()).isTrue();
        assertThat(response.getBody()).isEqualTo(TestDataFactory.TEST_MANAGER_1_DTO);
    }

    @Test
    void deleteById_validId_returnsEmptyEntityWithOkStatus() {
        // Act
        ResponseEntity<Void> response = managerController.deleteById(TestDataFactory.TEST_MANAGER_1.getId());

        // Assert
        assertThat(response.getStatusCode().value()).isEqualTo(204);
        assertThat(response.hasBody()).isFalse();
        verify(managerFacade, times(1)).deleteById(TestDataFactory.TEST_MANAGER_1.getId());
    }

    @Test
    void updateBands_validOperation_returnsUpdatedManagerWithOkStatus() {
        // Arrange
        Mockito.when(managerFacade.updateBandIds(TestDataFactory.TEST_MANAGER_1_DTO.getId(), TestDataFactory.TEST_MANAGER_1_DTO.getManagedBandIds()))
                .thenReturn(TestDataFactory.TEST_MANAGER_1_DTO);

        // Act
        ResponseEntity<ManagerDto> response = managerController.updateBands(TestDataFactory.TEST_MANAGER_1_DTO.getId(), TestDataFactory.TEST_MANAGER_1_DTO.getManagedBandIds());

        // Assert
        assertThat(response.getStatusCode().value()).isEqualTo(200);
        assertThat(response.hasBody()).isTrue();
        assertThat(response.getBody()).isEqualTo(TestDataFactory.TEST_MANAGER_1_DTO);
    }

    @Test
    void getManagersByBandIds_twoManagersMatch_returnsListWithOkStatus() {
        // Arrange
        Set<Long> bandIds = Set.of(2L, 3L);
        Mockito.when(managerFacade.findByBandIds(bandIds)).thenReturn(List.of(TestDataFactory.TEST_MANAGER_1_DTO, TestDataFactory.TEST_MANAGER_2_DTO));

        // Act
        ResponseEntity<List<ManagerDto>> response = managerController.getManagersByBandIds(Set.of(2L, 3L));

        // Assert
        assertThat(response.getStatusCode().value()).isEqualTo(200);
        assertThat(response.hasBody()).isTrue();
        assertEquals(2, Objects.requireNonNull(response.getBody()).size());
        assertThat(Objects.requireNonNull(response.getBody()).contains(TestDataFactory.TEST_MANAGER_1_DTO)).isTrue();
        assertThat(Objects.requireNonNull(response.getBody()).contains(TestDataFactory.TEST_MANAGER_2_DTO)).isTrue();
    }
}
