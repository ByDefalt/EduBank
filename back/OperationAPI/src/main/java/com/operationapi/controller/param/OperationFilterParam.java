package com.operationapi.controller.param;

import com.operationapi.exception.FunctionalException;
import dto.operationapi.OperationFilter;
import dto.operationapi.OperationState;
import jakarta.ws.rs.QueryParam;

import java.time.OffsetDateTime;
import java.time.format.DateTimeParseException;

public class OperationFilterParam {

    @QueryParam("filter[state]")
    private OperationState state;

    @QueryParam("filter[date_from]")
    private String dateFrom;

    @QueryParam("filter[date_to]")
    private String dateTo;

    public OperationFilter toDto() {
        OperationFilter filter = new OperationFilter();
        filter.setState(state);
        filter.setDateFrom(parseDate(dateFrom));
        filter.setDateTo(parseDate(dateTo));
        return filter;
    }

    private OffsetDateTime parseDate(String date) {
        if (date == null || date.isBlank()) return null;
        try {
            return OffsetDateTime.parse(date);
        } catch (DateTimeParseException e) {
            throw new FunctionalException("400", "Le format de date est invalide : " + date);
        }
    }
}
