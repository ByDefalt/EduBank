package accountapi.repository;

import accountapi.entity.AccountEntity;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class AccountRepository {

    private final NamedParameterJdbcTemplate jdbcTemplate;

    public AccountRepository(NamedParameterJdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private static final String SQL_FIND_ALL = "SELECT id, personal_info_id, role_id, password, state FROM Account";
    private static final String SQL_FIND_BY_ID = "SELECT id, personal_info_id, role_id, password, state FROM Account WHERE id = :id";
    private static final String SQL_INSERT = "INSERT INTO Account (id, personal_info_id, role_id, password, state) VALUES (:id, :personal_info_id, :role_id, :password, :state)";
    private static final String SQL_DELETE = "DELETE FROM Account WHERE id = :id";
    private static final String SQL_FIND_BY_ID_AND_PASSWORD = "SELECT id, personal_info_id, role_id, password, state FROM Account WHERE id = :id AND password = :password";
    private static final String SQL_UPDATE_STATE = "UPDATE Account SET state = :state WHERE id = :id";

    public List<AccountEntity> findAll() {
        return jdbcTemplate.query(SQL_FIND_ALL, (rs, rowNum) -> {
            AccountEntity account = new AccountEntity();
            account.setId(rs.getString("id"));
            account.setPersonalInfoId(rs.getInt("personal_info_id"));
            account.setRoleId(rs.getInt("role_id"));
            account.setState(rs.getString("state"));
            return account;
        });
    }

    public AccountEntity findById(String id) {
        Map<String, Object> params = new HashMap<>();
        params.put("id", id);

        try {
            return jdbcTemplate.queryForObject(SQL_FIND_BY_ID, params, (rs, rowNum) -> {
                AccountEntity account = new AccountEntity();
                account.setId(rs.getString("id"));
                account.setPersonalInfoId(rs.getInt("personal_info_id"));
                account.setRoleId(rs.getInt("role_id"));
                account.setState(rs.getString("state"));
                return account;
            });
        }catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    public AccountEntity register(AccountEntity account) {
        Map<String, Object> params = new HashMap<>();
        params.put("id", account.getId());
        params.put("personal_info_id", account.getPersonalInfoId());
        params.put("role_id", account.getRoleId());
        params.put("password", account.getPassword());
        params.put("state", account.getState());

        jdbcTemplate.update(SQL_INSERT, params);

        AccountEntity accountCreated = findById(account.getId());

        return accountCreated;
    }

    public AccountEntity getAccountByIdAndPassword(String id, String password) {
        Map<String, Object> params = new HashMap<>();
        params.put("id", id);
        params.put("password", password);

        return jdbcTemplate.queryForObject(SQL_FIND_BY_ID_AND_PASSWORD, params, (rs, rowNum) -> {
            AccountEntity account = new AccountEntity();
            account.setId(rs.getString("id"));
            account.setPersonalInfoId(rs.getInt("personal_info_id"));
            account.setRoleId(rs.getInt("role_id"));
            account.setPassword(rs.getString("password"));
            account.setState(rs.getString("state"));
            return account;
        });
    }

    public boolean delete(String id) {
        Map<String, Object> params = new HashMap<>();
        params.put("id", id);

        return jdbcTemplate.update(SQL_DELETE, params) > 0;
    }

    public boolean updateState(AccountEntity accountEntity){
        Map<String, Object> params = new HashMap<>();
        params.put("id", accountEntity.getId());
        params.put("state", accountEntity.getState());

        return jdbcTemplate.update(SQL_UPDATE_STATE, params) > 0;
    }
}