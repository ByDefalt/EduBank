package com.example.clientAPI.business;

import com.example.clientAPI.entity.BankAccountDetailEntity;
import com.example.clientAPI.entity.BankAccountEntity;
import com.example.clientAPI.entity.BankAccountParameterEntity;
import com.example.clientAPI.entity.BankAccountPivotEntity;
import com.example.clientAPI.entity.TypesEntity;
import com.example.clientAPI.repository.BankAccountParameterRepository;
import com.example.clientAPI.repository.BankAccountPivotRepository;
import com.example.clientAPI.repository.BankAccountRepository;
import dto.bankapi.BankAccount;
import dto.bankapi.BankAccountDetail;
import dto.bankapi.State;
import jakarta.ws.rs.NotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BankAccountBusinessTest {

    @Mock
    private BankAccountRepository bankAccountRepository;

    @Mock
    private BankAccountParameterBusiness bankAccountParameterBusiness;

    @Mock
    private BankAccountParameterRepository bankAccountParameterRepository;

    @Mock
    private BankAccountPivotRepository bankAccountPivotRepository;

    @InjectMocks
    private BankAccountBusiness bankAccountBusiness;

    // ==================== Helpers ====================

    private BankAccountEntity validBankAccountEntity() {
        BankAccountEntity ba = new BankAccountEntity();
        ba.setId("BA001");
        ba.setParameterId(1);
        ba.setTypeId(1);
        ba.setSold(1500.50);
        ba.setIban("FR7612345678901234567890123");
        return ba;
    }

    private BankAccountDetailEntity validBankAccountDetailEntity() {
        BankAccountParameterEntity param = new BankAccountParameterEntity();
        param.setId(1);
        param.setOverdraftLimit(500.00);
        param.setState(State.ACTIVE);

        TypesEntity type = new TypesEntity();
        type.setId(1);
        type.setName("Compte Courant");

        BankAccountDetailEntity detail = new BankAccountDetailEntity();
        detail.setId("BA001");
        detail.setParameter(param);
        detail.setType(type);
        detail.setSold(1500.50);
        detail.setIban("FR7612345678901234567890123");
        return detail;
    }

    private BankAccountParameterEntity validParameterEntity() {
        BankAccountParameterEntity param = new BankAccountParameterEntity();
        param.setId(1);
        param.setOverdraftLimit(500.00);
        param.setState(State.ACTIVE);
        return param;
    }

    // ==================== getAllBankAccounts ====================

    @Test
    void testGetAllBankAccounts() {
        when(bankAccountRepository.getAllBankAccounts()).thenReturn(List.of(validBankAccountEntity()));

        List<BankAccount> result = bankAccountBusiness.getAllBankAccounts();

        assertEquals(1, result.size());
        assertEquals("BA001", result.get(0).getId());
    }

    @Test
    void testGetAllBankAccountsReturnsEmptyList() {
        when(bankAccountRepository.getAllBankAccounts()).thenReturn(List.of());

        List<BankAccount> result = bankAccountBusiness.getAllBankAccounts();

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    // ==================== getBankAccountsByAccountId ====================

    @Test
    void testGetBankAccountsByAccountId() {
        when(bankAccountRepository.getBankAccountsByAccountId("ACC-1"))
                .thenReturn(List.of(validBankAccountEntity()));

        List<BankAccount> result = bankAccountBusiness.getBankAccountsByAccountId("ACC-1");

        assertEquals(1, result.size());
        assertEquals("BA001", result.get(0).getId());
    }

    // ==================== getBankAccountDetailById ====================

    @Test
    void testGetBankAccountDetailById() {
        when(bankAccountRepository.getBankAccountDetailById("BA001"))
                .thenReturn(validBankAccountDetailEntity());

        BankAccountDetail result = bankAccountBusiness.getBankAccountDetailById("BA001");

        assertNotNull(result);
        assertEquals("BA001", result.getId());
        assertEquals(1500.50, result.getSold());
    }

    @Test
    void testGetBankAccountDetailByIdThrowsNotFoundExceptionWhenNotFound() {
        when(bankAccountRepository.getBankAccountDetailById("INEXISTANT")).thenReturn(null);

        NotFoundException ex = assertThrows(
                NotFoundException.class,
                () -> bankAccountBusiness.getBankAccountDetailById("INEXISTANT")
        );

        assertTrue(ex.getMessage().contains("non trouvé"));
    }

    // ==================== getBankAccountByIban ====================

    @Test
    void testGetBankAccountByIban() {
        when(bankAccountRepository.getBankAccountByIban("FR7612345678901234567890123"))
                .thenReturn(validBankAccountEntity());

        BankAccount result = bankAccountBusiness.getBankAccountByIban("FR7612345678901234567890123");

        assertNotNull(result);
        assertEquals("BA001", result.getId());
        assertEquals("FR7612345678901234567890123", result.getIban());
    }

    @Test
    void testGetBankAccountByIbanThrowsNotFoundExceptionWhenNotFound() {
        when(bankAccountRepository.getBankAccountByIban("INEXISTANT")).thenReturn(null);

        NotFoundException ex = assertThrows(
                NotFoundException.class,
                () -> bankAccountBusiness.getBankAccountByIban("INEXISTANT")
        );

        assertTrue(ex.getMessage().contains("non trouvé"));
    }

    // ==================== updateBankAccount ====================

    @Test
    void testUpdateBankAccount() {
        BankAccountEntity updatedEntity = new BankAccountEntity();
        updatedEntity.setId("BA001");
        updatedEntity.setParameterId(1);
        updatedEntity.setTypeId(1);
        updatedEntity.setSold(9999.99);
        updatedEntity.setIban("FR7612345678901234567890123");

        when(bankAccountRepository.getBankAccountById("BA001")).thenReturn(validBankAccountEntity());
        when(bankAccountRepository.updateBankAccount(eq("BA001"), any(BankAccountEntity.class)))
                .thenReturn(updatedEntity);

        BankAccountEntity  bankAccountEntity = new BankAccountEntity();
        bankAccountEntity.setParameterId(1);
        bankAccountEntity.setTypeId(1);
        bankAccountEntity.setSold(9999.99);
        bankAccountEntity.setIban("FR7612345678901234567890123");

        BankAccount result = bankAccountBusiness.updateBankAccount("BA001", bankAccountEntity);

        assertNotNull(result);
        assertEquals(9999.99, result.getSold());
        verify(bankAccountRepository).updateBankAccount(eq("BA001"), any(BankAccountEntity.class));
    }

    @Test
    void testUpdateBankAccountThrowsNotFoundExceptionWhenNotFound() {
        when(bankAccountRepository.getBankAccountById("INEXISTANT")).thenReturn(null);

        BankAccountEntity entity = new BankAccountEntity();
        entity.setSold(100.00);

        NotFoundException ex = assertThrows(
                NotFoundException.class,
                () -> bankAccountBusiness.updateBankAccount("INEXISTANT", entity)
        );

        assertTrue(ex.getMessage().contains("non trouvé"));
        verify(bankAccountRepository, never()).updateBankAccount(any(), any());
    }

    // ==================== createBankAccountForUser ====================

    @Test
    void testCreateBankAccountForUser() {
        when(bankAccountParameterBusiness.createParameterEntity(any(BankAccountParameterEntity.class)))
                .thenReturn(validParameterEntity());
        when(bankAccountRepository.getBankAccountDetailById(anyString()))
                .thenReturn(validBankAccountDetailEntity());

        BankAccountDetail result = bankAccountBusiness.createBankAccountForUser(
                "ACC-1",
                validBankAccountEntity(),
                validParameterEntity()
        );

        assertNotNull(result);
        verify(bankAccountRepository).createBankAccount(any(BankAccountEntity.class));
        verify(bankAccountPivotRepository).createPivot(any(BankAccountPivotEntity.class));
    }

    // ==================== deleteBankAccount ====================

    @Test
    void testDeleteBankAccount() {
        when(bankAccountRepository.getBankAccountById("BA001")).thenReturn(validBankAccountEntity());

        bankAccountBusiness.deleteBankAccount("BA001");

        verify(bankAccountPivotRepository).deleteAllPivotsByBankAccount("BA001");
        verify(bankAccountRepository).deleteBankAccount("BA001");
        verify(bankAccountParameterRepository).deleteParameter(1);
    }

    @Test
    void testDeleteBankAccountThrowsNotFoundExceptionWhenNotFound() {
        when(bankAccountRepository.getBankAccountById("INEXISTANT")).thenReturn(null);

        NotFoundException ex = assertThrows(
                NotFoundException.class,
                () -> bankAccountBusiness.deleteBankAccount("INEXISTANT")
        );

        assertTrue(ex.getMessage().contains("non trouvé"));
    }

    // ==================== getMyBankAccounts ====================

    @Test
    void testGetMyBankAccountsWithoutTypeId() {
        when(bankAccountRepository.getActiveBankAccountsByUserId("ACC-1"))
                .thenReturn(List.of(validBankAccountEntity()));

        List<BankAccount> result = bankAccountBusiness.getMyBankAccounts("ACC-1", null);

        assertEquals(1, result.size());
        verify(bankAccountRepository).getActiveBankAccountsByUserId("ACC-1");
        verify(bankAccountRepository, never()).getActiveBankAccountsByUserIdAndTypeId(any(), any());
    }

    @Test
    void testGetMyBankAccountsWithTypeId() {
        when(bankAccountRepository.getActiveBankAccountsByUserIdAndTypeId("ACC-1", 1))
                .thenReturn(List.of(validBankAccountEntity()));

        List<BankAccount> result = bankAccountBusiness.getMyBankAccounts("ACC-1", 1);

        assertEquals(1, result.size());
        verify(bankAccountRepository).getActiveBankAccountsByUserIdAndTypeId("ACC-1", 1);
        verify(bankAccountRepository, never()).getActiveBankAccountsByUserId(any());
    }

    // ==================== getMyBankAccountById ====================

    @Test
    void testGetMyBankAccountById() {
        when(bankAccountRepository.getBankAccountDetailById("BA001"))
                .thenReturn(validBankAccountDetailEntity());
        when(bankAccountPivotRepository.getAccountsByBankAccount("BA001"))
                .thenReturn(List.of("ACC-1", "ACC-2"));

        BankAccountDetail result = bankAccountBusiness.getMyBankAccountById("ACC-1", "BA001");

        assertNotNull(result);
        assertEquals("BA001", result.getId());
    }

    @Test
    void testGetMyBankAccountByIdThrowsNotFoundExceptionWhenNotFound() {
        when(bankAccountRepository.getBankAccountDetailById("INEXISTANT")).thenReturn(null);

        NotFoundException ex = assertThrows(
                NotFoundException.class,
                () -> bankAccountBusiness.getMyBankAccountById("ACC-1", "INEXISTANT")
        );

        assertTrue(ex.getMessage().contains("non trouvé"));
    }

    @Test
    void testGetMyBankAccountByIdThrowsSecurityExceptionWhenUserNotOwner() {
        when(bankAccountRepository.getBankAccountDetailById("BA001"))
                .thenReturn(validBankAccountDetailEntity());
        when(bankAccountPivotRepository.getAccountsByBankAccount("BA001"))
                .thenReturn(List.of("ACC-2", "ACC-3"));

        SecurityException ex = assertThrows(
                SecurityException.class,
                () -> bankAccountBusiness.getMyBankAccountById("ACC-1", "BA001")
        );

        assertTrue(ex.getMessage().contains("appartient pas"));
    }

    // ==================== getCoHolderIds ====================

    @Test
    void testGetCoHolderIds() {
        when(bankAccountPivotRepository.getAccountsByBankAccount("BA001"))
                .thenReturn(List.of("ACC-1", "ACC-2", "ACC-3"));

        List<String> result = bankAccountBusiness.getCoHolderIds("ACC-1", "BA001");

        assertEquals(2, result.size());
        assertFalse(result.contains("ACC-1"));
        assertTrue(result.contains("ACC-2"));
        assertTrue(result.contains("ACC-3"));
    }

    @Test
    void testGetCoHolderIdsReturnsEmptyWhenSoleHolder() {
        when(bankAccountPivotRepository.getAccountsByBankAccount("BA001"))
                .thenReturn(List.of("ACC-1"));

        List<String> result = bankAccountBusiness.getCoHolderIds("ACC-1", "BA001");

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }
}