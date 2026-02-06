package com.example.clientAPI.repository;

import com.example.clientAPI.entity.*;

import dto.bankapi.State;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class BankAccountRepository {

    private final NamedParameterJdbcTemplate jdbcTemplate;

    public BankAccountRepository(NamedParameterJdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    // ============== BankAccount SQL ==============
    private static final String SQL_INSERT_BANK_ACCOUNT =
            "INSERT INTO BankAccount (id, parameter_id, type_id, sold, iban) " +
                    "VALUES (:id, :parameter_id, :type_id, :sold, :iban)";

    private static final String SQL_GET_BANK_ACCOUNT_BY_ID =
            "SELECT id, parameter_id, type_id, sold, iban " +
                    "FROM BankAccount WHERE id = :id";

    private static final String SQL_GET_BANK_ACCOUNT_DETAIL_BY_ID =
            "SELECT ba.id, ba.parameter_id, ba.type_id, ba.sold, ba.iban, " +
                    "bap.id as param_id, bap.overdraft_limit, bap.state, " +
                    "t.id as type_id_val, t.name " +
                    "FROM BankAccount ba " +
                    "INNER JOIN BankAccountParameter bap ON ba.parameter_id = bap.id " +
                    "INNER JOIN Types t ON ba.type_id = t.id " +
                    "WHERE ba.id = :id";

    private static final String SQL_GET_ALL_BANK_ACCOUNTS =
            "SELECT id, parameter_id, type_id, sold, iban FROM BankAccount";

    private static final String SQL_GET_BANK_ACCOUNTS_BY_ACCOUNT_ID =
            "SELECT ba.id, ba.parameter_id, ba.type_id, ba.sold, ba.iban " +
                    "FROM BankAccount ba " +
                    "INNER JOIN BankAccountPivot pivot ON ba.id = pivot.bank_account_id " +
                    "WHERE pivot.account_id = :account_id";

    private static final String SQL_GET_BANK_ACCOUNTS_BY_TYPE_ID =
            "SELECT id, parameter_id, type_id, sold, iban " +
                    "FROM BankAccount WHERE type_id = :type_id";

    private static final String SQL_UPDATE_BANK_ACCOUNT =
            "UPDATE BankAccount SET " +
                    "parameter_id = :parameter_id, " +
                    "type_id = :type_id, " +
                    "sold = :sold, " +
                    "iban = :iban " +
                    "WHERE id = :id";

    private static final String SQL_DELETE_BANK_ACCOUNT =
            "DELETE FROM BankAccount WHERE id = :id";

    private static final String SQL_UPDATE_STATE =
            "UPDATE BankAccountParameter SET state = :state WHERE id = :parameter_id";

    private static final String SQL_GET_BALANCE =
            "SELECT sold FROM BankAccount WHERE id = :id";

    // ============== Type SQL ==============
    private static final String SQL_GET_ALL_TYPES =
            "SELECT id, name FROM Types";

    private static final String SQL_INSERT_TYPE =
            "INSERT INTO Types (name) VALUES (:name)";

    // ============== BankAccountParameter SQL ==============
    private static final String SQL_GET_ALL_PARAMETERS =
            "SELECT id, overdraft_limit, state FROM BankAccountParameter";

    private static final String SQL_INSERT_PARAMETER =
            "INSERT INTO BankAccountParameter (overdraft_limit, state) " +
                    "VALUES (:overdraft_limit, :state)";

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

    // ============== BankAccount Methods ==============

    public BankAccountEntity createBankAccount(BankAccountEntity bankAccount) {

        if (bankAccount.getId() == null || bankAccount.getId().isEmpty()) {
            bankAccount.setId(String.valueOf(System.currentTimeMillis()));
        }

        Map<String, Object> params = new HashMap<>();
        params.put("id", bankAccount.getId());
        params.put("parameter_id", bankAccount.getParameterId());
        params.put("type_id", bankAccount.getTypeId());
        params.put("sold", bankAccount.getSold());
        params.put("iban", bankAccount.getIban());

        jdbcTemplate.update(SQL_INSERT_BANK_ACCOUNT, params);
        return bankAccount;
    }

    public BankAccountEntity getBankAccountById(String id) {
        Map<String, Object> params = new HashMap<>();
        params.put("id", id);

        return jdbcTemplate.queryForObject(SQL_GET_BANK_ACCOUNT_BY_ID, params, (rs, rowNum) -> {
            BankAccountEntity ba = new BankAccountEntity();
            ba.setId(rs.getString("id"));
            ba.setParameterId(rs.getInt("parameter_id"));
            ba.setTypeId(rs.getInt("type_id"));
            ba.setSold(rs.getDouble("sold"));
            ba.setIban(rs.getString("iban"));
            return ba;
        });
    }

    public BankAccountDetailEntity getBankAccountDetailById(String id) {
        Map<String, Object> params = new HashMap<>();
        params.put("id", id);

        return jdbcTemplate.queryForObject(SQL_GET_BANK_ACCOUNT_DETAIL_BY_ID, params, (rs, rowNum) -> {
            // Créer l'entité BankAccountParameter
            BankAccountParameterEntity parameter = new BankAccountParameterEntity();
            parameter.setId(rs.getInt("param_id"));
            parameter.setOverdraftLimit(rs.getDouble("overdraft_limit"));
            parameter.setState(State.fromValue(rs.getString("state")));

            // Créer l'entité Type
            TypesEntity type = new TypesEntity();
            type.setId(rs.getInt("type_id_val"));
            type.setName(rs.getString("name"));

            // Créer et retourner l'entité BankAccountDetail
            BankAccountDetailEntity detail = new BankAccountDetailEntity();
            detail.setId(rs.getString("id"));
            detail.setParameter(parameter);
            detail.setType(type);
            detail.setSold(rs.getDouble("sold"));
            detail.setIban(rs.getString("iban"));
            return detail;
        });
    }

    public List<BankAccountEntity> getAllBankAccounts() {
        return jdbcTemplate.query(SQL_GET_ALL_BANK_ACCOUNTS, (rs, rowNum) -> {
            BankAccountEntity ba = new BankAccountEntity();
            ba.setId(rs.getString("id"));
            ba.setParameterId(rs.getInt("parameter_id"));
            ba.setTypeId(rs.getInt("type_id"));
            ba.setSold(rs.getDouble("sold"));
            ba.setIban(rs.getString("iban"));
            return ba;
        });
    }

    public List<BankAccountEntity> getBankAccountsByAccountId(Integer accountId) {
        Map<String, Object> params = new HashMap<>();
        params.put("account_id", accountId);

        return jdbcTemplate.query(SQL_GET_BANK_ACCOUNTS_BY_ACCOUNT_ID, params, (rs, rowNum) -> {
            BankAccountEntity ba = new BankAccountEntity();
            ba.setId(rs.getString("id"));
            ba.setParameterId(rs.getInt("parameter_id"));
            ba.setTypeId(rs.getInt("type_id"));
            ba.setSold(rs.getDouble("sold"));
            ba.setIban(rs.getString("iban"));
            return ba;
        });
    }

    public List<BankAccountEntity> getBankAccountsByTypeId(Integer typeId) {
        Map<String, Object> params = new HashMap<>();
        params.put("type_id", typeId);

        return jdbcTemplate.query(SQL_GET_BANK_ACCOUNTS_BY_TYPE_ID, params, (rs, rowNum) -> {
            BankAccountEntity ba = new BankAccountEntity();
            ba.setId(rs.getString("id"));
            ba.setParameterId(rs.getInt("parameter_id"));
            ba.setTypeId(rs.getInt("type_id"));
            ba.setSold(rs.getDouble("sold"));
            ba.setIban(rs.getString("iban"));
            return ba;
        });
    }

    public BankAccountEntity updateBankAccount(String id, BankAccountEntity bankAccount) {
        Map<String, Object> params = new HashMap<>();
        params.put("id", id);
        params.put("parameter_id", bankAccount.getParameterId());
        params.put("type_id", bankAccount.getTypeId());
        params.put("sold", bankAccount.getSold());
        params.put("iban", bankAccount.getIban());

        jdbcTemplate.update(SQL_UPDATE_BANK_ACCOUNT, params);
        return bankAccount;
    }

    public void deleteBankAccount(String id) {
        Map<String, Object> params = new HashMap<>();
        params.put("id", id);
        jdbcTemplate.update(SQL_DELETE_BANK_ACCOUNT, params);
    }

    public void updateState(Integer parameterId, String state) {
        Map<String, Object> params = new HashMap<>();
        params.put("parameter_id", parameterId);
        params.put("state", state);
        jdbcTemplate.update(SQL_UPDATE_STATE, params);
    }

    public Double getBalance(String id) {
        Map<String, Object> params = new HashMap<>();
        params.put("id", id);
        return jdbcTemplate.queryForObject(SQL_GET_BALANCE, params, Double.class);
    }

    // ============== Type Methods ==============
    public List<TypesEntity> getAllTypes() {
        return jdbcTemplate.query(SQL_GET_ALL_TYPES, (rs, rowNum) -> {
            TypesEntity type = new TypesEntity();
            type.setId(rs.getInt("id"));
            type.setName(rs.getString("name"));
            return type;
        });
    }

    public TypesEntity createType(TypesEntity type) {
        Map<String, Object> params = new HashMap<>();
        params.put("name", type.getName());
        jdbcTemplate.update(SQL_INSERT_TYPE, params);
        return type;
    }

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