package com.operationapi.business;

import com.operationapi.entity.BeneficiaryEntity;
import com.operationapi.exception.NotFoundException;
import com.operationapi.mapper.BeneficiaryMapper;
import com.operationapi.repository.BeneficiaryRepository;
import dto.operationapi.Beneficiary;
import dto.operationapi.BeneficiaryList;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BeneficiaryBusiness {
    private final BeneficiaryRepository beneficiaryRepository;

    public BeneficiaryBusiness(BeneficiaryRepository beneficiaryRepository) {
        this.beneficiaryRepository = beneficiaryRepository;
    }

    public Beneficiary createBeneficiary(Beneficiary beneficiary) {
        BeneficiaryEntity beneficiaryEntity = BeneficiaryMapper.toEntity(beneficiary);
        BeneficiaryEntity beneficiaryResult = this.beneficiaryRepository.save(beneficiaryEntity);
        return BeneficiaryMapper.toDto(beneficiaryResult);
    }

    public BeneficiaryList getBeneficiaries() {
        List<BeneficiaryEntity> beneficiaryEntities = this.beneficiaryRepository.getBeneficiaries();
        List<Beneficiary> beneficiaries = BeneficiaryMapper.toDto(beneficiaryEntities);
        BeneficiaryList beneficiaryList = new BeneficiaryList();
        beneficiaryList.setData(beneficiaries);
        return beneficiaryList;
    }

    public BeneficiaryList getBeneficiariesByAccountId(String accountId) {
        List<BeneficiaryEntity> beneficiaryEntities = this.beneficiaryRepository.getBeneficiariesByAccountId(accountId);
        List<Beneficiary> beneficiaries = BeneficiaryMapper.toDto(beneficiaryEntities);
        BeneficiaryList beneficiaryList = new BeneficiaryList();
        beneficiaryList.setData(beneficiaries);
        if (beneficiaryList.getData().isEmpty()) {
            throw new NotFoundException("404", "Aucun bénéficiaire trouvé pour le compte " + accountId);
        }
        return beneficiaryList;
    }

    public Beneficiary updateBeneficiary(Integer id, Beneficiary beneficiary) {
        beneficiary.setId(id);
        BeneficiaryEntity beneficiaryEntity = BeneficiaryMapper.toEntity(beneficiary);
        BeneficiaryEntity beneficiaryResult = this.beneficiaryRepository.update(beneficiaryEntity);
        return BeneficiaryMapper.toDto(beneficiaryResult);
    }

    public void deleteBeneficiaryById(Integer id) {
        this.beneficiaryRepository.deleteBeneficiaryById(id);
    }

}
