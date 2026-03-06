package com.example.clientAPI.business;

import com.example.clientAPI.entity.BankAccountEntity;
import com.example.clientAPI.entity.BankAccountDetailEntity;
import com.example.clientAPI.entity.BankAccountParameterEntity;
import com.example.clientAPI.mapper.BankAccountDetailMapper;
import com.example.clientAPI.mapper.BankAccountMapper;
import com.example.clientAPI.mapper.BankAccountParameterMapper;
import com.example.clientAPI.repository.BankAccountRepository;
import com.example.clientAPI.repository.BankAccountParameterRepository;
import com.example.clientAPI.repository.BankAccountPivotRepository;
import dto.bankapi.BankAccount;
import dto.bankapi.BankAccountDetail;
import dto.bankapi.BankAccountParameter;
import dto.bankapi.BankAccountPivot;
import jakarta.ws.rs.container.ContainerRequestContext;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
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

    public List<BankAccountEntity> getAllBankAccounts() {
        List<BankAccount> dtos = bankAccountRepository.getAllBankAccounts();
        return dtos.stream().map(BankAccountMapper::toEntity).collect(Collectors.toList());
    }

    public List<BankAccountEntity> getBankAccountsByAccountId(String accountId) {
        List<BankAccount> dtos = bankAccountRepository.getBankAccountsByAccountId(accountId);
        return dtos.stream().map(BankAccountMapper::toEntity).collect(Collectors.toList());
    }

    public BankAccountDetailEntity getBankAccountDetailById(String id) {
        BankAccountDetail dto = bankAccountRepository.getBankAccountDetailById(id);
        if (dto == null) {
            throw new IllegalArgumentException("Compte bancaire non trouvé");
        }
        return BankAccountDetailMapper.toEntity(dto);
    }

    @Transactional
    public BankAccountDetailEntity createBankAccountForUser(
            String accountId,
            BankAccountEntity bankAccountEntity,
            BankAccountParameterEntity parameterEntity) {

        String bankAccountId = generateBankAccountId();
        bankAccountEntity.setId(bankAccountId);

        BankAccountParameterEntity createdParameter = bankAccountParameterBusiness.createParameterEntity(parameterEntity);
        bankAccountEntity.setParameterId(createdParameter.getId());

        bankAccountRepository.createBankAccount(BankAccountMapper.toDto(bankAccountEntity));

        BankAccountPivot pivot = new BankAccountPivot();
        pivot.setBankAccountId(bankAccountId);
        pivot.setAccountId(accountId);
        bankAccountPivotRepository.createPivot(pivot);

        BankAccountDetail detail = bankAccountRepository.getBankAccountDetailById(bankAccountId);
        return BankAccountDetailMapper.toEntity(detail);
    }

    @Transactional
    public void deleteBankAccount(String id) {
        BankAccount bankAccount = bankAccountRepository.getBankAccountById(id);
        if (bankAccount == null) {
            throw new IllegalArgumentException("Compte bancaire non trouvé");
        }

        bankAccountPivotRepository.deleteAllPivotsByBankAccount(id);
        bankAccountRepository.deleteBankAccount(id);
        bankAccountParameterRepository.deleteParameter(bankAccount.getParameterId());
    }

    public List<BankAccountEntity> getMyBankAccounts(ContainerRequestContext requestContext, Integer typeId) {
        String userId = (String) requestContext.getProperty("userId");

        List<BankAccount> dtos;
        if (typeId != null) {
            dtos = bankAccountRepository.getActiveBankAccountsByUserIdAndTypeId(userId, typeId);
        } else {
            dtos = bankAccountRepository.getActiveBankAccountsByUserId(userId);
        }
        return dtos.stream().map(BankAccountMapper::toEntity).collect(Collectors.toList());
    }

    public BankAccountDetailEntity getMyBankAccountById(ContainerRequestContext requestContext, String bankAccountId) {
        String userId = (String) requestContext.getProperty("userId");

        if (!userOwnsAccount(userId, bankAccountId)) {
            throw new SecurityException("Ce compte ne vous appartient pas");
        }

        BankAccountDetail dto = bankAccountRepository.getBankAccountDetailById(bankAccountId);
        if (dto == null) {
            throw new IllegalArgumentException("Compte bancaire non trouvé");
        }

        return BankAccountDetailMapper.toEntity(dto);
    }

    public List<String> getCoHolderIds(ContainerRequestContext requestContext, String bankAccountId) {
        String userId = (String) requestContext.getProperty("userId");

        if (!userOwnsAccount(userId, bankAccountId)) {
            throw new SecurityException("Ce compte ne vous appartient pas");
        }



        List<String> accountIds = bankAccountPivotRepository.getAccountsByBankAccount(bankAccountId);

        return accountIds.stream()
                .filter(accountId -> !accountId.equals(userId))
                .toList();
    }

    private boolean userOwnsAccount(String userId, String bankAccountId) {
        List<String> accountIds = bankAccountPivotRepository.getAccountsByBankAccount(bankAccountId);
        return accountIds.contains(userId);
    }

    private String generateBankAccountId() {
        return "BA" + System.currentTimeMillis();
    }
}