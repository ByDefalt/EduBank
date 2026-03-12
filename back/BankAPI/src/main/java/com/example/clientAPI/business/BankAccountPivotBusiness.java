package com.example.clientAPI.business;

import com.example.clientAPI.entity.BankAccountEntity;
import com.example.clientAPI.entity.BankAccountPivotEntity;
import com.example.clientAPI.repository.BankAccountPivotRepository;
import com.example.clientAPI.repository.BankAccountRepository;
import jakarta.ws.rs.NotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BankAccountPivotBusiness {

    private final BankAccountPivotRepository bankAccountPivotRepository;
    private final BankAccountRepository bankAccountRepository;

    public BankAccountPivotBusiness(BankAccountPivotRepository bankAccountPivotRepository,
                                    BankAccountRepository bankAccountRepository) {
        this.bankAccountPivotRepository = bankAccountPivotRepository;
        this.bankAccountRepository = bankAccountRepository;
    }

    public void createLink(BankAccountPivotEntity entity) {
        BankAccountEntity bankAccount = bankAccountRepository.getBankAccountById(entity.getBankAccountId());
        if (bankAccount == null) {
            throw new NotFoundException("Compte bancaire non trouvé");
        }
        bankAccountPivotRepository.createPivot(entity);
    }

    public void deleteLink(BankAccountPivotEntity entity) {
        BankAccountEntity bankAccount = bankAccountRepository.getBankAccountById(entity.getBankAccountId());
        if (bankAccount == null) {
            throw new NotFoundException("Compte bancaire non trouvé");
        }
        bankAccountPivotRepository.deletePivot(entity);
    }

    public void deleteAllByBankAccount(String bankAccountId) {
        bankAccountPivotRepository.deleteAllPivotsByBankAccount(bankAccountId);
    }

    public void deleteAllByAccount(String accountId) {
        bankAccountPivotRepository.deleteAllPivotsByAccount(accountId);
    }

    public List<BankAccountPivotEntity> getLinksByBankAccount(String bankAccountId) {
        List<String> accountIds = bankAccountPivotRepository.getAccountsByBankAccount(bankAccountId);
        return accountIds.stream().map(accountId -> {
            BankAccountPivotEntity entity = new BankAccountPivotEntity();
            entity.setBankAccountId(bankAccountId);
            entity.setAccountId(accountId);
            return entity;
        }).toList();
    }

    public List<BankAccountPivotEntity> getLinksByAccount(String accountId) {
        List<String> bankAccountIds = bankAccountPivotRepository.getBankAccountsByAccount(accountId);
        return bankAccountIds.stream().map(bankAccountId -> {
            BankAccountPivotEntity entity = new BankAccountPivotEntity();
            entity.setBankAccountId(bankAccountId);
            entity.setAccountId(accountId);
            return entity;
        }).toList();
    }
}