package com.operationapi.mapper;

import com.operationapi.entity.BeneficiaryEntity;
import dto.operationapi.Beneficiary;

import java.util.List;

public class BeneficiaryMapper {
    private BeneficiaryMapper() {
    }

    public static BeneficiaryEntity toEntity(Beneficiary beneficiary) {
        if(beneficiary == null) {
            return null;
        }
        return new BeneficiaryEntity(beneficiary.getId(), beneficiary.getAccountSourceId(), beneficiary.getIbanTarget(), beneficiary.getName());
    }

    public static Beneficiary toDto(BeneficiaryEntity beneficiaryEntity) {
        if(beneficiaryEntity == null) {
            return null;
        }
        Beneficiary beneficiary = new Beneficiary();
        beneficiary.setId(beneficiaryEntity.id());
        beneficiary.setAccountSourceId(beneficiaryEntity.accountSourceId());
        beneficiary.setIbanTarget(beneficiaryEntity.ibanTarget());
        beneficiary.setName(beneficiaryEntity.name());
        return beneficiary;
    }

    public static List<Beneficiary> toDto(List<BeneficiaryEntity> beneficiaryEntities) {
        if(beneficiaryEntities == null) {
            return null;
        }
        return beneficiaryEntities.stream().map(BeneficiaryMapper::toDto).toList();
    }

    public static List<BeneficiaryEntity> toEntity(List<Beneficiary> beneficiaries) {
        if(beneficiaries == null) {
            return null;
        }
        return beneficiaries.stream().map(BeneficiaryMapper::toEntity).toList();
    }
}
