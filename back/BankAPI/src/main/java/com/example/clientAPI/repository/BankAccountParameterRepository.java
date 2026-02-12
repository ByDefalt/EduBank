package com.example.clientAPI.repository;

import com.example.clientAPI.entity.BankAccountParameterEntity;
import dto.bankapi.State;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class BankAccountParameterRepository {

    private final NamedParameterJdbcTemplate jdbcTemplate;

    public BankAccountParameterRepository(NamedParameterJdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    // ============== BankAccountParameter SQL ==============
    private static final String SQL_GET_ALL_PARAMETERS =
            "SELECT id, overdraft_limit, state FROM BankAccountParameter";

    private static final String SQL_INSERT_PARAMETER =
            "INSERT INTO BankAccountParameter (overdraft_limit, state) " +
                    "VALUES (:overdraft_limit, :state)";

    private static final String SQL_UPDATE_STATE =
            "UPDATE BankAccountParameter SET state = :state WHERE id = :parameter_id";

    // ============== BankAccountParameter Methods ==============
    public List<BankAccountParameterEntity> getAllParameters() {
        return jdbcTemplate.query(SQL_GET_ALL_PARAMETERS, (rs, rowNum) -> {
            BankAccountParameterEntity param = new BankAccountParameterEntity();
            param.setId(rs.getInt("id"));
            param.setOverdraftLimit(rs.getDouble("overdraft_limit"));
            param.setState(State.fromValue(rs.getString("state")));
            return param;
        });
    }

    public BankAccountParameterEntity createParameter(BankAccountParameterEntity parameter) {
        Map<String, Object> params = new HashMap<>();
        params.put("overdraft_limit", parameter.getOverdraftLimit());
        params.put("state", parameter.getState());
        jdbcTemplate.update(SQL_INSERT_PARAMETER, params);
        return parameter;
    }

    public void updateState(Integer parameterId, String state) {
        Map<String, Object> params = new HashMap<>();
        params.put("parameter_id", parameterId);
        params.put("state", state);
        jdbcTemplate.update(SQL_UPDATE_STATE, params);
    }
}

