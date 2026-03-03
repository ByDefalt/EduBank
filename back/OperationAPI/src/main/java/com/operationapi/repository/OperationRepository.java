package com.operationapi.repository;

import dto.operationapi.Operation;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Map;

public class OperationRepository {
    private final NamedParameterJdbcTemplate jdbcTemplate;
    private static final String SQL_SELECT_OPERATIONS = "SELECT * FROM OPERATION";
    private static final String SQL_SAVE_OPERATION = "INSERT INTO OPERATION (account_source_id, label, state, iban_target, amount, date) " + "VALUES (:account_source_id, :label, :state, :iban_target, :amount, :date)";

    public OperationRepository(NamedParameterJdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<Operation> getOperations() {
        return jdbcTemplate.query(SQL_SELECT_OPERATIONS, (rs, rowNum) -> {
            Operation operation = new Operation();
            operation.setId(rs.getInt("id"));
            operation.setAccountSourceId(String.valueOf(rs.getInt("account_source_id")));
            operation.setLabel(rs.getString("label"));
            operation.setState(Operation.StateEnum.fromValue(rs.getString("state")));
            operation.setIbanTarget(rs.getString("iban_target"));
            operation.setAmount(rs.getDouble("amount"));
            operation.setDate(OffsetDateTime.from(rs.getTimestamp("date").toLocalDateTime()));
            return operation;
        });
    }

    public Operation save(Operation operation) {
        this.jdbcTemplate.update(SQL_SAVE_OPERATION, Map.of(
                "account_source_id", operation.getAccountSourceId(),
                "label", operation.getLabel(),
                "state", operation.getState(),
                "iban_target", operation.getIbanTarget(),
                "amount", operation.getAmount(),
                "date", operation.getDate()
        ));
        return operation;
    }
}
