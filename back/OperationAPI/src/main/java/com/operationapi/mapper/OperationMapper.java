package com.operationapi.mapper;

import com.operationapi.entity.OperationEntity;
import dto.operationapi.Operation;

import java.time.ZoneOffset;
import java.util.List;

public class OperationMapper {
    private OperationMapper() {
    }

    public static OperationEntity toEntity(Operation operation) {
        if(operation == null) {
            return null;
        }
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
        if(entity == null) {
            return null;
        }
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

    public static List<OperationEntity> toEntity(List<Operation> operations) {
        if (operations == null) {
            return null;
        }
        return operations.stream().map(OperationMapper::toEntity).toList();
    }

    public static List<Operation> toDto(List<OperationEntity> entities) {
        if (entities == null) {
            return null;
        }
        return entities.stream().map(OperationMapper::toDto).toList();
    }
}
