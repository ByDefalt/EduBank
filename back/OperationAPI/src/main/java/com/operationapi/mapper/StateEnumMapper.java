package com.operationapi.mapper;

import com.operationapi.entity.StateEnumEntity;
import dto.operationapi.Operation;

public class StateEnumMapper {
    private StateEnumMapper() {

    }

    public static Operation.StateEnum toDto(StateEnumEntity stateEnumEntity) {
        return switch (stateEnumEntity) {
            case PENDING -> Operation.StateEnum.PENDING;
            case COMPLETED -> Operation.StateEnum.COMPLETED;
            case FAILED -> Operation.StateEnum.FAILED;
            case CANCELLED -> Operation.StateEnum.CANCELLED;
        };
    }

    public static StateEnumEntity toEntity(Operation.StateEnum stateEnum) {
        return switch (stateEnum) {
            case PENDING -> StateEnumEntity.PENDING;
            case COMPLETED -> StateEnumEntity.COMPLETED;
            case FAILED -> StateEnumEntity.FAILED;
            case CANCELLED -> StateEnumEntity.CANCELLED;
        };
    }
}
