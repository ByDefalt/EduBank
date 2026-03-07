package com.operationapi.mapper;

import com.operationapi.entity.BeneficiaryEntity;
import dto.operationapi.Beneficiary;

public class BeneficiaryMapper {
    private BeneficiaryMapper() {
    }

    public static BeneficiaryEntity toEntity(Beneficiary beneficiary) {
        return new BeneficiaryEntity(beneficiary.getId(), beneficiary.getAccountSourceId(), beneficiary.getIbanTarget(), beneficiary.getName());
    }

    public static Beneficiary toDto(BeneficiaryEntity beneficiaryEntity) {
        Beneficiary beneficiary = new Beneficiary();
        beneficiary.setId(beneficiaryEntity.id());
        beneficiary.setAccountSourceId(beneficiaryEntity.accountSourceId());
        beneficiary.setIbanTarget(beneficiaryEntity.ibanTarget());
        beneficiary.setName(beneficiaryEntity.name());
        return beneficiary;
    }
}
