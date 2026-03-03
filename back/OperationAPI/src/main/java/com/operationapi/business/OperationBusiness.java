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

    public Operation save(Operation operation) {
        return this.operationRepository.save(operation);
    }
}
