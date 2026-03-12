package com.example.clientAPI.repository;

import com.example.clientAPI.entity.TypesEntity;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class TypeRepository {

    private final NamedParameterJdbcTemplate jdbcTemplate;

    public TypeRepository(NamedParameterJdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private static final String SQL_GET_ALL_TYPES =
            "SELECT id, name FROM Types";

    private static final String SQL_GET_TYPE_BY_ID =
            "SELECT id, name FROM Types WHERE id = :id";

    private static final String SQL_INSERT_TYPE =
            "INSERT INTO Types (name) VALUES (:name)";

    public List<TypesEntity> getAllTypes() {
        return jdbcTemplate.query(SQL_GET_ALL_TYPES,
                (rs, rowNum) -> mapType(rs));
    }

    public TypesEntity getTypeById(Integer id) {
        Map<String, Object> params = new HashMap<>();
        params.put("id", id);
        List<TypesEntity> results = jdbcTemplate.query(SQL_GET_TYPE_BY_ID, params,
                (rs, rowNum) -> mapType(rs));
        return results.isEmpty() ? null : results.get(0);
    }

    public TypesEntity createType(TypesEntity type) {
        Map<String, Object> params = new HashMap<>();
        params.put("name", type.getName());
        jdbcTemplate.update(SQL_INSERT_TYPE, params);
        return type;
    }

    private TypesEntity mapType(ResultSet rs) {
        try {
            TypesEntity entity = new TypesEntity();
            entity.setId(rs.getInt("id"));
            entity.setName(rs.getString("name"));
            return entity;
        } catch (Exception e) {
            throw new RuntimeException("Erreur lors du mapping Type", e);
        }
    }
}