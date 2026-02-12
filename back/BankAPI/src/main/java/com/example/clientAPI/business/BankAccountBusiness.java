package com.example.clientAPI.business;

import com.example.clientAPI.entity.BankAccountEntity;
import com.example.clientAPI.entity.BankAccountDetailEntity;
import com.example.clientAPI.mapper.BankAccountMapper;
import com.example.clientAPI.repository.BankAccountRepository;
import dto.bankapi.BankAccount;
import dto.bankapi.BankAccountDetail;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class BankAccountBusiness {

    private final BankAccountRepository bankAccountRepository;
    private final BankAccountParameterBusiness bankAccountParameterBusiness;

    public BankAccountBusiness(BankAccountRepository bankAccountRepository,
                               BankAccountParameterBusiness bankAccountParameterBusiness) {
        this.bankAccountRepository = bankAccountRepository;
        this.bankAccountParameterBusiness = bankAccountParameterBusiness;
    }

    // ============== BankAccount Business Logic ==============

    public BankAccount createBankAccount(BankAccount dto) {
        BankAccountEntity entity = BankAccountMapper.toEntity(dto);
        bankAccountRepository.createBankAccount(entity);

        // Créer automatiquement un BankAccountParameter pour le nouveau compte
        bankAccountParameterBusiness.createParameterForBankAccount(entity.getId());

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

    public List<BankAccount> getActiveBankAccountsByUserId(Integer userId) {
        List<BankAccountEntity> entities = bankAccountRepository.getActiveBankAccountsByUserId(userId);
        return entities.stream()
                .map(BankAccountMapper::toDto)
                .collect(Collectors.toList());
    }

    public List<BankAccount> getActiveBankAccountsByUserIdAndTypeId(Integer userId, Integer typeId) {
        List<BankAccountEntity> entities = bankAccountRepository.getActiveBankAccountsByUserIdAndTypeId(userId, typeId);
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
}