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
    private final TypeRepository typeRepository;
    private final BankAccountParameterRepository bankAccountParameterRepository;
    private final BankAccountPivotRepository bankAccountPivotRepository;

    public BankAccountRepository(NamedParameterJdbcTemplate jdbcTemplate,
                                 TypeRepository typeRepository,
                                 BankAccountParameterRepository bankAccountParameterRepository,
                                 BankAccountPivotRepository bankAccountPivotRepository) {
        this.jdbcTemplate = jdbcTemplate;
        this.typeRepository = typeRepository;
        this.bankAccountParameterRepository = bankAccountParameterRepository;
        this.bankAccountPivotRepository = bankAccountPivotRepository;
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

    private static final String SQL_GET_ACTIVE_BANK_ACCOUNTS_BY_USER_ID =
            "SELECT ba.id, ba.parameter_id, ba.type_id, ba.sold, ba.iban " +
                    "FROM BankAccount ba " +
                    "INNER JOIN BankAccountPivot pivot ON ba.id = pivot.bank_account_id " +
                    "INNER JOIN BankAccountParameter bap ON ba.parameter_id = bap.id " +
                    "WHERE pivot.account_id = :account_id AND bap.state = 'active'";

    private static final String SQL_GET_ACTIVE_BANK_ACCOUNTS_BY_USER_ID_AND_TYPE_ID =
            "SELECT ba.id, ba.parameter_id, ba.type_id, ba.sold, ba.iban " +
                    "FROM BankAccount ba " +
                    "INNER JOIN BankAccountPivot pivot ON ba.id = pivot.bank_account_id " +
                    "INNER JOIN BankAccountParameter bap ON ba.parameter_id = bap.id " +
                    "WHERE pivot.account_id = :account_id AND ba.type_id = :type_id AND bap.state = 'active'";

    private static final String SQL_UPDATE_BANK_ACCOUNT =
            "UPDATE BankAccount SET " +
                    "parameter_id = :parameter_id, " +
                    "type_id = :type_id, " +
                    "sold = :sold, " +
                    "iban = :iban " +
                    "WHERE id = :id";

    private static final String SQL_DELETE_BANK_ACCOUNT =
            "DELETE FROM BankAccount WHERE id = :id";

    private static final String SQL_GET_BALANCE =
            "SELECT sold FROM BankAccount WHERE id = :id";

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

    public List<BankAccountEntity> getActiveBankAccountsByUserId(Integer accountId) {
        Map<String, Object> params = new HashMap<>();
        params.put("account_id", accountId);

        return jdbcTemplate.query(SQL_GET_ACTIVE_BANK_ACCOUNTS_BY_USER_ID, params, (rs, rowNum) -> {
            BankAccountEntity ba = new BankAccountEntity();
            ba.setId(rs.getString("id"));
            ba.setParameterId(rs.getInt("parameter_id"));
            ba.setTypeId(rs.getInt("type_id"));
            ba.setSold(rs.getDouble("sold"));
            ba.setIban(rs.getString("iban"));
            return ba;
        });
    }

    public List<BankAccountEntity> getActiveBankAccountsByUserIdAndTypeId(Integer accountId, Integer typeId) {
        Map<String, Object> params = new HashMap<>();
        params.put("account_id", accountId);
        params.put("type_id", typeId);

        return jdbcTemplate.query(SQL_GET_ACTIVE_BANK_ACCOUNTS_BY_USER_ID_AND_TYPE_ID, params, (rs, rowNum) -> {
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

    public Double getBalance(String id) {
        Map<String, Object> params = new HashMap<>();
        params.put("id", id);
        return jdbcTemplate.queryForObject(SQL_GET_BALANCE, params, Double.class);
    }

    // ============== Delegated Methods to Other Repositories ==============

    public List<TypesEntity> getAllTypes() {
        return typeRepository.getAllTypes();
    }

    public TypesEntity createType(TypesEntity type) {
        return typeRepository.createType(type);
    }

    public List<BankAccountParameterEntity> getAllParameters() {
        return bankAccountParameterRepository.getAllParameters();
    }

    public BankAccountParameterEntity createParameter(BankAccountParameterEntity parameter) {
        return bankAccountParameterRepository.createParameter(parameter);
    }

    public void updateState(Integer parameterId, String state) {
        bankAccountParameterRepository.updateState(parameterId, state);
    }

    public void createPivot(String bankAccountId, Integer accountId) {
        bankAccountPivotRepository.createPivot(bankAccountId, accountId);
    }

    public void deletePivot(String bankAccountId, Integer accountId) {
        bankAccountPivotRepository.deletePivot(bankAccountId, accountId);
    }

    public void deleteAllPivotsByBankAccount(String bankAccountId) {
        bankAccountPivotRepository.deleteAllPivotsByBankAccount(bankAccountId);
    }

    public void deleteAllPivotsByAccount(Integer accountId) {
        bankAccountPivotRepository.deleteAllPivotsByAccount(accountId);
    }

    public List<Integer> getAccountsByBankAccount(String bankAccountId) {
        return bankAccountPivotRepository.getAccountsByBankAccount(bankAccountId);
    }

    public List<String> getBankAccountsByAccount(Integer accountId) {
        return bankAccountPivotRepository.getBankAccountsByAccount(accountId);
    }
}