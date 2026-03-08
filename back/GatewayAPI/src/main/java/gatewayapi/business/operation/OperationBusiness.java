package gatewayapi.business.operation;

import dto.operationapi.Operation;
import dto.operationapi.OperationFilter;
import dto.operationapi.OperationList;
import dto.operationapi.OperationState;
import gatewayapi.repository.operation.OperationRepository;
import org.springframework.stereotype.Service;

@Service
public class OperationBusiness {

    private final OperationRepository operationRepository;

    public OperationBusiness(OperationRepository operationRepository) {
        this.operationRepository = operationRepository;
    }

    public OperationList getAllOperations(OperationFilter filter) {
        return this.operationRepository.getAllOperations(filter);
    }

    public OperationList getOperationsByAccountId(String accountId, OperationFilter filter) {
        return this.operationRepository.getOperationsByAccountId(accountId, filter);
    }

    public Operation createOperation(Operation operation) {
        return this.operationRepository.createOperation(operation);
    }

    public Operation getOperationById(Integer id) {
        return this.operationRepository.getOperationById(id);
    }

    public Operation updateOperationState(Integer id, OperationState state) {
        return this.operationRepository.updateOperationState(id, state);
    }

    public Operation cancelOperation(Integer id) {
        return this.operationRepository.cancelOperation(id);
    }
}
