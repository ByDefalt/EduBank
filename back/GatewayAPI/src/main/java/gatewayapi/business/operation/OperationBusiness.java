package gatewayapi.business.operation;

import dto.bankapiswagger.BankAccount;
import dto.bankapiswagger.BankAccountDetail;
import dto.operationapi.Operation;
import dto.operationapi.OperationList;
import dto.operationapi.OperationState;
import feign.FeignException;
import gatewayapi.exception.FunctionalException;
import gatewayapi.mapper.BankAccountMapper;
import gatewayapi.repository.bankaccount.BankAccountRepository;
import gatewayapi.repository.operation.OperationRepository;
import org.springframework.stereotype.Service;

@Service
public class OperationBusiness {

    private final OperationRepository operationRepository;
    private final BankAccountRepository bankAccountRepository;

    public OperationBusiness(OperationRepository operationRepository, BankAccountRepository bankAccountRepository) {
        this.operationRepository = operationRepository;
        this.bankAccountRepository = bankAccountRepository;
    }

    public OperationList getAllOperations(OperationState state, String dateFrom, String dateTo) {
        return this.operationRepository.getAllOperations(state, dateFrom, dateTo);
    }

    public OperationList getOperationsByAccountId(String accountId, OperationState state, String dateFrom, String dateTo) {
        return this.operationRepository.getOperationsByAccountId(accountId, state, dateFrom, dateTo);
    }

    public Operation createOperation(Operation operation) {
        Operation resultOperation = this.operationRepository.createOperation(operation);

        return applyOperation(resultOperation);
    }

    public Operation getOperationById(Integer id) {
        return this.operationRepository.getOperationById(id);
    }

    public Operation updateOperationState(Integer id, OperationState state) {
        return this.operationRepository.updateOperationState(id, state);
    }

    public Operation cancelOperation(Integer id) {
        Operation resultOperation = this.operationRepository.cancelOperation(id);

        return applyOperation(resultOperation);
    }

    private Operation applyOperation(Operation operation) {
        try {
            BankAccountDetail bankAccountSource = this.bankAccountRepository.findById(operation.getAccountSourceId());
            BankAccount bankAccountTarget = this.bankAccountRepository.findByIban(operation.getIbanTarget());

            bankAccountSource.setSold(bankAccountSource.getSold() - operation.getAmount());
            bankAccountTarget.setSold(bankAccountTarget.getSold() + operation.getAmount());

            if(bankAccountSource.getSold() < -bankAccountSource.getParameter().getOverdraftLimit()) {
                operation = this.operationRepository.updateOperationState(operation.getId(), OperationState.FAILED);
                throw new FunctionalException("422", "Le compte source n'a pas assez de fonds pour effectuer l'opération.");
            } else {
                this.bankAccountRepository.update(bankAccountSource.getId(), BankAccountMapper.toBankAccount(bankAccountSource));
                this.bankAccountRepository.update(bankAccountTarget.getId(), bankAccountTarget);

                operation = this.operationRepository.updateOperationState(operation.getId(), OperationState.COMPLETED);
            }
        } catch (FeignException exception) {
            this.operationRepository.updateOperationState(operation.getId(), OperationState.FAILED);
            throw exception;
        }

        return operation;
    }
}