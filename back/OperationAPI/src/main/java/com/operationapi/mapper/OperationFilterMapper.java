package com.operationapi.mapper;

import com.operationapi.entity.OperationFilterEntity;
import dto.operationapi.OperationFilter;

public class OperationFilterMapper {

    public static OperationFilter toDto(OperationFilterEntity entity) {
        if (entity == null) {
            return null;
        }
        OperationFilter filter = new OperationFilter();
        filter.setState(StateEnumMapper.toDto(entity.state()));
        filter.setDateFrom(entity.dateFrom());
        filter.setDateTo(entity.dateTo());
        return filter;
    }

    public static OperationFilterEntity toEntity(OperationFilter filter) {
        if (filter == null) {
            return null;
        }
        return new OperationFilterEntity(StateEnumMapper.toEntity(filter.getState()), filter.getDateFrom(), filter.getDateTo());
    }
}
