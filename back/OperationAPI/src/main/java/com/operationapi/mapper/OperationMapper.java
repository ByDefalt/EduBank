package com.operationapi.mapper;

import com.operationapi.entity.OperationEntity;
import dto.operationapi.Operation;

import java.time.ZoneOffset;

public class OperationMapper {
    private OperationMapper() {
    }

    public static OperationEntity toEntity(Operation operation) {
        return new OperationEntity(
                operation.getId(),
                operation.getAccountSourceId(),
                operation.getLabel(),
                StateEnumMapper.toEntity(operation.getState()),
                operation.getIbanTarget(),
                operation.getAmount(),
                operation.getDate().toLocalDateTime());
    }

    public static Operation toDto(OperationEntity entity) {
        Operation operation = new Operation();
        operation.setId(entity.id());
        operation.setAccountSourceId(entity.accountSourceId());
        operation.setLabel(entity.label());
        operation.setState(StateEnumMapper.toDto(entity.state()));
        operation.setIbanTarget(entity.ibanTarget());
        operation.setAmount(entity.amount());
        operation.setDate(entity.date().atOffset(ZoneOffset.UTC));
        return operation;
    }
}
