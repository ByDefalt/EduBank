package com.operationapi.business;

import com.operationapi.exception.FunctionalException;
import com.operationapi.repository.OperationRepository;
import dto.operationapi.Operation;
import dto.operationapi.OperationFilter;
import dto.operationapi.OperationList;
import dto.operationapi.OperationState;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.Map;

@Service
public class OperationBusiness {
    private final OperationRepository operationRepository;

    public OperationBusiness(OperationRepository operationRepository) {
        this.operationRepository = operationRepository;
    }

    public OperationList getOperations(OperationFilter filter) {
        OperationList operationList = new OperationList();
        operationList.setData(this.operationRepository.getOperations(null, filter));
        return operationList;
    }

    public OperationList getOperationsByAccountId(String accountId, OperationFilter filter) {
        OperationList operationList = new OperationList();
        operationList.setData(this.operationRepository.getOperations(accountId, filter));
        return operationList;
    }

    public Operation save(Operation operation) {
        if (operation.getAccountSourceId() == null || operation.getAccountSourceId().isBlank()) {
            throw new FunctionalException("400", "Le champ 'account_source_id' est obligatoire");
        }
        if (operation.getLabel() == null || operation.getLabel().isBlank()) {
            throw new FunctionalException("400", "Le champ 'label' est obligatoire");
        }
        if (operation.getIbanTarget() == null || operation.getIbanTarget().isBlank()) {
            throw new FunctionalException("400", "Le champ 'iban_target' est obligatoire");
        }
        if (operation.getAmount() == null || operation.getAmount() < 0.01) {
            throw new FunctionalException("400", "Le champ 'amount' doit être supérieur à 0");
        }

        operation.setState(OperationState.PENDING);
        if (operation.getDate() == null) {
            operation.setDate(OffsetDateTime.now(ZoneOffset.UTC));
        }

        return this.operationRepository.save(operation);
    }

    public Operation getOperationById(Integer id) {
        return this.operationRepository.getOperationById(id);
    }

    public Operation updateStateOperation(Integer id, OperationState state) {
        if (state == null) {
            throw new FunctionalException("400", "Le champ 'state' est obligatoire");
        }
        Operation existing = this.operationRepository.getOperationById(id);
        if (OperationState.CANCELLED.equals(existing.getState())) {
            throw new FunctionalException("400", "Impossible de modifier l'état d'une opération annulée");
        }
        this.operationRepository.updateState(id, state);
        return this.operationRepository.getOperationById(id);
    }

    public Map<String, Operation> cancelOperation(Integer id) {
        Operation original = this.operationRepository.getOperationById(id);

        if (OperationState.CANCELLED.equals(original.getState())) {
            throw new FunctionalException("400", "L'opération est déjà annulée");
        }

        this.operationRepository.updateState(id, OperationState.CANCELLED);
        Operation updatedOriginal = this.operationRepository.getOperationById(id);

        Operation cancellation = new Operation();
        cancellation.setAccountSourceId(original.getAccountSourceId());
        cancellation.setLabel("ANNULATION - " + original.getLabel());
        cancellation.setState(OperationState.COMPLETED);
        cancellation.setIbanTarget(original.getIbanTarget());
        cancellation.setAmount(-original.getAmount());
        cancellation.setDate(OffsetDateTime.now(ZoneOffset.UTC));
        Operation savedCancellation = this.operationRepository.save(cancellation);

        return Map.of("original_operation", updatedOriginal, "cancellation_operation", savedCancellation);
    }
}
