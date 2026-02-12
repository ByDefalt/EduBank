package com.example.clientAPI.repository;

import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class BankAccountPivotRepository {

    private final NamedParameterJdbcTemplate jdbcTemplate;

    public BankAccountPivotRepository(NamedParameterJdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    // ============== BankAccountPivot SQL ==============
    private static final String SQL_INSERT_PIVOT =
            "INSERT INTO BankAccountPivot (bank_account_id, account_id) " +
                    "VALUES (:bank_account_id, :account_id)";

    private static final String SQL_DELETE_PIVOT =
            "DELETE FROM BankAccountPivot WHERE bank_account_id = :bank_account_id AND account_id = :account_id";

    private static final String SQL_DELETE_ALL_PIVOTS_BY_BANK_ACCOUNT =
            "DELETE FROM BankAccountPivot WHERE bank_account_id = :bank_account_id";

    private static final String SQL_DELETE_ALL_PIVOTS_BY_ACCOUNT =
            "DELETE FROM BankAccountPivot WHERE account_id = :account_id";

    private static final String SQL_GET_ACCOUNTS_BY_BANK_ACCOUNT =
            "SELECT account_id FROM BankAccountPivot WHERE bank_account_id = :bank_account_id";

    private static final String SQL_GET_BANK_ACCOUNTS_BY_ACCOUNT =
            "SELECT bank_account_id FROM BankAccountPivot WHERE account_id = :account_id";

    // ============== BankAccountPivot Methods ==============

    public void createPivot(String bankAccountId, Integer accountId) {
        Map<String, Object> params = new HashMap<>();
        params.put("bank_account_id", bankAccountId);
        params.put("account_id", accountId);
        jdbcTemplate.update(SQL_INSERT_PIVOT, params);
    }

    public void deletePivot(String bankAccountId, Integer accountId) {
        Map<String, Object> params = new HashMap<>();
        params.put("bank_account_id", bankAccountId);
        params.put("account_id", accountId);
        jdbcTemplate.update(SQL_DELETE_PIVOT, params);
    }

    public void deleteAllPivotsByBankAccount(String bankAccountId) {
        Map<String, Object> params = new HashMap<>();
        params.put("bank_account_id", bankAccountId);
        jdbcTemplate.update(SQL_DELETE_ALL_PIVOTS_BY_BANK_ACCOUNT, params);
    }

    public void deleteAllPivotsByAccount(Integer accountId) {
        Map<String, Object> params = new HashMap<>();
        params.put("account_id", accountId);
        jdbcTemplate.update(SQL_DELETE_ALL_PIVOTS_BY_ACCOUNT, params);
    }

    public List<Integer> getAccountsByBankAccount(String bankAccountId) {
        Map<String, Object> params = new HashMap<>();
        params.put("bank_account_id", bankAccountId);
        return jdbcTemplate.queryForList(SQL_GET_ACCOUNTS_BY_BANK_ACCOUNT, params, Integer.class);
    }

    public List<String> getBankAccountsByAccount(Integer accountId) {
        Map<String, Object> params = new HashMap<>();
        params.put("account_id", accountId);
        return jdbcTemplate.queryForList(SQL_GET_BANK_ACCOUNTS_BY_ACCOUNT, params, String.class);
    }
}

