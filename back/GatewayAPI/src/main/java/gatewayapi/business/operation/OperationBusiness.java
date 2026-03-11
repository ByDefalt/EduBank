package gatewayapi.business.operation;

import dto.operationapi.ChangeStateRequest;
import dto.operationapi.Operation;
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

    public OperationList getAllOperations(OperationState state, String dateFrom, String dateTo) {
        return this.operationRepository.getAllOperations(state, dateFrom, dateTo);
    }

    public OperationList getOperationsByAccountId(String accountId, OperationState state, String dateFrom, String dateTo) {
        return this.operationRepository.getOperationsByAccountId(accountId, state, dateFrom, dateTo);
    }

    public Operation createOperation(Operation operation) {
        return this.operationRepository.createOperation(operation);
    }

    public Operation getOperationById(Integer id) {
        return this.operationRepository.getOperationById(id);
    }

    public Operation updateOperationState(Integer id, ChangeStateRequest state) {
        return this.operationRepository.updateOperationState(id, state);
    }

    public Operation cancelOperation(Integer id) {
        return this.operationRepository.cancelOperation(id);
    }
}