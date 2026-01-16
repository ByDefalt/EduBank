package com.operationapi.repository;

import com.operationapi.entity.AccountEntity;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class AccountRepository {

    private final NamedParameterJdbcTemplate jdbcTemplate;

    public AccountRepository(NamedParameterJdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private static final String SQL_INSERT_ACCOUNT =
            "INSERT INTO account (username, password, nom, prenom, email) " +
                    "VALUES (:username, :password, :nom, :prenom, :email)";

    private static final String SQL_GET_ACCOUNT_BY_ID =
            "SELECT id, username, password, nom, prenom, email " +
                    "FROM account WHERE id = :id";

    private static final String SQL_UPDATE_ACCOUNT =
            "UPDATE account SET " +
                    "username = :username, " +
                    "password = :password, " +
                    "nom = :nom, " +
                    "prenom = :prenom, " +
                    "email = :email " +
                    "WHERE id = :id";

    private static final String SQL_DELETE_ACCOUNT =
            "DELETE FROM account WHERE id = :id";

    private static final String SQL_GET_ACCOUNT_BY_USERNAME_PASSWORD =
            "SELECT id, username, password, nom, prenom, email " +
                    "FROM account WHERE username = :username AND password = :password";

    private static final String SQL_GET_ACCOUNT_BY_EMAIL =
            "SELECT id, username, password, nom, prenom, email " +
                    "FROM account WHERE email = :email";

    public AccountEntity createAccount(AccountEntity account) {
        Map<String, Object> params = new HashMap<>();
        params.put("username", account.getUsername());
        params.put("password", account.getPassword());
        params.put("nom", account.getNom());
        params.put("prenom", account.getPrenom());
        params.put("email", account.getEmail());

        jdbcTemplate.update(SQL_INSERT_ACCOUNT, params);
        return account;
    }

    public AccountEntity getAccountById(String id) {
        Map<String, Object> params = new HashMap<>();
        params.put("id", id);

        return jdbcTemplate.queryForObject(SQL_GET_ACCOUNT_BY_ID, params, (rs, rowNum) -> {
            AccountEntity a = new AccountEntity();
            a.setId(rs.getInt("id"));
            a.setUsername(rs.getString("username"));
            a.setPassword(rs.getString("password"));
            a.setNom(rs.getString("nom"));
            a.setPrenom(rs.getString("prenom"));
            a.setEmail(rs.getString("email"));
            return a;
        });
    }

    public AccountEntity updateAccount(String id, AccountEntity account) {
        Map<String, Object> params = new HashMap<>();
        params.put("id", id);
        params.put("username", account.getUsername());
        params.put("password", account.getPassword());
        params.put("nom", account.getNom());
        params.put("prenom", account.getPrenom());
        params.put("email", account.getEmail());

        jdbcTemplate.update(SQL_UPDATE_ACCOUNT, params);
        return account;
    }

    public void deleteAccount(String id) {
        Map<String, Object> params = new HashMap<>();
        params.put("id", id);
        jdbcTemplate.update(SQL_DELETE_ACCOUNT, params);
    }


    public AccountEntity getAccountByUsernameAndPassword(String username, String password) {
        Map<String, Object> params = new HashMap<>();
        params.put("username", username);
        params.put("password", password);

        return jdbcTemplate.queryForObject(SQL_GET_ACCOUNT_BY_USERNAME_PASSWORD, params, (rs, rowNum) -> {
            AccountEntity a = new AccountEntity();
            a.setId(rs.getInt("id"));
            a.setUsername(rs.getString("username"));
            a.setPassword(rs.getString("password"));
            a.setNom(rs.getString("nom"));
            a.setPrenom(rs.getString("prenom"));
            a.setEmail(rs.getString("email"));
            return a;
        });
    }

    public AccountEntity getAccountByEmail(String email) {
        Map<String, Object> params = new HashMap<>();
        params.put("email", email);

        return jdbcTemplate.queryForObject(SQL_GET_ACCOUNT_BY_EMAIL, params, (rs, rowNum) -> {
            AccountEntity a = new AccountEntity();
            a.setId(rs.getInt("id"));
            a.setUsername(rs.getString("username"));
            a.setPassword(rs.getString("password"));
            a.setNom(rs.getString("nom"));
            a.setPrenom(rs.getString("prenom"));
            a.setEmail(rs.getString("email"));
            return a;
        });
    }
}
