package com.operationapi.repository;

import com.operationapi.entity.OperationEntity;
import com.operationapi.entity.StateEnumEntity;
import com.operationapi.exception.NotFoundException;
import dto.operationapi.OperationState;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class OperationRepository {
    private final NamedParameterJdbcTemplate jdbcTemplate;
    private static final String SQL_SELECT_OPERATION_BY_ID = "SELECT * FROM operation WHERE id = :id";
    private static final String SQL_SAVE_OPERATION = "INSERT INTO operation (account_source_id, label, state, iban_target, amount, date) VALUES (:account_source_id, :label, :state, :iban_target, :amount, :date)";
    private static final String SQL_UPDATE_STATE_OPERATION = "UPDATE operation SET state = :state WHERE id = :id";

    public OperationRepository(NamedParameterJdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<OperationEntity> getOperations(String accountId, OperationState state, OffsetDateTime dateFrom, OffsetDateTime dateTo) {
        StringBuilder sql = new StringBuilder("SELECT * FROM operation WHERE 1=1");
        Map<String, Object> params = new HashMap<>();

        if (accountId != null && !accountId.isBlank()) {
            sql.append(" AND account_source_id = :account_source_id");
            params.put("account_source_id", accountId);
        }
        if (state != null) {
            sql.append(" AND state = :state");
            params.put("state", state.toString());
        }
        if (dateFrom != null) {
            sql.append(" AND date >= :date_from");
            params.put("date_from", dateFrom.toLocalDateTime());
        }
        if (dateTo != null) {
            sql.append(" AND date <= :date_to");
            params.put("date_to", dateTo.toLocalDateTime());
        }

        return jdbcTemplate.query(sql.toString(), params, (rs, rowNum) -> mapRow(rs));
    }

    public OperationEntity getOperationById(Integer id) {
        try {
            return jdbcTemplate.queryForObject(
                    SQL_SELECT_OPERATION_BY_ID,
                    Map.of("id", id),
                    (rs, rowNum) -> mapRow(rs)
            );
        } catch (Exception e) {
            throw new NotFoundException("404", "Opération introuvable pour l'id " + id);
        }
    }

    public OperationEntity save(OperationEntity operation) {
        Map<String, Object> params = new HashMap<>();
        params.put("account_source_id", operation.accountSourceId());
        params.put("label", operation.label());
        params.put("state", operation.state() != null ? operation.state().toString() : "pending");
        params.put("iban_target", operation.ibanTarget());
        params.put("amount", operation.amount());
        params.put("date", operation.date() != null ? operation.date() : LocalDateTime.now());

        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(SQL_SAVE_OPERATION, new org.springframework.jdbc.core.namedparam.MapSqlParameterSource(params), keyHolder);

        Number key = keyHolder.getKey();
        if (key != null) {
            return new OperationEntity(key.intValue(), operation.accountSourceId(), operation.label(), operation.state(), operation.ibanTarget(), operation.amount(), operation.date());
        }
        return operation;
    }

    public void updateState(Integer id, OperationState state) {
        this.jdbcTemplate.update(SQL_UPDATE_STATE_OPERATION, Map.of(
                "id", id,
                "state", state.toString()
        ));
    }

    private OperationEntity mapRow(ResultSet rs) throws SQLException {
        return new OperationEntity(
                rs.getInt("id"),
                rs.getString("account_source_id"),
                rs.getString("label"),
                StateEnumEntity.fromValue(rs.getString("state")),
                rs.getString("iban_target"),
                rs.getDouble("amount"),
                rs.getTimestamp("date").toLocalDateTime()
        );
    }
}