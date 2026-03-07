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
        BeneficiaryEntity entity = new BeneficiaryEntity(null, "ACC-1", "FR7612345678901234567890123", "Alice");

        Beneficiary saved = new Beneficiary();
        saved.setId(1);
        saved.setName("Alice");

        when(beneficiaryRepository.save(any(Beneficiary.class))).thenReturn(saved);

        Beneficiary result = beneficiaryBusiness.createBeneficiary(entity);

        assertEquals(1, result.getId());
        assertEquals("Alice", result.getName());
    }

    @Test
    void testGetBeneficiaries() {
        Beneficiary beneficiary = new Beneficiary();
        beneficiary.setId(2);
        when(beneficiaryRepository.getBeneficiaries()).thenReturn(List.of(beneficiary));

        BeneficiaryList result = beneficiaryBusiness.getBeneficiaries();

        assertEquals(1, result.getData().size());
        assertEquals(2, result.getData().get(0).getId());
    }

    @Test
    void testGetBeneficiariesByAccountId() {
        Beneficiary beneficiary = new Beneficiary();
        beneficiary.setId(3);
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
        Beneficiary input = new Beneficiary();
        input.setName("New Name");

        Beneficiary updated = new Beneficiary();
        updated.setId(7);
        updated.setName("New Name");

        when(beneficiaryRepository.update(input)).thenReturn(updated);

        Beneficiary result = beneficiaryBusiness.updateBeneficiary(7, input);

        assertEquals(7, input.getId());
        assertEquals(7, result.getId());
    }

    @Test
    void testDeleteBeneficiary() {
        beneficiaryBusiness.deleteBeneficiaryById(9);
    }
}
