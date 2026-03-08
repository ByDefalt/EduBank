package com.operationapi.mapper;

import com.operationapi.entity.StateEnumEntity;
import dto.operationapi.OperationState;

public class StateEnumMapper {
    private StateEnumMapper() {}

    public static OperationState toDto(StateEnumEntity stateEnumEntity) {
        if(stateEnumEntity == null) {
            return null;
        }
        return switch (stateEnumEntity) {
            case PENDING -> OperationState.PENDING;
            case COMPLETED -> OperationState.COMPLETED;
            case FAILED -> OperationState.FAILED;
            case CANCELLED -> OperationState.CANCELLED;
        };
    }

    public static StateEnumEntity toEntity(OperationState stateEnum) {
        if(stateEnum == null) {
            return null;
        }
        return switch (stateEnum) {
            case PENDING -> StateEnumEntity.PENDING;
            case COMPLETED -> StateEnumEntity.COMPLETED;
            case FAILED -> StateEnumEntity.FAILED;
            case CANCELLED -> StateEnumEntity.CANCELLED;
        };
    }
}
