package gatewayapi.repository.bankaccount;

import dto.bankapiswagger.*;
import gatewayapi.client.BankAccountClient;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class BankAccountRepository {

    private final BankAccountClient bankAccountClient;

    public BankAccountRepository(BankAccountClient bankAccountClient) {
        this.bankAccountClient = bankAccountClient;
    }

    // ==================== ADMIN ====================

    public List<BankAccount> findAll() {
        return bankAccountClient.getAllBankAccounts();
    }

    public BankAccountDetail findById(String id) {
        return bankAccountClient.getBankAccountById(id);
    }

    public void delete(String id) {
        bankAccountClient.deleteBankAccount(id);
    }

    public List<BankAccount> findByAccountId(String accountId) {
        return bankAccountClient.getBankAccountsByAccountId(accountId);
    }

    public BankAccountDetail create(String accountId, BankAccountCreateRequest request) {
        return bankAccountClient.createBankAccount(accountId, request);
    }

    // ==================== CLIENT ====================

    public List<BankAccount> findMyBankAccounts(String userId) {
        return bankAccountClient.getMyBankAccounts(userId);
    }

    public List<BankAccount> findMyBankAccountsByType(String userId, Integer typeId) {
        return bankAccountClient.getMyBankAccountsByType(userId, typeId);
    }

    public BankAccountDetail findMyBankAccountById(String userId, String id) {
        return bankAccountClient.getMyBankAccountById(userId, id);
    }

    public List<String> findMyCoHolders(String userId, String id) {
        return bankAccountClient.getMyCoHolders(userId, id);
    }
}
