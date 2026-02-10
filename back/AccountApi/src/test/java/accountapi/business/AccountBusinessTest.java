package accountapi.business;

import accountapi.entity.AccountEntity;
import accountapi.repository.AccountRepository;
import dto.accountapi.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static accountapi.mapper.AccountMapper.toDto;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AccountBusinessTest {

    @Mock
    private AccountRepository accountRepository;

    @Mock
    private PersonalInformationBusiness personalInformationBusiness;

    @Mock
    private RoleBusiness roleBusiness;

    @InjectMocks
    private AccountBusiness accountBusiness;

    @Test
    void testGetAllAccounts() {
        AccountEntity accountEntity = new AccountEntity();
        accountEntity.setId("ACC123456789");
        accountEntity.setPassword("SecureP@ssw0rd123");
        accountEntity.setRoleId(2);
        accountEntity.setPersonalInfoId(100);
        accountEntity.setState("ACTIVE");

        Account account = toDto(accountEntity);

        when(accountRepository.findAll()).thenReturn(java.util.Collections.singletonList(accountEntity));

        var accounts = accountBusiness.getAllAccounts();

        assertEquals(1, accounts.size());
        assertEquals(accountEntity.getId(), accounts.get(0).getId());
    }

    @Test
    void testGetAccountById() {
        AccountEntity accountEntity = new AccountEntity();
        accountEntity.setId("ACC123456789");
        accountEntity.setPassword("SecureP@ssw0rd123");
        accountEntity.setRoleId(2);
        accountEntity.setPersonalInfoId(100);
        accountEntity.setState("ACTIVE");

        Account account = toDto(accountEntity);

        when(accountRepository.findById("ACC123456789")).thenReturn(accountEntity);

        Account accountResponse = accountBusiness.getAccountById("ACC123456789");

        assertEquals(accountEntity.getId(), accountResponse.getId());
        assertEquals(accountEntity.getState(), accountResponse.getState());
    }

    @Test
    void testCreateAccount() {
        PersonalInformationRegister personalInfoRegister = new PersonalInformationRegister();
        personalInfoRegister.setFirstname("Jean");
        personalInfoRegister.setLastname("Martin");
        personalInfoRegister.setEmail("jean.martin@example.com");
        personalInfoRegister.setAddress("123 Rue de la République, 69001 Lyon");
        personalInfoRegister.setPhoneNumber("+33698765432");

        AccountRegister accountRegister = new AccountRegister();
        accountRegister.setPassword("SecureP@ssw0rd123");
        accountRegister.setRoleId(2);
        accountRegister.setState("ACTIVE");
        accountRegister.setPersonalInfo(personalInfoRegister);

        PersonalInformation createdPersonalInfo = new PersonalInformation();
        createdPersonalInfo.setId(100);
        createdPersonalInfo.setFirstname("Jean");
        createdPersonalInfo.setLastname("Martin");

        AccountEntity savedEntity = new AccountEntity();
        savedEntity.setId("ACC123456789");
        savedEntity.setPassword("SecureP@ssw0rd123");
        savedEntity.setRoleId(2);
        savedEntity.setPersonalInfoId(100);
        savedEntity.setState("ACTIVE");

        Account account = toDto(savedEntity);

        when(personalInformationBusiness.createPersonalInformation(any(PersonalInformationRegister.class)))
                .thenReturn(createdPersonalInfo);
        when(accountRepository.findById(anyString())).thenReturn(null);
        when(accountRepository.register(any(AccountEntity.class))).thenReturn(savedEntity);

        Account accountResponse = accountBusiness.createAccount(accountRegister);

        assertEquals(savedEntity.getId(), accountResponse.getId());
        assertEquals(savedEntity.getState(), accountResponse.getState());
    }

    @Test
    void testDeleteAccount() {
        AccountEntity accountEntity = new AccountEntity();
        accountEntity.setId("ACC123456789");
        accountEntity.setPassword("SecureP@ssw0rd123");
        accountEntity.setRoleId(2);
        accountEntity.setPersonalInfoId(100);
        accountEntity.setState("ACTIVE");

        when(accountRepository.findById("ACC123456789")).thenReturn(accountEntity);
        when(accountRepository.delete("ACC123456789")).thenReturn(true);

        boolean result = accountBusiness.deleteAccount("ACC123456789");

        assertTrue(result);
        verify(accountRepository).delete("ACC123456789");
        verify(personalInformationBusiness).deletePersonalInformation(100);
    }

    @Test
    void testSignIn() {
        SignInRequest signInRequest = new SignInRequest();
        signInRequest.setId("ACC123456789");
        signInRequest.setPassword("SecureP@ssw0rd123");

        AccountEntity accountEntity = new AccountEntity();
        accountEntity.setId("ACC123456789");
        accountEntity.setPassword("SecureP@ssw0rd123");
        accountEntity.setRoleId(2);
        accountEntity.setPersonalInfoId(100);
        accountEntity.setState("ACTIVE");

        Role role = new Role();
        role.setId(2);
        role.setName("CUSTOMER");

        when(accountRepository.getAccountByIdAndPassword("ACC123456789", "SecureP@ssw0rd123"))
                .thenReturn(accountEntity);
        when(accountRepository.findById("ACC123456789")).thenReturn(accountEntity);
        when(roleBusiness.getRoleById(2)).thenReturn(role);

        TokenRequest tokenRequest = accountBusiness.signIn(signInRequest);

        assertNotNull(tokenRequest);
        assertNotNull(tokenRequest.getJwt());
    }

    @Test
    void testGetRoleByAccountId() {
        AccountEntity accountEntity = new AccountEntity();
        accountEntity.setId("ACC123456789");
        accountEntity.setPassword("SecureP@ssw0rd123");
        accountEntity.setRoleId(2);
        accountEntity.setPersonalInfoId(100);
        accountEntity.setState("ACTIVE");

        Role role = new Role();
        role.setId(2);
        role.setName("CUSTOMER");

        when(accountRepository.findById("ACC123456789")).thenReturn(accountEntity);
        when(roleBusiness.getRoleById(2)).thenReturn(role);

        Role roleResponse = accountBusiness.getRoleByAccountId("ACC123456789");

        assertEquals(role.getName(), roleResponse.getName());
    }

    @Test
    void testGetPersonalInformationByAccountId() {
        AccountEntity accountEntity = new AccountEntity();
        accountEntity.setId("ACC123456789");
        accountEntity.setPassword("SecureP@ssw0rd123");
        accountEntity.setRoleId(2);
        accountEntity.setPersonalInfoId(100);
        accountEntity.setState("ACTIVE");

        PersonalInformation personalInfo = new PersonalInformation();
        personalInfo.setId(100);
        personalInfo.setFirstname("Jean");
        personalInfo.setLastname("Martin");
        personalInfo.setEmail("jean.martin@example.com");
        personalInfo.setAddress("123 Rue de la République, 69001 Lyon");
        personalInfo.setPhoneNumber("+33698765432");

        when(accountRepository.findById("ACC123456789")).thenReturn(accountEntity);
        when(personalInformationBusiness.getPersonalInformationById(100)).thenReturn(personalInfo);

        PersonalInformation personalInfoResponse = accountBusiness.getPersonalInformationByAccountId("ACC123456789");

        assertEquals(personalInfo.getFirstname(), personalInfoResponse.getFirstname());
        assertEquals(personalInfo.getLastname(), personalInfoResponse.getLastname());
    }

    @Test
    void testDeactivateAccount() {
        AccountEntity accountEntity = new AccountEntity();
        accountEntity.setId("ACC123456789");
        accountEntity.setPassword("SecureP@ssw0rd123");
        accountEntity.setRoleId(2);
        accountEntity.setPersonalInfoId(100);
        accountEntity.setState("ACTIVE");

        when(accountRepository.findById("ACC123456789")).thenReturn(accountEntity);

        boolean result = accountBusiness.deactivateAccount("ACC123456789");

        assertTrue(result);
        verify(accountRepository).updateState(any(AccountEntity.class));
    }

    @Test
    void testActivateAccount() {
        AccountEntity accountEntity = new AccountEntity();
        accountEntity.setId("ACC123456789");
        accountEntity.setPassword("SecureP@ssw0rd123");
        accountEntity.setRoleId(2);
        accountEntity.setPersonalInfoId(100);
        accountEntity.setState("INACTIVE");

        when(accountRepository.findById("ACC123456789")).thenReturn(accountEntity);

        boolean result = accountBusiness.activateAccount("ACC123456789");

        assertTrue(result);
        verify(accountRepository).updateState(any(AccountEntity.class));
    }
}
