package com.example.clientAPI.business;

import com.example.clientAPI.entity.BankAccountEntity;
import com.example.clientAPI.entity.BankAccountParameterEntity;
import com.example.clientAPI.repository.BankAccountParameterRepository;
import com.example.clientAPI.repository.BankAccountRepository;
import dto.bankapi.State;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.NotFoundException;
import org.springframework.stereotype.Service;

@Service
public class BankAccountParameterBusiness {

    private final BankAccountParameterRepository bankAccountParameterRepository;
    private final BankAccountRepository bankAccountRepository;

    public BankAccountParameterBusiness(
            BankAccountParameterRepository bankAccountParameterRepository,
            BankAccountRepository bankAccountRepository) {
        this.bankAccountParameterRepository = bankAccountParameterRepository;
        this.bankAccountRepository = bankAccountRepository;
    }

    public BankAccountParameterEntity createParameterEntity(BankAccountParameterEntity parameterEntity) {
        if (parameterEntity.getOverdraftLimit() == null) {
            parameterEntity.setOverdraftLimit(0.0);
        }
        if (parameterEntity.getState() == null) {
            parameterEntity.setState(State.ACTIVE);
        }
        return bankAccountParameterRepository.createParameter(parameterEntity);
    }
    @Transactional
    public void updateParametersByBankAccountId(String bankAccountId, BankAccountParameterEntity parameterEntity) {
        BankAccountEntity bankAccount = bankAccountRepository.getBankAccountById(bankAccountId);
        if (bankAccount == null) {
            throw new NotFoundException("Compte bancaire non trouvé");
        }
        if (parameterEntity.getOverdraftLimit() != null && parameterEntity.getOverdraftLimit() < 0) {
            throw new IllegalArgumentException("Le découvert autorisé ne peut pas être négatif");
        }
        if (parameterEntity.getOverdraftLimit() != null) {
            bankAccountParameterRepository.updateOverdraftLimit(
                    bankAccount.getParameterId(),
                    parameterEntity.getOverdraftLimit()
            );
        }
        if (parameterEntity.getState() != null) {
            bankAccountParameterRepository.updateState(
                    bankAccount.getParameterId(),
                    parameterEntity.getState().toString()
            );
        }
    }
}