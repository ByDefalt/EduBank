package gatewayapi.business.bankaccount;

import dto.bankapiswagger.*;
import gatewayapi.repository.bankaccount.BankAccountPivotRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BankAccountPivotBusiness {

    private final BankAccountPivotRepository bankAccountPivotRepository;

    public BankAccountPivotBusiness(BankAccountPivotRepository bankAccountPivotRepository) {
        this.bankAccountPivotRepository = bankAccountPivotRepository;
    }

    // ==================== ADMIN ====================

    public void createPivot(BankAccountPivot pivot) {
        bankAccountPivotRepository.createPivot(pivot);
    }

    public void deletePivot(BankAccountPivot pivot) {
        bankAccountPivotRepository.deletePivot(pivot);
    }

    public List<BankAccountPivot> getPivotsByBankAccount(String bankAccountId) {
        return bankAccountPivotRepository.getPivotsByBankAccount(bankAccountId);
    }

    public List<BankAccountPivot> getPivotsByAccount(String accountId) {
        return bankAccountPivotRepository.getPivotsByAccount(accountId);
    }

    public void deleteAllPivotsByBankAccount(String bankAccountId) {
        bankAccountPivotRepository.deleteAllPivotsByBankAccount(bankAccountId);
    }

    public void deleteAllPivotsByAccount(String accountId) {
        bankAccountPivotRepository.deleteAllPivotsByAccount(accountId);
    }
}