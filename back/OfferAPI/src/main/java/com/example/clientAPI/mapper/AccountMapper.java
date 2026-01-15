package com.example.clientAPI.mapper;

import com.example.clientAPI.entity.AccountEntity;
import dto.accountApi.Account;

public class AccountMapper {

    // DTO → Entity
    public static AccountEntity toEntity(Account dto) {
        if (dto == null) return null;

        AccountEntity entity = new AccountEntity();
        entity.setId(dto.getId());
        entity.setUsername(dto.getUsername());
        entity.setPassword(dto.getPassword());
        entity.setNom(dto.getNom());
        entity.setPrenom(dto.getPrenom());
        entity.setEmail(dto.getEmail());

        return entity;
    }

    // Entity → DTO
    public static Account toDto(AccountEntity entity) {
        if (entity == null) return null;

        Account dto = new Account();
        dto.setUsername(entity.getUsername());
        dto.setPassword(entity.getPassword());
        dto.setNom(entity.getNom());
        dto.setPrenom(entity.getPrenom());
        dto.setEmail(entity.getEmail());

        return dto;
    }
}
