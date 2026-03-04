package com.operationapi.repository;

import com.operationapi.exception.NotFoundException;
import dto.operationapi.Operation;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class OperationRepository {
    private final NamedParameterJdbcTemplate jdbcTemplate;
    private static final String SQL_SELECT_OPERATIONS = "SELECT * FROM OPERATION";
    private static final String SQL_SELECT_OPERATION_BY_ID = "SELECT * FROM OPERATION WHERE id = :id";
    private static final String SQL_SAVE_OPERATION = "INSERT INTO OPERATION (account_source_id, label, state, iban_target, amount, date) VALUES (:account_source_id, :label, :state, :iban_target, :amount, :date)";
    private static final String SQL_UPDATE_STATE_OPERATION = "UPDATE OPERATION SET state = :state WHERE id = :id";

    public OperationRepository(NamedParameterJdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<Operation> getOperations() {
        return jdbcTemplate.query(SQL_SELECT_OPERATIONS, (rs, rowNum) -> mapRow(rs));
    }

    public Operation getOperationById(Integer id) {
        List<Operation> results = jdbcTemplate.query(
                SQL_SELECT_OPERATION_BY_ID,
                Map.of("id", id),
                (rs, rowNum) -> mapRow(rs)
        );
        if (results.isEmpty()) {
            throw new NotFoundException("404", "Opération introuvable pour l'id " + id);
        }
        return results.get(0);
    }

    public Operation save(Operation operation) {
        Map<String, Object> params = new HashMap<>();
        params.put("account_source_id", operation.getAccountSourceId());
        params.put("label", operation.getLabel());
        params.put("state", operation.getState() != null ? operation.getState().toString() : "pending");
        params.put("iban_target", operation.getIbanTarget());
        params.put("amount", operation.getAmount());
        params.put("date", operation.getDate() != null ? operation.getDate().toLocalDateTime() : LocalDateTime.now());

        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(SQL_SAVE_OPERATION, new org.springframework.jdbc.core.namedparam.MapSqlParameterSource(params), keyHolder);

        Number key = keyHolder.getKey();
        if (key != null) {
            operation.setId(key.intValue());
        }
        return operation;
    }

    public void updateState(Integer id, Operation.StateEnum state) {
        this.jdbcTemplate.update(SQL_UPDATE_STATE_OPERATION, Map.of(
                "id", id,
                "state", state.toString()
        ));
    }

    private Operation mapRow(ResultSet rs) throws SQLException {
        Operation operation = new Operation();
        operation.setId(rs.getInt("id"));
        operation.setAccountSourceId(String.valueOf(rs.getInt("account_source_id")));
        operation.setLabel(rs.getString("label"));
        operation.setState(Operation.StateEnum.fromValue(rs.getString("state")));
        operation.setIbanTarget(rs.getString("iban_target"));
        operation.setAmount(rs.getDouble("amount"));
        operation.setDate(rs.getTimestamp("date").toLocalDateTime().atOffset(ZoneOffset.UTC));
        return operation;
    }
}
