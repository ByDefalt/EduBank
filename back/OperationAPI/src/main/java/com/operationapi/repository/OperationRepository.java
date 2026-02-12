package com.operationapi.repository;

import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

public class OperationRepository {
    private final NamedParameterJdbcTemplate jdbcTemplate;

    public OperationRepository(NamedParameterJdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

}
