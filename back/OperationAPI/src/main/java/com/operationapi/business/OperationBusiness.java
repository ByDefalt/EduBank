package com.operationapi.business;

import com.operationapi.repository.OperationRepository;
import dto.operationapi.Operation;

import java.util.List;

public class OperationBusiness {
    private final OperationRepository operationRepository;

    public OperationBusiness(OperationRepository operationRepository) {
        this.operationRepository = operationRepository;
    }

    public List<Operation> getOperations() {
        return this.operationRepository.getOperations();
    }

    public void save(Operation operation) {
        this.operationRepository.save(operation);
    }

    public Operation getOperationById(Integer id) {
        return this.operationRepository.getOperationById(id);
    }

    public Operation updateStateOperation(Integer id, Operation.StateEnum state) {
        boolean isUpdated = this.operationRepository.updateState(id, state);
        if(isUpdated) {
            return this.operationRepository.getOperationById(id);
        }
        return null;
    }

    public Operation cancelOperation(Integer id) {
        return this.updateStateOperation(id, Operation.StateEnum.CANCELLED);
    }
}
