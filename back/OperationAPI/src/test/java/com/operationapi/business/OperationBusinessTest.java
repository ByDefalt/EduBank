package com.operationapi.business;

import com.operationapi.exception.FunctionalException;
import com.operationapi.repository.OperationRepository;
import dto.operationapi.Operation;
import dto.operationapi.OperationList;
import dto.operationapi.OperationState;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class OperationBusinessTest {

    @Mock
    private OperationRepository operationRepository;

    @InjectMocks
    private OperationBusiness operationBusiness;

    @Test
    void testGetOperations() {
        Operation op = new Operation();
        op.setId(1);
        when(operationRepository.getOperations()).thenReturn(List.of(op));

        OperationList result = operationBusiness.getOperations();

        assertEquals(1, result.getData().size());
        assertEquals(1, result.getData().get(0).getId());
   }

    @Test
    void testSaveOperationFunctionalExceptionOnAccountSourceId() {
        Operation operation = validOperation();
        operation.setAccountSourceId(" ");

        FunctionalException ex = assertThrows(FunctionalException.class, () -> operationBusiness.save(operation));

        assertEquals("400", ex.getCode());
        assertTrue(ex.getMessage().contains("account_source_id"));
    }

    @Test
    void testSaveOperationFunctionalExceptionOnLabel() {
        Operation operation = validOperation();
        operation.setLabel("");

        FunctionalException ex = assertThrows(FunctionalException.class, () -> operationBusiness.save(operation));

        assertEquals("400", ex.getCode());
        assertTrue(ex.getMessage().contains("label"));
    }

    @Test
    void testSaveOperationFunctionalExceptionOnIbanTarget() {
        Operation operation = validOperation();
        operation.setIbanTarget(" ");

        FunctionalException ex = assertThrows(FunctionalException.class, () -> operationBusiness.save(operation));

        assertEquals("400", ex.getCode());
        assertTrue(ex.getMessage().contains("iban_target"));
    }

    @Test
    void testSaveOperationFunctionalExceptionOnAmount() {
        Operation operation = validOperation();
        operation.setAmount(0.0);

        FunctionalException ex = assertThrows(FunctionalException.class, () -> operationBusiness.save(operation));

        assertEquals("400", ex.getCode());
        assertTrue(ex.getMessage().contains("amount"));
    }

    @Test
    void testSaveOperationFunctionalExceptionOnDate() {
        Operation operation = validOperation();
        operation.setDate(null);

        when(operationRepository.save(any(Operation.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Operation result = operationBusiness.save(operation);

        assertEquals(OperationState.PENDING, result.getState());
        assertNotNull(result.getDate());
    }

    @Test
    void testGetOperationById() {
        Operation operation = validOperation();
        operation.setId(12);
        when(operationRepository.getOperationById(12)).thenReturn(operation);

        Operation result = operationBusiness.getOperationById(12);

        assertEquals(12, result.getId());
    }

    @Test
    void testUpdateStateOperationStateNull() {
        FunctionalException ex = assertThrows(FunctionalException.class, () -> operationBusiness.updateStateOperation(1, null));

        assertEquals("400", ex.getCode());
        assertTrue(ex.getMessage().contains("state"));
    }

    @Test
    void testUpdateStateOperationFunctionalExceptionOnCancelledOperation() {
        Operation cancelled = validOperation();
        cancelled.setState(OperationState.CANCELLED);
        when(operationRepository.getOperationById(2)).thenReturn(cancelled);

        FunctionalException ex = assertThrows(
            FunctionalException.class,
            () -> operationBusiness.updateStateOperation(2, OperationState.COMPLETED)
        );

        assertEquals("400", ex.getCode());
        assertTrue(ex.getMessage().contains("annul"));
    }

    @Test
    void testUpdateStateOperationSuccess() {
        Operation initial = validOperation();
        initial.setState(OperationState.PENDING);
        Operation updated = validOperation();
        updated.setState(OperationState.COMPLETED);

        when(operationRepository.getOperationById(3)).thenReturn(initial, updated);

        Operation result = operationBusiness.updateStateOperation(3, OperationState.COMPLETED);

        assertEquals(OperationState.COMPLETED, result.getState());
    }

    @Test
    void testCancelOperationFunctionalExceptionOnAlreadyCancelledOperation() {
        Operation cancelled = validOperation();
        cancelled.setState(OperationState.CANCELLED);
        when(operationRepository.getOperationById(4)).thenReturn(cancelled);

        FunctionalException ex = assertThrows(FunctionalException.class, () -> operationBusiness.cancelOperation(4));

        assertEquals("400", ex.getCode());
        assertTrue(ex.getMessage().contains("déjà annulée"));
    }

    @Test
    void testCancelOperationSuccess() {
        Operation original = validOperation();
        original.setId(10);
        original.setState(OperationState.PENDING);
        original.setAmount(25.5);
        original.setLabel("Paiement test");

        Operation updatedOriginal = validOperation();
        updatedOriginal.setId(10);
        updatedOriginal.setState(OperationState.CANCELLED);

        when(operationRepository.getOperationById(10)).thenReturn(original, updatedOriginal);
        when(operationRepository.save(any(Operation.class))).thenAnswer(invocation -> {
            Operation op = invocation.getArgument(0);
            op.setId(99);
            return op;
        });

        Map<String, Operation> result = operationBusiness.cancelOperation(10);

        assertEquals(OperationState.CANCELLED, result.get("original_operation").getState());
        Operation cancellation = result.get("cancellation_operation");
        assertEquals(OperationState.COMPLETED, cancellation.getState());
        assertEquals(-25.5, cancellation.getAmount());
        assertTrue(cancellation.getLabel().startsWith("ANNULATION - "));
        assertNotNull(cancellation.getDate());
    }

    private Operation validOperation() {
        Operation operation = new Operation();
        operation.setAccountSourceId("ACC-1");
        operation.setLabel("Virement");
        operation.setIbanTarget("FR7612345678901234567890123");
        operation.setAmount(10.0);
        operation.setState(OperationState.PENDING);
        operation.setDate(OffsetDateTime.now());
        return operation;
    }
}
