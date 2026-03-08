package gatewayapi.repository.operation;

import dto.operationapi.Operation;
import dto.operationapi.OperationFilter;
import dto.operationapi.OperationList;
import dto.operationapi.OperationState;
import gatewayapi.client.OperationClient;
import org.springframework.stereotype.Service;

@Service
public class OperationRepository {

    private final OperationClient operationClient;

    public OperationRepository(OperationClient operationClient) {
        this.operationClient = operationClient;
    }

    public OperationList getAllOperations(OperationFilter filter) {
        return this.operationClient.getAllOperations(filter);
    }

    public OperationList getOperationsByAccountId(String accountId, OperationFilter filter) {
        return this.operationClient.getOperationsByAccountId(accountId, filter);
    }

    public Operation createOperation(Operation operation) {
        return this.operationClient.createOperation(operation);
    }

    public Operation getOperationById(Integer id) {
        return this.operationClient.getOperationById(id);
    }

    public Operation updateOperationState(Integer id, OperationState state) {
        return this.operationClient.updateOperationState(id, state);
    }

    public Operation cancelOperation(Integer id) {
        return this.operationClient.cancelOperation(id);
    }
}
