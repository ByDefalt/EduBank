package com.operationapi.repository;

import com.operationapi.entity.OperationEntity;
import com.operationapi.entity.OperationFilterEntity;
import com.operationapi.entity.StateEnumEntity;
import com.operationapi.exception.NotFoundException;
import dto.operationapi.Operation;
import dto.operationapi.OperationFilter;
import dto.operationapi.OperationState;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@JdbcTest
@ActiveProfiles("test")
@Import(OperationRepository.class)
class OperationRepositoryTest {

    @Autowired
    private OperationRepository operationRepository;

    private OperationEntity operation;

    @BeforeEach
    void setUp() {
        operation = new OperationEntity(
                null,
                "ACC-1",
                "Virement loyer",
                StateEnumEntity.PENDING,
                "FR7612345678901234567890123",
                850.00,
                LocalDateTime.of(2026, 1, 15, 10, 0, 0)
        );
    }

    @Test
    void testSaveOperation() {
        OperationEntity saved = operationRepository.save(operation);

        assertNotNull(saved);
        assertNotNull(saved.id());
        assertTrue(saved.id() > 0);
        assertEquals("Virement loyer", saved.label());
        assertEquals(850.00, saved.amount());
        assertEquals(StateEnumEntity.PENDING, saved.state());
    }

    @Test
    void testGetOperations() {
        operationRepository.save(operation);

        List<OperationEntity> result = operationRepository.getOperations(null, null);

        assertFalse(result.isEmpty());
        assertEquals(1, result.size());
        assertEquals("Virement loyer", result.get(0).label());
    }

    @Test
    void testGetOperationById() {
        OperationEntity saved = operationRepository.save(operation);
        int id = saved.id();

        OperationEntity found = operationRepository.getOperationById(id);

        assertNotNull(found);
        assertEquals(id, found.id());
        assertEquals("Virement loyer", found.label());
        assertEquals(850.00, found.amount());
        assertEquals(StateEnumEntity.PENDING, found.state());
    }

    @Test
    void testGetOperationByIdThrowsNotFoundException() {
        NotFoundException ex = assertThrows(
                NotFoundException.class,
                () -> operationRepository.getOperationById(9999)
        );

        assertEquals("404", ex.getCode());
        assertTrue(ex.getMessage().contains("9999"));
    }

    @Test
    void testUpdateStateOperation() {
        OperationEntity saved = operationRepository.save(operation);
        int id = saved.id();

        operationRepository.updateState(id, OperationState.COMPLETED);

        OperationEntity updated = operationRepository.getOperationById(id);
        assertEquals(StateEnumEntity.COMPLETED, updated.state());
    }

    @Test
    void testUpdateStateToCancelled() {
        OperationEntity saved = operationRepository.save(operation);
        int id = saved.id();

        operationRepository.updateState(id, OperationState.CANCELLED);

        OperationEntity updated = operationRepository.getOperationById(id);
        assertEquals(StateEnumEntity.CANCELLED, updated.state());
    }

    @Test
    void testSaveMultipleOperations() {
        operationRepository.save(operation);

        OperationEntity op2 = new OperationEntity(
                null,
                "ACC-2",
                "Facture EDF",
                StateEnumEntity.COMPLETED,
                "FR7699999999999999999999999",
                120.50,
                LocalDateTime.of(2026, 2, 1, 9, 0, 0)
        );
        operationRepository.save(op2);

        List<OperationEntity> result = operationRepository.getOperations(null, null);
        assertEquals(2, result.size());
    }

    @Test
    void testGetOperationsFilterByAccountSourceId() {
        operationRepository.save(operation);

        OperationEntity op2 = new OperationEntity(
                null,
                "ACC-2",
                "Facture EDF",
                StateEnumEntity.COMPLETED,
                "FR7699999999999999999999999",
                120.50,
                LocalDateTime.of(2026, 2, 1, 9, 0, 0)
        );
        operationRepository.save(op2);

        OperationFilterEntity filter = new OperationFilterEntity(null, null, null);
        List<OperationEntity> result = operationRepository.getOperations("ACC-1", filter);

        assertEquals(1, result.size());
        assertEquals("ACC-1", result.get(0).accountSourceId());
    }

    @Test
    void testGetOperationsFilterByState() {
        operationRepository.save(operation); // PENDING

        OperationEntity op2 = new OperationEntity(
                null,
                "ACC-1",
                "Facture EDF",
                StateEnumEntity.COMPLETED,
                "FR7699999999999999999999999",
                50.00,
                LocalDateTime.of(2026, 2, 1, 9, 0, 0)
        );
        operationRepository.save(op2);

        OperationFilterEntity filter = new OperationFilterEntity(StateEnumEntity.COMPLETED, null, null);
        List<OperationEntity> result = operationRepository.getOperations(null, filter);

        assertEquals(1, result.size());
        assertEquals(StateEnumEntity.COMPLETED, result.get(0).state());
    }

    @Test
    void testGetOperationsFilterByDateRange() {
        operationRepository.save(operation); // 2026-01-15

        OperationEntity op2 = new OperationEntity(
                null,
                "ACC-1",
                "Virement mars",
                StateEnumEntity.PENDING,
                "FR7699999999999999999999999",
                200.00,
                LocalDateTime.of(2026, 3, 1, 9, 0, 0)
        );
        operationRepository.save(op2);

        OperationFilterEntity filter = new OperationFilterEntity(
                null,
                OffsetDateTime.of(2026, 2, 1, 0, 0, 0, 0, ZoneOffset.UTC),
                OffsetDateTime.of(2026, 3, 31, 23, 59, 59, 0, ZoneOffset.UTC)
        );
        List<OperationEntity> result = operationRepository.getOperations(null, filter);

        assertEquals(1, result.size());
        assertEquals("Virement mars", result.get(0).label());
    }
}

