package cz.muni.fi.userservice.repository;

import cz.muni.fi.userservice.TestDataFactory;
import cz.muni.fi.userservice.model.Manager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
public class ManagerRepositoryTest {

    @Autowired
    private ManagerRepository managerRepository;

    @BeforeEach
    void setUp() {
        managerRepository.deleteAll();
    }

    @Test
    void findByUsername_existingUsername_returnsManager() {
        // Arrange
        Manager manager = TestDataFactory.setUpTestManager1();
        manager.setId(null);
        managerRepository.save(manager);

        // Act
        Optional<Manager> foundManager = managerRepository.findByUsername(manager.getUsername());

        // Assert
        assertTrue(foundManager.isPresent());
        assertEquals(manager.getUsername(), foundManager.get().getUsername());
    }

    @Test
    void findByUsername_nonExistingUsername_returnsEmpty() {
        // Arrange
        Optional<Manager> foundManager = managerRepository.findByUsername("doesNotExist");

        // Act & Assert
        assertTrue(foundManager.isEmpty());
    }

    @Test
    void findByManagedBandIds_existingBandIds_returnsManagers() {
        // Arrange
        Manager manager1 = TestDataFactory.setUpTestManager1();
        manager1.setId(null);
        manager1.setManagedBandIds(Set.of(1L, 2L));
        managerRepository.save(manager1);

        Manager manager2 = TestDataFactory.setUpTestManager2();
        manager2.setId(null);
        manager2.setManagedBandIds(Set.of(3L, 4L));
        managerRepository.save(manager2);

        // Act
        List<Manager> foundManagers = managerRepository.findByManagedBandIds(Set.of(1L, 3L));

        // Assert
        assertEquals(2, foundManagers.size());
    }

    @Test
    void findByManagedBandIds_nonExistingBandIds_returnsEmpty() {
        // Arrange
        Manager manager1 = TestDataFactory.setUpTestManager1();
        manager1.setId(null);
        manager1.setManagedBandIds(Set.of(1L, 2L));
        managerRepository.save(manager1);

        // Act
        List<Manager> foundManagers = managerRepository.findByManagedBandIds(Set.of(3L, 4L));

        // Assert
        assertTrue(foundManagers.isEmpty());
    }
}
