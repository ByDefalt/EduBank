package com.example.clientAPI.mapper;

import com.example.clientAPI.entity.BankAccountEntity;
import com.example.clientAPI.entity.BankAccountParameterEntity;
import com.example.clientAPI.entity.TypesEntity;
import com.example.clientAPI.entity.BankAccountDetailEntity;
import dto.bankapi.BankAccount;
import dto.bankapi.BankAccountParameter;
import dto.bankapi.Type;
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

    // DTO → Entity (BankAccountParameter)
    public static BankAccountParameterEntity toParameterEntity(BankAccountParameter dto) {
        if (dto == null) return null;

        BankAccountParameterEntity entity = new BankAccountParameterEntity();
        entity.setId(dto.getId());
        entity.setOverdraftLimit(dto.getOverdraftLimit());
        entity.setState(dto.getState());

        return entity;
    }

    // Entity → DTO (BankAccountParameter)
    public static BankAccountParameter toParameterDto(BankAccountParameterEntity entity) {
        if (entity == null) return null;

        BankAccountParameter dto = new BankAccountParameter();
        dto.setId(entity.getId());
        dto.setOverdraftLimit(entity.getOverdraftLimit());
        dto.setState(entity.getState());

        return dto;
    }

    // DTO → Entity (Type)
    public static TypesEntity toTypeEntity(Type dto) {
        if (dto == null) return null;

        TypesEntity entity = new TypesEntity();
        entity.setId(dto.getId());
        entity.setName(dto.getName());

        return entity;
    }

    // Entity → DTO (Type)
    public static Type toTypeDto(TypesEntity entity) {
        if (entity == null) return null;

        Type dto = new Type();
        dto.setId(entity.getId());
        dto.setName(entity.getName());

        return dto;
    }

    // DTO → Entity (BankAccountDetail)
    public static BankAccountDetailEntity toDetailEntity(BankAccountDetail dto) {
        if (dto == null) return null;

        BankAccountDetailEntity entity = new BankAccountDetailEntity();
        entity.setId(dto.getId());
        entity.setParameter(toParameterEntity(dto.getParameter()));
        entity.setType(toTypeEntity(dto.getType()));
        entity.setSold(dto.getSold());
        entity.setIban(dto.getIban());

        return entity;
    }

    // Entity → DTO (BankAccountDetail)
    public static BankAccountDetail toDetailDto(BankAccountDetailEntity entity) {
        if (entity == null) return null;

        BankAccountDetail dto = new BankAccountDetail();
        dto.setId(entity.getId());
        dto.setParameter(toParameterDto(entity.getParameter()));
        dto.setType(toTypeDto(entity.getType()));
        dto.setSold(entity.getSold());
        dto.setIban(entity.getIban());

        return dto;
    }
}