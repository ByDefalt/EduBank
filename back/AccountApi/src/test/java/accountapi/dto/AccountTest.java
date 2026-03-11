package accountapi.dto;

import dto.accountapi.Account;
import dto.accountapi.AccountStateEnum;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AccountTest {

    @Test
    void testAccountGettersSetters() {
        Account account = new Account();
        account.setId("ACC123");
        account.setPersonalInfoId(1);
        account.setRoleId(2);
        account.setState(AccountStateEnum.ACTIVE);

        assertEquals("ACC123", account.getId());
        assertEquals(1, account.getPersonalInfoId());
        assertEquals(2, account.getRoleId());
        assertEquals(AccountStateEnum.ACTIVE, account.getState());
    }

    @Test
    void testAccountFluentSetters() {
        Account account = new Account()
                .id("ACC123")
                .personalInfoId(1)
                .roleId(2)
                .state(AccountStateEnum.INACTIVE);

        assertEquals("ACC123", account.getId());
        assertEquals(1, account.getPersonalInfoId());
        assertEquals(2, account.getRoleId());
        assertEquals(AccountStateEnum.INACTIVE, account.getState());
    }

    @Test
    void testAccountEquals() {
        Account a1 = new Account().id("ACC123").personalInfoId(1).roleId(2).state(AccountStateEnum.ACTIVE);
        Account a2 = new Account().id("ACC123").personalInfoId(1).roleId(2).state(AccountStateEnum.ACTIVE);
        Account a3 = new Account().id("OTHER").personalInfoId(1).roleId(2).state(AccountStateEnum.ACTIVE);

        assertEquals(a1, a2);
        assertEquals(a1, a1);
        assertNotEquals(a1, a3);
        assertNotEquals(a1, null);
        assertNotEquals(a1, "string");
    }

    @Test
    void testAccountHashCode() {
        Account a1 = new Account().id("ACC123").personalInfoId(1).roleId(2).state(AccountStateEnum.ACTIVE);
        Account a2 = new Account().id("ACC123").personalInfoId(1).roleId(2).state(AccountStateEnum.ACTIVE);

        assertEquals(a1.hashCode(), a2.hashCode());
    }

    @Test
    void testAccountToString() {
        Account account = new Account().id("ACC123").personalInfoId(1).roleId(2).state(AccountStateEnum.ACTIVE);
        String str = account.toString();

        assertNotNull(str);
        assertTrue(str.contains("ACC123"));
        assertTrue(str.contains("class Account"));
    }

    @Test
    void testAccountNullFields() {
        Account account = new Account();

        assertNull(account.getId());
        assertNull(account.getPersonalInfoId());
        assertNull(account.getRoleId());
        assertNull(account.getState());
    }

    @Test
    void testAccountEqualsDifferentFields() {
        Account base = new Account().id("ACC123").personalInfoId(1).roleId(2).state(AccountStateEnum.ACTIVE);
        Account diffPersonalInfo = new Account().id("ACC123").personalInfoId(99).roleId(2).state(AccountStateEnum.ACTIVE);
        Account diffRole = new Account().id("ACC123").personalInfoId(1).roleId(99).state(AccountStateEnum.ACTIVE);
        Account diffState = new Account().id("ACC123").personalInfoId(1).roleId(2).state(AccountStateEnum.INACTIVE);

        assertNotEquals(base, diffPersonalInfo);
        assertNotEquals(base, diffRole);
        assertNotEquals(base, diffState);
    }
}

