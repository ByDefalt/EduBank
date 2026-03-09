package gatewayapi.business.bankaccount;

import dto.bankapiswagger.*;
import gatewayapi.repository.bankaccount.BankAccountRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BankAccountBusiness {

    private final BankAccountRepository bankAccountRepository;

    public BankAccountBusiness(BankAccountRepository bankAccountRepository) {
        this.bankAccountRepository = bankAccountRepository;
    }

    // ==================== ADMIN ====================

    public List<BankAccount> getAllBankAccounts() {
        return bankAccountRepository.findAll();
    }

    public BankAccountDetail getBankAccountById(String id) {
        return bankAccountRepository.findById(id);
    }

    public void deleteBankAccount(String id) {
        bankAccountRepository.delete(id);
    }

    public List<BankAccount> getBankAccountsByAccountId(String accountId) {
        return bankAccountRepository.findByAccountId(accountId);
    }

    public BankAccountDetail createBankAccount(String accountId, BankAccountCreateRequest request) {
        return bankAccountRepository.create(accountId, request);
    }

    public void updateParameters(String bankAccountId, BankAccountParameter parameters) {
        bankAccountRepository.updateParameters(bankAccountId, parameters);
    }

    // ==================== TYPES ====================

    public List<Type> getAllTypes() {
        return bankAccountRepository.findAllTypes();
    }

    public Type getTypeById(Integer id) {
        return bankAccountRepository.findTypeById(id);
    }

    public Type createType(Type type) {
        return bankAccountRepository.createType(type);
    }

    // ==================== PIVOT ====================

    public void createPivot(BankAccountPivot pivot) {
        bankAccountRepository.createPivot(pivot);
    }

    public void deletePivot(BankAccountPivot pivot) {
        bankAccountRepository.deletePivot(pivot);
    }

    public List<BankAccountPivot> getPivotsByBankAccount(String bankAccountId) {
        return bankAccountRepository.getPivotsByBankAccount(bankAccountId);
    }

    public List<BankAccountPivot> getPivotsByAccount(String accountId) {
        return bankAccountRepository.getPivotsByAccount(accountId);
    }

    public void deleteAllPivotsByBankAccount(String bankAccountId) {
        bankAccountRepository.deleteAllPivotsByBankAccount(bankAccountId);
    }

    public void deleteAllPivotsByAccount(String accountId) {
        bankAccountRepository.deleteAllPivotsByAccount(accountId);
    }

    // ==================== CLIENT ====================

    public List<BankAccount> getMyBankAccounts(String userId, Integer typeId) {
        if (typeId != null) {
            return bankAccountRepository.findMyBankAccountsByType(userId, typeId);
        }
        return bankAccountRepository.findMyBankAccounts(userId);
    }

    public BankAccountDetail getMyBankAccountById(String userId, String bankAccountId) {
        checkOwnership(userId, bankAccountId);
        return bankAccountRepository.findMyBankAccountById(userId, bankAccountId);
    }

    public List<String> getMyCoHolders(String userId, String bankAccountId) {
        checkOwnership(userId, bankAccountId);
        return bankAccountRepository.findMyCoHolders(userId, bankAccountId);
    }

    // ==================== PRIVATE ====================

    private void checkOwnership(String userId, String bankAccountId) {
        List<BankAccountPivot> pivots = bankAccountRepository.getPivotsByBankAccount(bankAccountId);
        boolean owns = pivots.stream()
                .anyMatch(pivot -> userId.equals(pivot.getAccountId()));
        if (!owns) {
            throw new SecurityException("Ce compte ne vous appartient pas");
        }
    }
}