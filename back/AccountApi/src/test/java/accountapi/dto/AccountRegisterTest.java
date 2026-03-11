package accountapi.dto;

import dto.accountapi.AccountRegister;
import dto.accountapi.PersonalInformationRegister;
import dto.accountapi.RoleEnum;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AccountRegisterTest {

    @Test
    void testAccountRegisterGettersSetters() {
        PersonalInformationRegister pi = new PersonalInformationRegister();
        pi.setFirstname("John");

        AccountRegister ar = new AccountRegister();
        ar.setPersonalInfo(pi);
        ar.setRole(RoleEnum.ADMIN);
        ar.setPassword("password123");

        assertEquals(pi, ar.getPersonalInfo());
        assertEquals(RoleEnum.ADMIN, ar.getRole());
        assertEquals("password123", ar.getPassword());
    }

    @Test
    void testAccountRegisterFluentSetters() {
        PersonalInformationRegister pi = new PersonalInformationRegister().firstname("John");

        AccountRegister ar = new AccountRegister()
                .personalInfo(pi)
                .role(RoleEnum.CUSTOMER)
                .password("pass");

        assertEquals(pi, ar.getPersonalInfo());
        assertEquals(RoleEnum.CUSTOMER, ar.getRole());
        assertEquals("pass", ar.getPassword());
    }

    @Test
    void testAccountRegisterEquals() {
        PersonalInformationRegister pi = new PersonalInformationRegister().firstname("John").lastname("Doe").email("j@d.com");
        AccountRegister ar1 = new AccountRegister().personalInfo(pi).role(RoleEnum.ADMIN).password("pass");
        AccountRegister ar2 = new AccountRegister().personalInfo(pi).role(RoleEnum.ADMIN).password("pass");
        AccountRegister ar3 = new AccountRegister().personalInfo(pi).role(RoleEnum.CUSTOMER).password("pass");

        assertEquals(ar1, ar2);
        assertEquals(ar1, ar1);
        assertNotEquals(ar1, ar3);
        assertNotEquals(ar1, null);
        assertNotEquals(ar1, "string");
    }

    @Test
    void testAccountRegisterHashCode() {
        PersonalInformationRegister pi = new PersonalInformationRegister().firstname("John");
        AccountRegister ar1 = new AccountRegister().personalInfo(pi).role(RoleEnum.ADMIN).password("pass");
        AccountRegister ar2 = new AccountRegister().personalInfo(pi).role(RoleEnum.ADMIN).password("pass");

        assertEquals(ar1.hashCode(), ar2.hashCode());
    }

    @Test
    void testAccountRegisterToString() {
        AccountRegister ar = new AccountRegister().password("secret").role(RoleEnum.ADMIN);
        String str = ar.toString();

        assertNotNull(str);
        assertTrue(str.contains("class AccountRegister"));
        assertTrue(str.contains("secret"));
    }

    @Test
    void testAccountRegisterNullFields() {
        AccountRegister ar = new AccountRegister();

        assertNull(ar.getPersonalInfo());
        assertNull(ar.getRole());
        assertNull(ar.getPassword());
    }

    @Test
    void testAccountRegisterEqualsDifferentFields() {
        PersonalInformationRegister pi1 = new PersonalInformationRegister().firstname("John");
        PersonalInformationRegister pi2 = new PersonalInformationRegister().firstname("Jane");
        AccountRegister base = new AccountRegister().personalInfo(pi1).role(RoleEnum.ADMIN).password("pass");
        AccountRegister diffPI = new AccountRegister().personalInfo(pi2).role(RoleEnum.ADMIN).password("pass");
        AccountRegister diffPassword = new AccountRegister().personalInfo(pi1).role(RoleEnum.ADMIN).password("other");

        assertNotEquals(base, diffPI);
        assertNotEquals(base, diffPassword);
    }
}

