package com.operationapi.repository;

import dto.operationapi.Beneficiary;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public class BeneficiaryRepository {
    private final NamedParameterJdbcTemplate jdbcTemplate;
    private static final String SQL_INSERT_BENEFICIARY = "INSERT INTO BENEFICIARY (account_source_id, iban_target, name) VALUES (:account_source_id, :iban_target, :name)";
    private static final String SQL_SELECT_BENEFICIARIES = "SELECT * FROM BENEFICIARY";
    private static final String SQL_SELECT_BENEFICIARY_BY_ID = "SELECT * FROM BENEFICIARY WHERE account_source_id = :account_source_id";
    public BeneficiaryRepository(NamedParameterJdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public Beneficiary save(Beneficiary beneficiary) {
        this.jdbcTemplate.update(SQL_INSERT_BENEFICIARY, Map.of(
                "account_source_id", beneficiary.getAccountSourceId(),
                "iban_target", beneficiary.getIbanTarget(),
                "name", beneficiary.getName()
        ));
        return beneficiary;
    }

    public List<Beneficiary> getBeneficiaries() {
        return jdbcTemplate.query(SQL_SELECT_BENEFICIARIES, (rs, rowNum) -> {
            Beneficiary beneficiary = new Beneficiary();
            beneficiary.setAccountSourceId(String.valueOf(rs.getInt("account_source_id")));
            beneficiary.setIbanTarget(rs.getString("iban_target"));
            beneficiary.setName(rs.getString("name"));
            return beneficiary;
        });
    }

    public Beneficiary getBeneficiaryById(String accountId) {
        Map<String, Object> params = Map.of("account_source_id", accountId);
        return jdbcTemplate.queryForObject(SQL_SELECT_BENEFICIARY_BY_ID, params, (rs, rowNum) -> {
            Beneficiary beneficiary = new Beneficiary();
            beneficiary.setAccountSourceId(String.valueOf(rs.getInt("account_source_id")));
            beneficiary.setIbanTarget(rs.getString("iban_target"));
            beneficiary.setName(rs.getString("name"));
            return beneficiary;
        });
    }
}
