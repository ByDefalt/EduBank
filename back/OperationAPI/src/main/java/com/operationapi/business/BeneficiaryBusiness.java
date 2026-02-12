package com.operationapi.business;

import com.operationapi.entity.BeneficiaryEntity;
import com.operationapi.mapper.BeneficiaryMapper;
import com.operationapi.repository.BeneficiaryRepository;
import dto.operationapi.Beneficiary;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BeneficiaryBusiness {
    private final BeneficiaryRepository beneficiaryRepository;

    public BeneficiaryBusiness(BeneficiaryRepository beneficiaryRepository) {
        this.beneficiaryRepository = beneficiaryRepository;
    }

    public Beneficiary createBeneficiary(BeneficiaryEntity beneficiaryEntity) {
        return this.beneficiaryRepository.save(BeneficiaryMapper.toDto(beneficiaryEntity));
    }

    public List<Beneficiary> getBeneficiaries() {
        return this.beneficiaryRepository.getBeneficiaries();
    }

    public List<Beneficiary> getBeneficiariesByAccountId(String accountId) {
        return this.beneficiaryRepository.getBeneficiariesByAccountId(accountId);
    }

    public Beneficiary updateBeneficiary(Integer id, Beneficiary beneficiary) {
        beneficiary.setId(id);
        return this.beneficiaryRepository.save(beneficiary);
    }

    public void deleteBeneficiaryById(Integer id) {
        this.beneficiaryRepository.deleteBeneficiaryById(id);
    }

}
