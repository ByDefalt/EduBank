package accountapi.repository;

import accountapi.entity.AccountEntity;
import accountapi.entity.RoleEntity;
import dto.accountapi.AccountRegister;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class RoleRepository {

    private final NamedParameterJdbcTemplate jdbcTemplate;

    public RoleRepository(NamedParameterJdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private static final String SQL_FIND_ALL = "SELECT id, name FROM Role";
    private static final String SQL_FIND_BY_ID = "SELECT id, name FROM Role WHERE id = :id";

    public List<RoleEntity> findAll() {
        return jdbcTemplate.query(SQL_FIND_ALL, (rs, rowNum) -> {
            RoleEntity role = new RoleEntity();
            role.setId(rs.getInt("id"));
            role.setName(rs.getString("name"));
            return role;
        });
    }

    public RoleEntity findById(Integer id) {
        Map<String, Object> params = new HashMap<>();
        params.put("id", id);
        return jdbcTemplate.queryForObject(SQL_FIND_BY_ID, params, (rs, rowNum) -> {
            RoleEntity role = new RoleEntity();
            role.setId(rs.getInt("id"));
            role.setName(rs.getString("name"));
            return role;
        });
    }
}
