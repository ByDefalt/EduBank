package com.example.clientAPI.business;

import com.example.clientAPI.repository.BankAccountPivotRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BankAccountPivotBusiness {

    private final BankAccountPivotRepository bankAccountPivotRepository;

    public BankAccountPivotBusiness(BankAccountPivotRepository bankAccountPivotRepository) {
        this.bankAccountPivotRepository = bankAccountPivotRepository;
    }

    // ============== BankAccountPivot Business Logic ==============

    public void linkAccountToBankAccount(String bankAccountId, Integer accountId) {
        bankAccountPivotRepository.createPivot(bankAccountId, accountId);
    }

    public void unlinkAccountFromBankAccount(String bankAccountId, Integer accountId) {
        bankAccountPivotRepository.deletePivot(bankAccountId, accountId);
    }

    public void unlinkAllAccountsFromBankAccount(String bankAccountId) {
        bankAccountPivotRepository.deleteAllPivotsByBankAccount(bankAccountId);
    }

    public void unlinkAllBankAccountsFromAccount(Integer accountId) {
        bankAccountPivotRepository.deleteAllPivotsByAccount(accountId);
    }

    public List<Integer> getAccountsLinkedToBankAccount(String bankAccountId) {
        return bankAccountPivotRepository.getAccountsByBankAccount(bankAccountId);
    }

    public List<String> getBankAccountsLinkedToAccount(Integer accountId) {
        return bankAccountPivotRepository.getBankAccountsByAccount(accountId);
    }
}

