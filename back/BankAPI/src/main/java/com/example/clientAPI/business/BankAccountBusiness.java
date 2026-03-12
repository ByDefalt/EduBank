package com.example.clientAPI.business;

import com.example.clientAPI.entity.BankAccountDetailEntity;
import com.example.clientAPI.entity.BankAccountEntity;
import com.example.clientAPI.entity.BankAccountParameterEntity;
import com.example.clientAPI.entity.BankAccountPivotEntity;
import com.example.clientAPI.mapper.BankAccountDetailMapper;
import com.example.clientAPI.mapper.BankAccountMapper;
import com.example.clientAPI.repository.BankAccountParameterRepository;
import com.example.clientAPI.repository.BankAccountPivotRepository;
import com.example.clientAPI.repository.BankAccountRepository;
import dto.bankapi.BankAccount;
import dto.bankapi.BankAccountDetail;
import jakarta.ws.rs.NotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class BankAccountBusiness {

    private final BankAccountRepository bankAccountRepository;
    private final BankAccountParameterBusiness bankAccountParameterBusiness;
    private final BankAccountParameterRepository bankAccountParameterRepository;
    private final BankAccountPivotRepository bankAccountPivotRepository;

    public BankAccountBusiness(BankAccountRepository bankAccountRepository,
                               BankAccountParameterBusiness bankAccountParameterBusiness,
                               BankAccountParameterRepository bankAccountParameterRepository,
                               BankAccountPivotRepository bankAccountPivotRepository) {
        this.bankAccountRepository = bankAccountRepository;
        this.bankAccountParameterBusiness = bankAccountParameterBusiness;
        this.bankAccountParameterRepository = bankAccountParameterRepository;
        this.bankAccountPivotRepository = bankAccountPivotRepository;
    }

    public List<BankAccount> getAllBankAccounts() {
        List<BankAccountEntity> entities = bankAccountRepository.getAllBankAccounts();
        return entities.stream().map(BankAccountMapper::toDto).collect(Collectors.toList());
    }

    public List<BankAccount> getBankAccountsByAccountId(String accountId) {
        List<BankAccountEntity> entities = bankAccountRepository.getBankAccountsByAccountId(accountId);
        return entities.stream().map(BankAccountMapper::toDto).collect(Collectors.toList());
    }

    public BankAccountDetail getBankAccountDetailById(String id) {
        BankAccountDetailEntity entity = bankAccountRepository.getBankAccountDetailById(id);
        if (entity == null) {
            throw new NotFoundException("Compte bancaire non trouvé");
        }
        return BankAccountDetailMapper.toDto(entity);
    }

    public BankAccount getBankAccountByIban(String iban) {
        BankAccountEntity entity = bankAccountRepository.getBankAccountByIban(iban);
        if (entity == null) {
            throw new NotFoundException("Compte bancaire non trouvé");
        }
        return BankAccountMapper.toDto(entity);
    }

    public BankAccount updateBankAccount(String id, BankAccountEntity entity) {
        BankAccountEntity existing = bankAccountRepository.getBankAccountById(id);
        if (existing == null) {
            throw new NotFoundException("Compte bancaire non trouvé");
        }
        BankAccountEntity updated = bankAccountRepository.updateBankAccount(id, entity);
        return BankAccountMapper.toDto(updated);
    }

    @Transactional
    public BankAccountDetail createBankAccountForUser(
            String accountId,
            BankAccountEntity bankAccountEntity,
            BankAccountParameterEntity parameterEntity) {

        String bankAccountId = generateBankAccountId();
        bankAccountEntity.setId(bankAccountId);

        BankAccountParameterEntity createdParameter = bankAccountParameterBusiness
                .createParameterEntity(parameterEntity);
        bankAccountEntity.setParameterId(createdParameter.getId());

        bankAccountRepository.createBankAccount(bankAccountEntity);

        BankAccountPivotEntity pivot = new BankAccountPivotEntity();
        pivot.setBankAccountId(bankAccountId);
        pivot.setAccountId(accountId);
        bankAccountPivotRepository.createPivot(pivot);

        BankAccountDetailEntity detail = bankAccountRepository.getBankAccountDetailById(bankAccountId);
        return BankAccountDetailMapper.toDto(detail);
    }

    @Transactional
    public void deleteBankAccount(String id) {
        BankAccountEntity bankAccount = bankAccountRepository.getBankAccountById(id);
        if (bankAccount == null) {
            throw new NotFoundException("Compte bancaire non trouvé");
        }
        bankAccountPivotRepository.deleteAllPivotsByBankAccount(id);
        bankAccountRepository.deleteBankAccount(id);
        bankAccountParameterRepository.deleteParameter(bankAccount.getParameterId());
    }

    public List<BankAccount> getMyBankAccounts(String userId, Integer typeId) {
        List<BankAccountEntity> entities;
        if (typeId != null) {
            entities = bankAccountRepository.getActiveBankAccountsByUserIdAndTypeId(userId, typeId);
        } else {
            entities = bankAccountRepository.getActiveBankAccountsByUserId(userId);
        }
        return entities.stream().map(BankAccountMapper::toDto).collect(Collectors.toList());
    }

    public BankAccountDetail getMyBankAccountById(String userId, String bankAccountId) {
        BankAccountDetailEntity entity = bankAccountRepository.getBankAccountDetailById(bankAccountId);
        if (entity == null) {
            throw new NotFoundException("Compte bancaire non trouvé");
        }
        List<String> accountIds = bankAccountPivotRepository.getAccountsByBankAccount(bankAccountId);
        if (!accountIds.contains(userId)) {
            throw new SecurityException("Ce compte ne vous appartient pas");
        }
        return BankAccountDetailMapper.toDto(entity);
    }

    public List<String> getCoHolderIds(String userId, String bankAccountId) {
        List<String> accountIds = bankAccountPivotRepository.getAccountsByBankAccount(bankAccountId);
        if (!accountIds.contains(userId)) {
            throw new SecurityException("Ce compte ne vous appartient pas");
        }
        return accountIds.stream()
                .filter(accountId -> !accountId.equals(userId))
                .toList();
    }

    private String generateBankAccountId() {
        return "BA" + System.currentTimeMillis();
    }
}