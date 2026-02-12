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
    private static final String SQL_SELECT_BENEFICIARIES_BY_ACCOUNT_ID = "SELECT * FROM BENEFICIARY WHERE account_source_id = :account_source_id";
    private static final String SQL_UPDATE_BENEFICIARY = "UPDATE BENEFICIARY SET account_source_id = :account_source_id, iban_target = :iban_target, name = :name WHERE id = :id";
    private static final String SQL_DELETE_BENEFICIARY_BY_ID = "DELETE FROM BENEFICIARY WHERE id = :id";
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

    public List<Beneficiary> getBeneficiariesByAccountId(String accountId) {
        return jdbcTemplate.query(SQL_SELECT_BENEFICIARIES_BY_ACCOUNT_ID, Map.of(
                "account_source_id", accountId),
                (rs, rowNum) -> {
                    Beneficiary beneficiary = new Beneficiary();
                    beneficiary.setAccountSourceId(String.valueOf(rs.getInt("account_source_id")));
                    beneficiary.setIbanTarget(rs.getString("iban_target"));
                    beneficiary.setName(rs.getString("name"));
                    return beneficiary;
                });
    }

    public Beneficiary update(Beneficiary beneficiary) {
        this.jdbcTemplate.update(SQL_UPDATE_BENEFICIARY, Map.of(
                "id", beneficiary.getId(),
                "account_source_id", beneficiary.getAccountSourceId(),
                "iban_target", beneficiary.getIbanTarget(),
                "name", beneficiary.getName()
        ));
        return beneficiary;
    }

    public void deleteBeneficiaryById(Integer id) {
        Map<String, Object> params = Map.of("id", id);
        this.jdbcTemplate.update(SQL_DELETE_BENEFICIARY_BY_ID, params);
    }
}
