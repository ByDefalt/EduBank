package com.example.clientAPI.mapper;

import com.example.clientAPI.entity.BankAccountEntity;
import com.example.clientAPI.entity.BankAccountDetailEntity;
import dto.bankapi.BankAccount;
import dto.bankapi.BankAccountDetail;

public class BankAccountMapper {

    // DTO → Entity (BankAccount)
    public static BankAccountEntity toEntity(BankAccount dto) {
        if (dto == null) return null;

        BankAccountEntity entity = new BankAccountEntity();
        entity.setId(dto.getId());
        entity.setParameterId(dto.getParameterId());
        entity.setTypeId(dto.getTypeId());
        entity.setSold(dto.getSold());
        entity.setIban(dto.getIban());

        return entity;
    }

    // Entity → DTO (BankAccount)
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

    // DTO → Entity (BankAccountDetail)
    public static BankAccountDetailEntity toDetailEntity(BankAccountDetail dto) {
        if (dto == null) return null;

        BankAccountDetailEntity entity = new BankAccountDetailEntity();
        entity.setId(dto.getId());
        entity.setParameter(BankAccountParameterMapper.toEntity(dto.getParameter()));
        entity.setType(TypeMapper.toEntity(dto.getType()));
        entity.setSold(dto.getSold());
        entity.setIban(dto.getIban());

        return entity;
    }

    // Entity → DTO (BankAccountDetail)
    public static BankAccountDetail toDetailDto(BankAccountDetailEntity entity) {
        if (entity == null) return null;

        BankAccountDetail dto = new BankAccountDetail();
        dto.setId(entity.getId());
        dto.setParameter(BankAccountParameterMapper.toDto(entity.getParameter()));
        dto.setType(TypeMapper.toDto(entity.getType()));
        dto.setSold(entity.getSold());
        dto.setIban(entity.getIban());

        return dto;
    }
}