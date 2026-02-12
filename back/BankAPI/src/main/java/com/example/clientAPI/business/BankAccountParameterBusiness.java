package com.example.clientAPI.business;

import com.example.clientAPI.entity.BankAccountParameterEntity;
import com.example.clientAPI.mapper.BankAccountParameterMapper;
import com.example.clientAPI.repository.BankAccountParameterRepository;
import dto.bankapi.BankAccountParameter;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class BankAccountParameterBusiness {

    private final BankAccountParameterRepository bankAccountParameterRepository;

    public BankAccountParameterBusiness(BankAccountParameterRepository bankAccountParameterRepository) {
        this.bankAccountParameterRepository = bankAccountParameterRepository;
    }

    // ============== BankAccountParameter Business Logic ==============

    public List<BankAccountParameter> getAllParameters() {
        List<BankAccountParameterEntity> entities = bankAccountParameterRepository.getAllParameters();
        return entities.stream()
                .map(BankAccountParameterMapper::toDto)
                .collect(Collectors.toList());
    }

    public BankAccountParameter createParameter(BankAccountParameter dto) {
        BankAccountParameterEntity entity = BankAccountParameterMapper.toEntity(dto);
        bankAccountParameterRepository.createParameter(entity);
        return BankAccountParameterMapper.toDto(entity);
    }

    public BankAccountParameter createParameterForBankAccount(String bankAccountId) {

        BankAccountParameter parameter = new BankAccountParameter();
        parameter.setOverdraftLimit(0.0);

        BankAccountParameterEntity entity = BankAccountParameterMapper.toEntity(parameter);
        bankAccountParameterRepository.createParameter(entity);
        return BankAccountParameterMapper.toDto(entity);
    }
}


