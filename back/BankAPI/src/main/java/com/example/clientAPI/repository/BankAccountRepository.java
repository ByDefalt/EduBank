package com.example.clientAPI.repository;

import com.example.clientAPI.entity.BankAccountDetailEntity;
import com.example.clientAPI.entity.BankAccountEntity;
import com.example.clientAPI.entity.BankAccountParameterEntity;
import com.example.clientAPI.entity.TypesEntity;
import dto.bankapi.State;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class BankAccountRepository {

    private final NamedParameterJdbcTemplate jdbcTemplate;

    public BankAccountRepository(NamedParameterJdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private static final String SQL_INSERT_BANK_ACCOUNT =
            "INSERT INTO BankAccount (id, parameter_id, type_id, sold, iban) " +
                    "VALUES (:id, :parameter_id, :type_id, :sold, :iban)";

    private static final String SQL_GET_BANK_ACCOUNT_BY_ID =
            "SELECT id, parameter_id, type_id, sold, iban FROM BankAccount WHERE id = :id";

    private static final String SQL_GET_BANK_ACCOUNT_BY_IBAN =
            "SELECT id, parameter_id, type_id, sold, iban FROM BankAccount WHERE iban = :iban";

    private static final String SQL_GET_ALL_BANK_ACCOUNTS =
            "SELECT id, parameter_id, type_id, sold, iban FROM BankAccount";

    private static final String SQL_GET_BANK_ACCOUNTS_BY_ACCOUNT_ID =
            "SELECT ba.id, ba.parameter_id, ba.type_id, ba.sold, ba.iban " +
                    "FROM BankAccount ba " +
                    "INNER JOIN BankAccountPivot pivot ON ba.id = pivot.bank_account_id " +
                    "WHERE pivot.account_id = :account_id";

    private static final String SQL_GET_ACTIVE_BANK_ACCOUNTS_BY_USER_ID =
            "SELECT ba.id, ba.parameter_id, ba.type_id, ba.sold, ba.iban " +
                    "FROM BankAccount ba " +
                    "INNER JOIN BankAccountPivot pivot ON ba.id = pivot.bank_account_id " +
                    "INNER JOIN BankAccountParameter bap ON ba.parameter_id = bap.id " +
                    "WHERE pivot.account_id = :account_id AND bap.state = 'active'";

    private static final String SQL_GET_ACTIVE_BANK_ACCOUNTS_BY_USER_ID_AND_TYPE =
            "SELECT ba.id, ba.parameter_id, ba.type_id, ba.sold, ba.iban " +
                    "FROM BankAccount ba " +
                    "INNER JOIN BankAccountPivot pivot ON ba.id = pivot.bank_account_id " +
                    "INNER JOIN BankAccountParameter bap ON ba.parameter_id = bap.id " +
                    "WHERE pivot.account_id = :account_id AND ba.type_id = :type_id AND bap.state = 'active'";

    private static final String SQL_GET_BANK_ACCOUNT_DETAIL_BY_ID =
            "SELECT ba.id, ba.parameter_id, ba.type_id, ba.sold, ba.iban, " +
                    "bap.id as param_id, bap.overdraft_limit, bap.state, " +
                    "t.id as type_id_val, t.name " +
                    "FROM BankAccount ba " +
                    "INNER JOIN BankAccountParameter bap ON ba.parameter_id = bap.id " +
                    "INNER JOIN Types t ON ba.type_id = t.id " +
                    "WHERE ba.id = :id";

    private static final String SQL_UPDATE_BANK_ACCOUNT =
            "UPDATE BankAccount SET parameter_id = :parameter_id, type_id = :type_id, " +
                    "sold = :sold, iban = :iban WHERE id = :id";

    private static final String SQL_DELETE_BANK_ACCOUNT =
            "DELETE FROM BankAccount WHERE id = :id";

    private static final String SQL_GET_BALANCE =
            "SELECT sold FROM BankAccount WHERE id = :id";

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
        List<BankAccountEntity> results = jdbcTemplate.query(SQL_GET_BANK_ACCOUNT_BY_ID, params,
                (rs, rowNum) -> mapBankAccount(rs));
        return results.isEmpty() ? null : results.get(0);
    }

    public BankAccountEntity getBankAccountByIban(String iban) {
        Map<String, Object> params = new HashMap<>();
        params.put("iban", iban);
        List<BankAccountEntity> results = jdbcTemplate.query(SQL_GET_BANK_ACCOUNT_BY_IBAN, params,
                (rs, rowNum) -> mapBankAccount(rs));
        return results.isEmpty() ? null : results.get(0);
    }

    public List<BankAccountEntity> getAllBankAccounts() {
        return jdbcTemplate.query(SQL_GET_ALL_BANK_ACCOUNTS,
                (rs, rowNum) -> mapBankAccount(rs));
    }

    public List<BankAccountEntity> getBankAccountsByAccountId(String accountId) {
        Map<String, Object> params = new HashMap<>();
        params.put("account_id", accountId);
        return jdbcTemplate.query(SQL_GET_BANK_ACCOUNTS_BY_ACCOUNT_ID, params,
                (rs, rowNum) -> mapBankAccount(rs));
    }

    public List<BankAccountEntity> getActiveBankAccountsByUserId(String accountId) {
        Map<String, Object> params = new HashMap<>();
        params.put("account_id", accountId);
        return jdbcTemplate.query(SQL_GET_ACTIVE_BANK_ACCOUNTS_BY_USER_ID, params,
                (rs, rowNum) -> mapBankAccount(rs));
    }

    public List<BankAccountEntity> getActiveBankAccountsByUserIdAndTypeId(String accountId, Integer typeId) {
        Map<String, Object> params = new HashMap<>();
        params.put("account_id", accountId);
        params.put("type_id", typeId);
        return jdbcTemplate.query(SQL_GET_ACTIVE_BANK_ACCOUNTS_BY_USER_ID_AND_TYPE, params,
                (rs, rowNum) -> mapBankAccount(rs));
    }

    public BankAccountDetailEntity getBankAccountDetailById(String id) {
        Map<String, Object> params = new HashMap<>();
        params.put("id", id);
        List<BankAccountDetailEntity> results = jdbcTemplate.query(SQL_GET_BANK_ACCOUNT_DETAIL_BY_ID, params,
                (rs, rowNum) -> mapBankAccountDetail(rs));
        return results.isEmpty() ? null : results.get(0);
    }

    public BankAccountEntity updateBankAccount(String id, BankAccountEntity bankAccount) {
        Map<String, Object> params = new HashMap<>();
        params.put("id", id);
        params.put("parameter_id", bankAccount.getParameterId());
        params.put("type_id", bankAccount.getTypeId());
        params.put("sold", bankAccount.getSold());
        params.put("iban", bankAccount.getIban());
        jdbcTemplate.update(SQL_UPDATE_BANK_ACCOUNT, params);
        bankAccount.setId(id);
        return bankAccount;
    }

    public void deleteBankAccount(String id) {
        Map<String, Object> params = new HashMap<>();
        params.put("id", id);
        jdbcTemplate.update(SQL_DELETE_BANK_ACCOUNT, params);
    }

    public Double getBalance(String id) {
        Map<String, Object> params = new HashMap<>();
        params.put("id", id);
        List<Double> results = jdbcTemplate.queryForList(SQL_GET_BALANCE, params, Double.class);
        return results.isEmpty() ? null : results.get(0);
    }

    private BankAccountEntity mapBankAccount(ResultSet rs) {
        try {
            BankAccountEntity ba = new BankAccountEntity();
            ba.setId(rs.getString("id"));
            ba.setParameterId(rs.getInt("parameter_id"));
            ba.setTypeId(rs.getInt("type_id"));
            ba.setSold(rs.getDouble("sold"));
            ba.setIban(rs.getString("iban"));
            return ba;
        } catch (Exception e) {
            throw new RuntimeException("Erreur lors du mapping BankAccount", e);
        }
    }

    private BankAccountDetailEntity mapBankAccountDetail(ResultSet rs) {
        try {
            BankAccountParameterEntity parameter = new BankAccountParameterEntity();
            parameter.setId(rs.getInt("param_id"));
            parameter.setOverdraftLimit(rs.getDouble("overdraft_limit"));
            parameter.setState(State.fromValue(rs.getString("state")));

            TypesEntity type = new TypesEntity();
            type.setId(rs.getInt("type_id_val"));
            type.setName(rs.getString("name"));

            BankAccountDetailEntity detail = new BankAccountDetailEntity();
            detail.setId(rs.getString("id"));
            detail.setParameter(parameter);
            detail.setType(type);
            detail.setSold(rs.getDouble("sold"));
            detail.setIban(rs.getString("iban"));
            return detail;
        } catch (Exception e) {
            throw new RuntimeException("Erreur lors du mapping BankAccountDetail", e);
        }
    }
}