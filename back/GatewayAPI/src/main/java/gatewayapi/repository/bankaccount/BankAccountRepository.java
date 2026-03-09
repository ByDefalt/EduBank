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

    public void updateParameters(String bankAccountId, BankAccountParameter parameters) {
        bankAccountClient.updateParameters(bankAccountId, parameters);
    }

    // ==================== TYPES ====================

    public List<Type> findAllTypes() {
        return bankAccountClient.getAllTypes();
    }

    public Type findTypeById(Integer id) {
        return bankAccountClient.getTypeById(id);
    }

    public Type createType(Type type) {
        return bankAccountClient.createType(type);
    }

    // ==================== PIVOT ====================

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