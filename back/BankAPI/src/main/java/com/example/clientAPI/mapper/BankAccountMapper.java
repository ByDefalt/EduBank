package com.example.clientAPI.mapper;

import com.example.clientAPI.entity.BankAccountEntity;
import dto.bankapiswagger.BankAccount;

public class BankAccountMapper {
    public static BankAccountEntity toEntity(BankAccount dto) {
        if (dto == null) return null;

        BankAccountEntity e = new BankAccountEntity();
        e.setId(dto.getId());
        e.setParameterId(dto.getParameterId());
        e.setTypeId(dto.getTypeId());
        e.setSold(dto.getSold());
        e.setIban(dto.getIban());
        return e;
    }

    public static BankAccount toDto(BankAccountEntity entity) {
        if (entity == null) return null;

        BankAccount dto = new BankAccount();
        dto.setId(entity.getId());
        dto.setParameterId(entity.getParameterId());
        dto.setTypeId(entity.getTypeId());
        dto.setSold(entity.getSold());
        dto.setIban(entity.getIban());
        return dto;
    }
}
