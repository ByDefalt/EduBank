package com.operationapi.entity;

import java.time.LocalDateTime;

public record OperationEntity(Integer id, String accountSourceId, String label, StateEnumEntity state, String ibanTarget, Double amount, LocalDateTime date) {
}
