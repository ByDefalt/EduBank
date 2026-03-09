package gatewayapi.repository.bankaccount;

import dto.bankapiswagger.*;
import gatewayapi.client.BankAccountClient;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class BankAccountPivotRepository {

    private final BankAccountClient bankAccountClient;

    public BankAccountPivotRepository(BankAccountClient bankAccountClient) {
        this.bankAccountClient = bankAccountClient;
    }

    // ==================== ADMIN ====================

    public void createPivot(BankAccountPivot pivot) {
        bankAccountClient.createPivot(pivot);
    }

    public void deletePivot(BankAccountPivot pivot) {
        bankAccountClient.deletePivot(pivot);
    }

    public List<BankAccountPivot> getPivotsByBankAccount(String bankAccountId) {
        return bankAccountClient.getPivotsByBankAccount(bankAccountId);
    }

    public List<BankAccountPivot> getPivotsByAccount(String accountId) {
        return bankAccountClient.getPivotsByAccount(accountId);
    }

    public void deleteAllPivotsByBankAccount(String bankAccountId) {
        bankAccountClient.deleteAllPivotsByBankAccount(bankAccountId);
    }

    public void deleteAllPivotsByAccount(String accountId) {
        bankAccountClient.deleteAllPivotsByAccount(accountId);
    }
}