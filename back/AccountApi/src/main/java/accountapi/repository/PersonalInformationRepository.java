package accountapi.repository;

import accountapi.entity.PersonalInformationEntity;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class PersonalInformationRepository {

    private final NamedParameterJdbcTemplate jdbcTemplate;

    public PersonalInformationRepository(NamedParameterJdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private static final String SQL_FIND_ALL = "SELECT id, firstname, lastname, email, address, phone_number FROM PersonalInformation";
    private static final String SQL_FIND_BY_ID = "SELECT id, firstname, lastname, email, address, phone_number FROM PersonalInformation WHERE id = :id";
    private static final String SQL_INSERT = "INSERT INTO PersonalInformation (firstname, lastname, email, address, phone_number) VALUES (:firstname, :lastname, :email, :address, :phone_number)";
    private static final String SQL_DELETE = "DELETE FROM PersonalInformation WHERE id = :id";

    public List<PersonalInformationEntity> findAll() {
        return jdbcTemplate.query(SQL_FIND_ALL, (rs, rowNum) -> {
            PersonalInformationEntity entity = new PersonalInformationEntity();
            entity.setId(rs.getInt("id"));
            entity.setFirstname(rs.getString("firstname"));
            entity.setLastname(rs.getString("lastname"));
            entity.setEmail(rs.getString("email"));
            entity.setAddress(rs.getString("address"));
            entity.setPhoneNumber(rs.getString("phone_number"));
            return entity;
        });
    }

    public PersonalInformationEntity findById(Integer id) {
        Map<String, Object> params = new HashMap<>();
        params.put("id", id);

        return jdbcTemplate.queryForObject(SQL_FIND_BY_ID, params, (rs, rowNum) -> {
            PersonalInformationEntity entity = new PersonalInformationEntity();
            entity.setId(rs.getInt("id"));
            entity.setFirstname(rs.getString("firstname"));
            entity.setLastname(rs.getString("lastname"));
            entity.setEmail(rs.getString("email"));
            entity.setAddress(rs.getString("address"));
            entity.setPhoneNumber(rs.getString("phone_number"));
            return entity;
        });
    }

    public PersonalInformationEntity create(PersonalInformationEntity personalInfo) {

        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("firstname", personalInfo.getFirstname());
        params.addValue("lastname", personalInfo.getLastname());
        params.addValue("email", personalInfo.getEmail());
        params.addValue("address", personalInfo.getAddress());
        params.addValue("phone_number", personalInfo.getPhoneNumber());

        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(SQL_INSERT, params, keyHolder);

        if (keyHolder.getKey() != null) {
            personalInfo.setId(keyHolder.getKey().intValue());
        }

        return personalInfo;
    }

    public boolean delete(Integer id) {
        Map<String, Object> params = new HashMap<>();
        params.put("id", id);

        return jdbcTemplate.update(SQL_DELETE, params) > 0;
    }
}