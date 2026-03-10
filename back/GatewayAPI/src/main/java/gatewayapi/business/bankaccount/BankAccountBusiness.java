package gatewayapi.business.bankaccount;

import dto.bankapiswagger.*;
import gatewayapi.repository.bankaccount.BankAccountRepository;
import gatewayapi.repository.bankaccount.BankAccountPivotRepository;
import jakarta.ws.rs.NotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BankAccountBusiness {

    private final BankAccountRepository bankAccountRepository;
    private final BankAccountPivotRepository bankAccountPivotRepository;

    public BankAccountBusiness(BankAccountRepository bankAccountRepository, BankAccountPivotRepository bankAccountPivotRepository) {
        this.bankAccountRepository = bankAccountRepository;
        this.bankAccountPivotRepository = bankAccountPivotRepository;
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
        List<BankAccountPivot> pivots = bankAccountPivotRepository.getPivotsByBankAccount(bankAccountId);

        if (pivots.isEmpty()) {
            throw new NotFoundException("Compte bancaire non trouvé");
        }

        boolean owns = pivots.stream()
                .anyMatch(pivot -> userId.equals(pivot.getAccountId()));
        if (!owns) {
            throw new SecurityException("Ce compte ne vous appartient pas");
        }
    }
}