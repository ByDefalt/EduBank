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

    public Beneficiary getBeneficiaryById(String accountId) {
        return this.beneficiaryRepository.getBeneficiaryById(accountId);
    }

    public Beneficiary deleteBeneficiaryById(String accountId) {
        return this.beneficiaryRepository.deleteBeneficiaryById(accountId);
    }

}
