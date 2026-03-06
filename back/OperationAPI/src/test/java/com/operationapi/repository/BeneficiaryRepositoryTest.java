package com.operationapi.repository;

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

    private Beneficiary beneficiary;

    @BeforeEach
    void setUp() {
        beneficiary = new Beneficiary();
        beneficiary.setAccountSourceId("ACC-1");
        beneficiary.setIbanTarget("FR7612345678901234567890123");
        beneficiary.setName("Alice Dupont");
    }

    @Test
    void testSaveBeneficiary() {
        Beneficiary saved = beneficiaryRepository.save(beneficiary);

        assertNotNull(saved);
        assertEquals("Alice Dupont", saved.getName());
        assertEquals("ACC-1", saved.getAccountSourceId());
    }

    @Test
    void testGetBeneficiaries() {
        beneficiaryRepository.save(beneficiary);

        List<Beneficiary> result = beneficiaryRepository.getBeneficiaries();

        assertFalse(result.isEmpty());
        assertEquals(1, result.size());
        assertEquals("Alice Dupont", result.get(0).getName());
    }

    @Test
    void testGetBeneficiariesByAccountId() {
        beneficiaryRepository.save(beneficiary);

        Beneficiary other = new Beneficiary();
        other.setAccountSourceId("ACC-2");
        other.setIbanTarget("FR7699999999999999999999999");
        other.setName("Bob Martin");
        beneficiaryRepository.save(other);

        List<Beneficiary> result = beneficiaryRepository.getBeneficiariesByAccountId("ACC-1");

        assertEquals(1, result.size());
        assertEquals("Alice Dupont", result.get(0).getName());
    }

    @Test
    void testGetBeneficiariesByAccountIdReturnsEmptyList() {
        List<Beneficiary> result = beneficiaryRepository.getBeneficiariesByAccountId("ACC-999");

        assertTrue(result.isEmpty());
    }

    @Test
    void testUpdateBeneficiary() {
        beneficiaryRepository.save(beneficiary);
        List<Beneficiary> saved = beneficiaryRepository.getBeneficiariesByAccountId("ACC-1");
        int id = saved.get(0).getId();

        Beneficiary toUpdate = new Beneficiary();
        toUpdate.setId(id);
        toUpdate.setAccountSourceId("ACC-1");
        toUpdate.setIbanTarget("FR7611111111111111111111111");
        toUpdate.setName("Alice Renommée");

        Beneficiary updated = beneficiaryRepository.update(toUpdate);

        assertEquals("Alice Renommée", updated.getName());
        assertEquals("FR7611111111111111111111111", updated.getIbanTarget());

        List<Beneficiary> afterUpdate = beneficiaryRepository.getBeneficiariesByAccountId("ACC-1");
        assertEquals("Alice Renommée", afterUpdate.get(0).getName());
    }

    @Test
    void testDeleteBeneficiaryById() {
        beneficiaryRepository.save(beneficiary);
        List<Beneficiary> saved = beneficiaryRepository.getBeneficiaries();
        int id = saved.get(0).getId();

        beneficiaryRepository.deleteBeneficiaryById(id);

        List<Beneficiary> afterDelete = beneficiaryRepository.getBeneficiaries();
        assertTrue(afterDelete.isEmpty());
    }
}

