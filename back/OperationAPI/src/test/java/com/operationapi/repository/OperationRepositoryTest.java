package com.operationapi.repository;

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

    private Operation operation;

    @BeforeEach
    void setUp() {
        operation = new Operation();
        operation.setAccountSourceId("ACC-1");
        operation.setLabel("Virement loyer");
        operation.setIbanTarget("FR7612345678901234567890123");
        operation.setAmount(850.00);
        operation.setState(OperationState.PENDING);
        operation.setDate(OffsetDateTime.of(2026, 1, 15, 10, 0, 0, 0, ZoneOffset.UTC));
    }

    @Test
    void testSaveOperation() {
        Operation saved = operationRepository.save(operation);

        assertNotNull(saved);
        assertNotNull(saved.getId());
        assertTrue(saved.getId() > 0);
        assertEquals("Virement loyer", saved.getLabel());
        assertEquals(850.00, saved.getAmount());
        assertEquals(OperationState.PENDING, saved.getState());
    }

    @Test
    void testGetOperations() {
        operationRepository.save(operation);

        List<Operation> result = operationRepository.getOperations(null);

        assertFalse(result.isEmpty());
        assertEquals(1, result.size());
        assertEquals("Virement loyer", result.get(0).getLabel());
    }

    @Test
    void testGetOperationById() {
        Operation saved = operationRepository.save(operation);
        int id = saved.getId();

        Operation found = operationRepository.getOperationById(id);

        assertNotNull(found);
        assertEquals(id, found.getId());
        assertEquals("Virement loyer", found.getLabel());
        assertEquals(850.00, found.getAmount());
        assertEquals(OperationState.PENDING, found.getState());
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
        Operation saved = operationRepository.save(operation);
        int id = saved.getId();

        operationRepository.updateState(id, OperationState.COMPLETED);

        Operation updated = operationRepository.getOperationById(id);
        assertEquals(OperationState.COMPLETED, updated.getState());
    }

    @Test
    void testUpdateStateToCancelled() {
        Operation saved = operationRepository.save(operation);
        int id = saved.getId();

        operationRepository.updateState(id, OperationState.CANCELLED);

        Operation updated = operationRepository.getOperationById(id);
        assertEquals(OperationState.CANCELLED, updated.getState());
    }

    @Test
    void testSaveMultipleOperations() {
        operationRepository.save(operation);

        Operation op2 = new Operation();
        op2.setAccountSourceId("ACC-2");
        op2.setLabel("Facture EDF");
        op2.setIbanTarget("FR7699999999999999999999999");
        op2.setAmount(120.50);
        op2.setState(OperationState.COMPLETED);
        op2.setDate(OffsetDateTime.of(2026, 2, 1, 9, 0, 0, 0, ZoneOffset.UTC));
        operationRepository.save(op2);

        List<Operation> result = operationRepository.getOperations(null);
        assertEquals(2, result.size());
    }

    @Test
    void testGetOperationsFilterByAccountSourceId() {
        operationRepository.save(operation);

        Operation op2 = new Operation();
        op2.setAccountSourceId("ACC-2");
        op2.setLabel("Facture EDF");
        op2.setIbanTarget("FR7699999999999999999999999");
        op2.setAmount(120.50);
        op2.setState(OperationState.COMPLETED);
        op2.setDate(OffsetDateTime.of(2026, 2, 1, 9, 0, 0, 0, ZoneOffset.UTC));
        operationRepository.save(op2);

        OperationFilter filter = new OperationFilter();
        filter.setAccountSourceId("ACC-1");
        List<Operation> result = operationRepository.getOperations(filter);

        assertEquals(1, result.size());
        assertEquals("ACC-1", result.get(0).getAccountSourceId());
    }

    @Test
    void testGetOperationsFilterByState() {
        operationRepository.save(operation); // PENDING

        Operation op2 = new Operation();
        op2.setAccountSourceId("ACC-1");
        op2.setLabel("Facture EDF");
        op2.setIbanTarget("FR7699999999999999999999999");
        op2.setAmount(50.00);
        op2.setState(OperationState.COMPLETED);
        op2.setDate(OffsetDateTime.of(2026, 2, 1, 9, 0, 0, 0, ZoneOffset.UTC));
        operationRepository.save(op2);

        OperationFilter filter = new OperationFilter();
        filter.setState(OperationState.COMPLETED);
        List<Operation> result = operationRepository.getOperations(filter);

        assertEquals(1, result.size());
        assertEquals(OperationState.COMPLETED, result.get(0).getState());
    }

    @Test
    void testGetOperationsFilterByDateRange() {
        operationRepository.save(operation); // 2026-01-15

        Operation op2 = new Operation();
        op2.setAccountSourceId("ACC-1");
        op2.setLabel("Virement mars");
        op2.setIbanTarget("FR7699999999999999999999999");
        op2.setAmount(200.00);
        op2.setState(OperationState.PENDING);
        op2.setDate(OffsetDateTime.of(2026, 3, 1, 9, 0, 0, 0, ZoneOffset.UTC));
        operationRepository.save(op2);

        OperationFilter filter = new OperationFilter();
        filter.setDateFrom(OffsetDateTime.of(2026, 2, 1, 0, 0, 0, 0, ZoneOffset.UTC));
        filter.setDateTo(OffsetDateTime.of(2026, 3, 31, 23, 59, 59, 0, ZoneOffset.UTC));
        List<Operation> result = operationRepository.getOperations(filter);

        assertEquals(1, result.size());
        assertEquals("Virement mars", result.get(0).getLabel());
    }
}

