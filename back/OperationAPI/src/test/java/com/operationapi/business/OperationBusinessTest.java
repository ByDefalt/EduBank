package com.operationapi.business;

import com.operationapi.entity.OperationEntity;
import com.operationapi.entity.StateEnumEntity;
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

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.isNull;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class OperationBusinessTest {

    @Mock
    private OperationRepository operationRepository;

    @InjectMocks
    private OperationBusiness operationBusiness;

    @Test
    void testGetOperations() {
        OperationEntity op = new OperationEntity(1, "ACC-1", "Virement", StateEnumEntity.PENDING, "FR7612345678901234567890123", 10.0, LocalDateTime.now());
        when(operationRepository.getOperations(isNull(), isNull(), isNull(), isNull())).thenReturn(List.of(op));

        OperationList result = operationBusiness.getOperations(null, null, null);

        assertEquals(1, result.getData().size());
        assertEquals(1, result.getData().get(0).getId());
    }

    @Test
    void testSaveOperationFunctionalExceptionOnAccountSourceId() {
        Operation operation = new Operation();
        operation.setLabel("Virement");
        operation.setIbanTarget("FR7612345678901234567890123");
        operation.setAmount(10.0);

        FunctionalException ex = assertThrows(FunctionalException.class, () -> operationBusiness.save(operation));

        assertEquals("400", ex.getCode());
    }

    @Test
    void testSaveOperationFunctionalExceptionOnLabel() {
        Operation operation = new Operation();
        operation.setAccountSourceId("ACC-1");
        operation.setIbanTarget("FR7612345678901234567890123");
        operation.setAmount(10.0);

        FunctionalException ex = assertThrows(FunctionalException.class, () -> operationBusiness.save(operation));

        assertEquals("400", ex.getCode());
    }

    @Test
    void testSaveOperationFunctionalExceptionOnIbanTarget() {
        Operation operation = new Operation();
        operation.setAccountSourceId("ACC-1");
        operation.setLabel("Virement");
        operation.setAmount(10.0);

        FunctionalException ex = assertThrows(FunctionalException.class, () -> operationBusiness.save(operation));

        assertEquals("400", ex.getCode());
    }

    @Test
    void testSaveOperationFunctionalExceptionOnAmount() {
        Operation operation = new Operation();
        operation.setAccountSourceId("ACC-1");
        operation.setLabel("Virement");
        operation.setIbanTarget("FR7612345678901234567890123");
        operation.setAmount(0.0);

        FunctionalException ex = assertThrows(FunctionalException.class, () -> operationBusiness.save(operation));

        assertEquals("400", ex.getCode());
    }

    @Test
    void testSaveOperationSuccess() {
        Operation operation = new Operation();
        operation.setAccountSourceId("ACC-1");
        operation.setLabel("Virement");
        operation.setIbanTarget("FR7612345678901234567890123");
        operation.setAmount(10.0);

        OperationEntity saved = new OperationEntity(1, "ACC-1", "Virement", StateEnumEntity.PENDING, "FR7612345678901234567890123", 10.0, LocalDateTime.now());

        when(operationRepository.save(any(OperationEntity.class))).thenReturn(saved);

        Operation result = operationBusiness.save(operation);

        assertEquals(1, result.getId());
        assertEquals("ACC-1", result.getAccountSourceId());
        assertEquals(OperationState.PENDING, result.getState());
    }

    @Test
    void testGetOperationById() {
        OperationEntity op = new OperationEntity(1, "ACC-1", "Virement", StateEnumEntity.PENDING, "FR7612345678901234567890123", 10.0, LocalDateTime.now());
        when(operationRepository.getOperationById(1)).thenReturn(op);

        Operation result = operationBusiness.getOperationById(1);

        assertEquals(1, result.getId());
        assertEquals("Virement", result.getLabel());
    }

    @Test
    void testUpdateStateOperationStateNull() {
        FunctionalException ex = assertThrows(FunctionalException.class, () -> operationBusiness.updateStateOperation(1, null));

        assertEquals("400", ex.getCode());
    }

    @Test
    void testUpdateStateOperationFunctionalExceptionOnCancelledOperation() {
        OperationEntity cancelledOp = new OperationEntity(1, "ACC-1", "Virement", StateEnumEntity.CANCELLED, "FR7612345678901234567890123", 10.0, LocalDateTime.now());
        when(operationRepository.getOperationById(1)).thenReturn(cancelledOp);

        FunctionalException ex = assertThrows(FunctionalException.class, () -> operationBusiness.updateStateOperation(1, OperationState.COMPLETED));

        assertEquals("400", ex.getCode());
    }

    @Test
    void testUpdateStateOperationSuccess() {
        OperationEntity op = new OperationEntity(1, "ACC-1", "Virement", StateEnumEntity.PENDING, "FR7612345678901234567890123", 10.0, LocalDateTime.now());
        OperationEntity updatedOp = new OperationEntity(1, "ACC-1", "Virement", StateEnumEntity.COMPLETED, "FR7612345678901234567890123", 10.0, LocalDateTime.now());

        when(operationRepository.getOperationById(1)).thenReturn(op).thenReturn(updatedOp);

        Operation result = operationBusiness.updateStateOperation(1, OperationState.COMPLETED);

        assertEquals(1, result.getId());
        assertEquals(OperationState.COMPLETED, result.getState());
    }

    @Test
    void testCancelOperationFunctionalExceptionOnAlreadyCancelledOperation() {
        OperationEntity cancelledOp = new OperationEntity(1, "ACC-1", "Virement", StateEnumEntity.CANCELLED, "FR7612345678901234567890123", 10.0, LocalDateTime.now());
        when(operationRepository.getOperationById(1)).thenReturn(cancelledOp);

        FunctionalException ex = assertThrows(FunctionalException.class, () -> operationBusiness.cancelOperation(1));

        assertEquals("400", ex.getCode());
    }

    @Test
    void testCancelOperationSuccess() {
        OperationEntity original = new OperationEntity(1, "ACC-1", "Virement", StateEnumEntity.PENDING, "FR7612345678901234567890123", 10.0, LocalDateTime.now());
        OperationEntity cancellation = new OperationEntity(2, "ACC-1", "ANNULATION - Virement", StateEnumEntity.COMPLETED, "FR7612345678901234567890123", -10.0, LocalDateTime.now());

        when(operationRepository.getOperationById(1)).thenReturn(original);
        when(operationRepository.save(any(OperationEntity.class))).thenReturn(cancellation);

        Operation result = operationBusiness.cancelOperation(1);

        assertNotNull(result);
        assertEquals(2, result.getId());
        assertEquals("ANNULATION - Virement", result.getLabel());
        assertEquals(-10.0, result.getAmount());
    }
}