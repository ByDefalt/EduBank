package com.operationapi.repository;

import com.operationapi.entity.BeneficiaryEntity;
import dto.operationapi.Beneficiary;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@JdbcTest
@ActiveProfiles("test")
@Import(BeneficiaryRepository.class)
class BeneficiaryRepositoryTest {

    @Autowired
    private BeneficiaryRepository beneficiaryRepository;

    private BeneficiaryEntity beneficiary;

    @BeforeEach
    void setUp() {
        beneficiary = new BeneficiaryEntity(1, "ACC-1", "FR7612345678901234567890123", "Alice Dupont");
    }

    @Test
    void testSaveBeneficiary() {
        BeneficiaryEntity saved = beneficiaryRepository.save(beneficiary);

        assertNotNull(saved);
        assertEquals("Alice Dupont", saved.name());
        assertEquals("ACC-1", saved.accountSourceId());
    }

    @Test
    void testGetBeneficiaries() {
        beneficiaryRepository.save(beneficiary);

        List<BeneficiaryEntity> result = beneficiaryRepository.getBeneficiaries();

        assertFalse(result.isEmpty());
        assertEquals(1, result.size());
        assertEquals("Alice Dupont", result.get(0).name());
    }

    @Test
    void testGetBeneficiariesByAccountId() {
        beneficiaryRepository.save(beneficiary);

        BeneficiaryEntity other = new BeneficiaryEntity(2, "ACC-2", "FR7611111111111111111111111", "Bob Martin");
        beneficiaryRepository.save(other);

        List<BeneficiaryEntity> result = beneficiaryRepository.getBeneficiariesByAccountId("ACC-1");

        assertEquals(1, result.size());
        assertEquals("Alice Dupont", result.get(0).name());
    }

    @Test
    void testGetBeneficiariesByAccountIdReturnsEmptyList() {
        List<BeneficiaryEntity> result = beneficiaryRepository.getBeneficiariesByAccountId("ACC-999");

        assertTrue(result.isEmpty());
    }

    @Test
    void testUpdateBeneficiary() {
        beneficiaryRepository.save(beneficiary);
        List<BeneficiaryEntity> saved = beneficiaryRepository.getBeneficiariesByAccountId("ACC-1");
        int id = saved.get(0).id();

        BeneficiaryEntity toUpdate = new BeneficiaryEntity(id, "ACC-1", "FR7611111111111111111111111", "Alice Renommée");

        BeneficiaryEntity updated = beneficiaryRepository.update(toUpdate);

        assertEquals("Alice Renommée", updated.name());
        assertEquals("FR7611111111111111111111111", updated.ibanTarget());

        List<BeneficiaryEntity> afterUpdate = beneficiaryRepository.getBeneficiariesByAccountId("ACC-1");
        assertEquals("Alice Renommée", afterUpdate.get(0).name());
    }

    @Test
    void testDeleteBeneficiaryById() {
        beneficiaryRepository.save(beneficiary);
        List<BeneficiaryEntity> saved = beneficiaryRepository.getBeneficiaries();
        int id = saved.get(0).id();

        beneficiaryRepository.deleteBeneficiaryById(id);

        List<BeneficiaryEntity> afterDelete = beneficiaryRepository.getBeneficiaries();
        assertTrue(afterDelete.isEmpty());
    }
}

