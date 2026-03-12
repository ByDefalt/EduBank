package com.example.clientAPI.repository;

import com.example.clientAPI.entity.BankAccountDetailEntity;
import com.example.clientAPI.entity.BankAccountEntity;
import dto.bankapi.BankAccount;
import dto.bankapi.BankAccountDetail;
import dto.bankapi.BankAccountParameter;
import dto.bankapi.State;
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
@Import(BankAccountRepository.class)
class BankAccountRepositoryTest {

    @Autowired
    private BankAccountRepository bankAccountRepository;


    private BankAccountEntity buildBankAccount(String id, Integer parameterId, Integer typeId, Double sold, String iban) {
        BankAccountEntity ba = new BankAccountEntity();
        ba.setId(id);
        ba.setParameterId(parameterId);
        ba.setTypeId(typeId);
        ba.setSold(sold);
        ba.setIban(iban);
        return ba;
    }

    // ==================== CREATE ====================

    @Test
    void testCreateBankAccount() {
        BankAccountEntity ba = buildBankAccount("BA-TEST-1", 1, 1, 1000.00, "FR7600000000000000000000001");

        BankAccountEntity created = bankAccountRepository.createBankAccount(ba);

        assertNotNull(created);
        assertEquals("BA-TEST-1", created.getId());
        assertEquals(1000.00, created.getSold());
        assertEquals("FR7600000000000000000000001", created.getIban());
    }

    @Test
    void testCreateBankAccountGeneratesIdWhenNull() {
        BankAccountEntity ba = buildBankAccount(null, 1, 1, 500.00, "FR7600000000000000000000002");

        BankAccountEntity created = bankAccountRepository.createBankAccount(ba);

        assertNotNull(created);
        assertNotNull(created.getId());
        assertFalse(created.getId().isBlank());
    }

    // ==================== READ ====================

    @Test
    void testGetBankAccountById() {
        BankAccountEntity ba = buildBankAccount("BA-TEST-2", 1, 1, 200.00, "FR7600000000000000000000003");
        bankAccountRepository.createBankAccount(ba);

        BankAccountEntity found = bankAccountRepository.getBankAccountById("BA-TEST-2");

        assertNotNull(found);
        assertEquals("BA-TEST-2", found.getId());
        assertEquals(200.00, found.getSold());
    }

    @Test
    void testGetBankAccountByIdReturnsNullWhenNotFound() {
        BankAccountEntity result = bankAccountRepository.getBankAccountById("INEXISTANT");

        assertNull(result);
    }

    @Test
    void testGetAllBankAccounts() {
        bankAccountRepository.createBankAccount(
                buildBankAccount("BA-ALL-1", 1, 1, 100.00, "FR7600000000000000000000010"));
        bankAccountRepository.createBankAccount(
                buildBankAccount("BA-ALL-2", 1, 1, 200.00, "FR7600000000000000000000011"));

        List<BankAccountEntity> result = bankAccountRepository.getAllBankAccounts();

        assertNotNull(result);
        assertTrue(result.size() >= 2);
    }

    @Test
    void testGetBankAccountDetailById() {
        BankAccountEntity ba = buildBankAccount("BA-DETAIL-1", 1, 1, 750.00, "FR7600000000000000000000020");
        bankAccountRepository.createBankAccount(ba);

        BankAccountDetailEntity detail = bankAccountRepository.getBankAccountDetailById("BA-DETAIL-1");

        assertNotNull(detail);
        assertEquals("BA-DETAIL-1", detail.getId());
        assertEquals(750.00, detail.getSold());
        assertNotNull(detail.getParameter());
        assertNotNull(detail.getType());
    }

    @Test
    void testGetBankAccountDetailByIdReturnsNullWhenNotFound() {
        BankAccountDetailEntity result = bankAccountRepository.getBankAccountDetailById("INEXISTANT");

        assertNull(result);
    }

    // ==================== UPDATE ====================

    @Test
    void testUpdateBankAccount() {
        BankAccountEntity ba = buildBankAccount("BA-UPDATE-1", 1, 1, 300.00, "FR7600000000000000000000030");
        bankAccountRepository.createBankAccount(ba);

        BankAccountEntity updated = buildBankAccount("BA-UPDATE-1", 1, 1, 999.99, "FR7600000000000000000000030");
        bankAccountRepository.updateBankAccount("BA-UPDATE-1", updated);

        BankAccountEntity found = bankAccountRepository.getBankAccountById("BA-UPDATE-1");
        assertEquals(999.99, found.getSold());
    }

    // ==================== DELETE ====================

    @Test
    void testDeleteBankAccount() {
        BankAccountEntity ba = buildBankAccount("BA-DEL-1", 1, 1, 100.00, "FR7600000000000000000000040");
        bankAccountRepository.createBankAccount(ba);

        bankAccountRepository.deleteBankAccount("BA-DEL-1");

        BankAccountEntity found = bankAccountRepository.getBankAccountById("BA-DEL-1");
        assertNull(found);
    }

    // ==================== BALANCE ====================

    @Test
    void testGetBalance() {
        BankAccountEntity ba = buildBankAccount("BA-BAL-1", 1, 1, 1234.56, "FR7600000000000000000000050");
        bankAccountRepository.createBankAccount(ba);

        Double balance = bankAccountRepository.getBalance("BA-BAL-1");

        assertNotNull(balance);
        assertEquals(1234.56, balance);
    }

    @Test
    void testGetBalanceReturnsNullWhenNotFound() {
        Double balance = bankAccountRepository.getBalance("INEXISTANT");

        assertNull(balance);
    }
}
