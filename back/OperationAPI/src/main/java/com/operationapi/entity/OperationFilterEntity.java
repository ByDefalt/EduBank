package com.operationapi.entity;

import java.time.OffsetDateTime;

public record OperationFilterEntity(StateEnumEntity state, OffsetDateTime dateFrom, OffsetDateTime dateTo) {
}
