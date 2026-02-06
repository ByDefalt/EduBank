package com.example.clientAPI.business;

import com.example.clientAPI.entity.BankAccountEntity;
import com.example.clientAPI.entity.BankAccountParameterEntity;
import com.example.clientAPI.entity.TypesEntity;
import com.example.clientAPI.entity.BankAccountDetailEntity;
import com.example.clientAPI.mapper.BankAccountMapper;
import com.example.clientAPI.repository.BankAccountRepository;
import dto.bankapi.BankAccount;
import dto.bankapi.BankAccountParameter;
import dto.bankapi.Type;
import dto.bankapi.BankAccountDetail;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class BankAccountBusiness {

    private final BankAccountRepository bankAccountRepository;

    public BankAccountBusiness(BankAccountRepository bankAccountRepository) {
        this.bankAccountRepository = bankAccountRepository;
    }

    // ============== BankAccount Business Logic ==============

    public BankAccount createBankAccount(BankAccount dto) {
        BankAccountEntity entity = BankAccountMapper.toEntity(dto);
        bankAccountRepository.createBankAccount(entity);
        return BankAccountMapper.toDto(entity);
    }

    public BankAccount getBankAccountById(String id) {
        BankAccountEntity entity = bankAccountRepository.getBankAccountById(id);
        return BankAccountMapper.toDto(entity);
    }

    public BankAccountDetail getBankAccountDetailById(String id) {
        BankAccountDetailEntity entity = bankAccountRepository.getBankAccountDetailById(id);
        return BankAccountMapper.toDetailDto(entity);
    }

    public List<BankAccount> getAllBankAccounts() {
        List<BankAccountEntity> entities = bankAccountRepository.getAllBankAccounts();
        return entities.stream()
                .map(BankAccountMapper::toDto)
                .collect(Collectors.toList());
    }

    public List<BankAccount> getBankAccountsByAccountId(Integer accountId) {
        List<BankAccountEntity> entities = bankAccountRepository.getBankAccountsByAccountId(accountId);
        return entities.stream()
                .map(BankAccountMapper::toDto)
                .collect(Collectors.toList());
    }

    public List<BankAccount> getBankAccountsByTypeId(Integer typeId) {
        List<BankAccountEntity> entities = bankAccountRepository.getBankAccountsByTypeId(typeId);
        return entities.stream()
                .map(BankAccountMapper::toDto)
                .collect(Collectors.toList());
    }

    public BankAccount updateBankAccount(String id, BankAccount dto) {
        BankAccountEntity entity = BankAccountMapper.toEntity(dto);
        bankAccountRepository.updateBankAccount(id, entity);
        return BankAccountMapper.toDto(entity);
    }

    public void deleteBankAccount(String id) {
        bankAccountRepository.deleteBankAccount(id);
    }

    public BankAccount updateState(String id, String state) {
        // Récupérer le compte bancaire
        BankAccountEntity bankAccount = bankAccountRepository.getBankAccountById(id);

        // Mettre à jour l'état dans les paramètres
        bankAccountRepository.updateState(bankAccount.getParameterId(), state);

        // Retourner le compte mis à jour
        return BankAccountMapper.toDto(bankAccount);
    }

    public Double getBalance(String id) {
        return bankAccountRepository.getBalance(id);
    }

    // ============== Type Business Logic ==============

    public List<Type> getAllTypes() {
        List<TypesEntity> entities = bankAccountRepository.getAllTypes();
        return entities.stream()
                .map(BankAccountMapper::toTypeDto)
                .collect(Collectors.toList());
    }

    public Type createType(Type dto) {
        TypesEntity entity = BankAccountMapper.toTypeEntity(dto);
        bankAccountRepository.createType(entity);
        return BankAccountMapper.toTypeDto(entity);
    }

    // ============== BankAccountParameter Business Logic ==============

    public List<BankAccountParameter> getAllParameters() {
        List<BankAccountParameterEntity> entities = bankAccountRepository.getAllParameters();
        return entities.stream()
                .map(BankAccountMapper::toParameterDto)
                .collect(Collectors.toList());
    }

    public BankAccountParameter createParameter(BankAccountParameter dto) {
        BankAccountParameterEntity entity = BankAccountMapper.toParameterEntity(dto);
        bankAccountRepository.createParameter(entity);
        return BankAccountMapper.toParameterDto(entity);
    }

    // ============== BankAccountPivot Business Logic ==============

    public void linkAccountToBankAccount(String bankAccountId, Integer accountId) {
        bankAccountRepository.createPivot(bankAccountId, accountId);
    }

    public void unlinkAccountFromBankAccount(String bankAccountId, Integer accountId) {
        bankAccountRepository.deletePivot(bankAccountId, accountId);
    }

    public void unlinkAllAccountsFromBankAccount(String bankAccountId) {
        bankAccountRepository.deleteAllPivotsByBankAccount(bankAccountId);
    }

    public void unlinkAllBankAccountsFromAccount(Integer accountId) {
        bankAccountRepository.deleteAllPivotsByAccount(accountId);
    }

    public List<Integer> getAccountsLinkedToBankAccount(String bankAccountId) {
        return bankAccountRepository.getAccountsByBankAccount(bankAccountId);
    }

    public List<String> getBankAccountsLinkedToAccount(Integer accountId) {
        return bankAccountRepository.getBankAccountsByAccount(accountId);
    }
}