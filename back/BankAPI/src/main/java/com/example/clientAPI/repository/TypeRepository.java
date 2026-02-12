package com.example.clientAPI.repository;

import com.example.clientAPI.entity.TypesEntity;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class TypeRepository {

    private final NamedParameterJdbcTemplate jdbcTemplate;

    public TypeRepository(NamedParameterJdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    // ============== Type SQL ==============
    private static final String SQL_GET_ALL_TYPES =
            "SELECT id, name FROM Types";

    private static final String SQL_INSERT_TYPE =
            "INSERT INTO Types (name) VALUES (:name)";

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
}

