package com.operationapi.business;

import com.operationapi.entity.BeneficiaryEntity;
import com.operationapi.exception.NotFoundException;
import com.operationapi.mapper.BeneficiaryMapper;
import com.operationapi.repository.BeneficiaryRepository;
import dto.operationapi.Beneficiary;
import dto.operationapi.BeneficiaryList;
import dto.operationapi.Error;
import jakarta.ws.rs.core.Response;
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

    public BeneficiaryList getBeneficiaries() {
        BeneficiaryList beneficiaryList = new BeneficiaryList();
        beneficiaryList.setData(this.beneficiaryRepository.getBeneficiaries());
        return beneficiaryList;
    }

    public BeneficiaryList getBeneficiariesByAccountId(String accountId) {
        BeneficiaryList beneficiaryList = new BeneficiaryList();
        beneficiaryList.setData(this.beneficiaryRepository.getBeneficiariesByAccountId(accountId));
        if (beneficiaryList.getData().isEmpty()) {
            throw new NotFoundException("404", "Aucun bénéficiaire trouvé pour le compte " + accountId);
        }
        return beneficiaryList;
    }

    public Beneficiary updateBeneficiary(Integer id, Beneficiary beneficiary) {
        beneficiary.setId(id);
        return this.beneficiaryRepository.update(beneficiary);
    }

    public void deleteBeneficiaryById(Integer id) {
        this.beneficiaryRepository.deleteBeneficiaryById(id);
    }

}
