package com.operationapi.business;

import com.operationapi.entity.BeneficiaryEntity;
import com.operationapi.exception.NotFoundException;
import com.operationapi.repository.BeneficiaryRepository;
import dto.operationapi.Beneficiary;
import dto.operationapi.BeneficiaryList;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BeneficiaryBusinessTest {

    @Mock
    private BeneficiaryRepository beneficiaryRepository;

    @InjectMocks
    private BeneficiaryBusiness beneficiaryBusiness;

    @Test
    void testCreateBeneficiary() {
        Beneficiary beneficiary = new Beneficiary();
        beneficiary.setAccountSourceId("ACC-1");
        beneficiary.setIbanTarget("FR7612345678901234567890123");
        beneficiary.setName("Alice");

        BeneficiaryEntity saved = new BeneficiaryEntity(1, "ACC-1", "FR7612345678901234567890123", "Alice");

        when(beneficiaryRepository.save(any(BeneficiaryEntity.class))).thenReturn(saved);

        Beneficiary result = beneficiaryBusiness.createBeneficiary(beneficiary);

        assertEquals(1, result.getId());
        assertEquals("Alice", result.getName());
    }

    @Test
    void testGetBeneficiaries() {
        BeneficiaryEntity beneficiary = new BeneficiaryEntity(2, "ACC-2", "FR7611111111111111111111111", "Bob");
        when(beneficiaryRepository.getBeneficiaries()).thenReturn(List.of(beneficiary));

        BeneficiaryList result = beneficiaryBusiness.getBeneficiaries();

        assertEquals(1, result.getData().size());
        assertEquals(2, result.getData().get(0).getId());
    }

    @Test
    void testGetBeneficiariesByAccountId() {
        BeneficiaryEntity beneficiary = new BeneficiaryEntity(3, "ACC-1", "FR7612345678901234567890123", "Alice");
        when(beneficiaryRepository.getBeneficiariesByAccountId("ACC-1")).thenReturn(List.of(beneficiary));

        BeneficiaryList result = beneficiaryBusiness.getBeneficiariesByAccountId("ACC-1");

        assertEquals(1, result.getData().size());
        assertEquals(3, result.getData().get(0).getId());
    }

    @Test
    void testGetBeneficiariesByAccountIdNotFountException() {
        when(beneficiaryRepository.getBeneficiariesByAccountId("ACC-404")).thenReturn(Collections.emptyList());

        NotFoundException ex = assertThrows(NotFoundException.class, () -> beneficiaryBusiness.getBeneficiariesByAccountId("ACC-404"));

        assertEquals("404", ex.getCode());
    }

    @Test
    void testUpdateBeneficiary() {
        Beneficiary beneficiary = new Beneficiary();
        beneficiary.setAccountSourceId("ACC-1");
        beneficiary.setName("Alice Updated");

        BeneficiaryEntity updated = new BeneficiaryEntity(4, "ACC-1", "FR7612345678901234567890123", "Alice Updated");

        when(beneficiaryRepository.update(any())).thenReturn(updated);

        Beneficiary result = beneficiaryBusiness.updateBeneficiary(4, beneficiary);

        assertEquals(4, result.getId());
        assertEquals("Alice Updated", result.getName());
    }

    @Test
    void testDeleteBeneficiary() {
        beneficiaryBusiness.deleteBeneficiaryById(9);
    }
}
