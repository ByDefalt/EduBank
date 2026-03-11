package accountapi.dto;

import dto.accountapi.PersonalInformationRegister;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PersonalInformationRegisterTest {

    @Test
    void testPersonalInformationRegisterGettersSetters() {
        PersonalInformationRegister pir = new PersonalInformationRegister();
        pir.setFirstname("John");
        pir.setLastname("Doe");
        pir.setEmail("john@doe.com");
        pir.setAddress("123 Main St");
        pir.setPhoneNumber("0123456789");

        assertEquals("John", pir.getFirstname());
        assertEquals("Doe", pir.getLastname());
        assertEquals("john@doe.com", pir.getEmail());
        assertEquals("123 Main St", pir.getAddress());
        assertEquals("0123456789", pir.getPhoneNumber());
    }

    @Test
    void testPersonalInformationRegisterFluentSetters() {
        PersonalInformationRegister pir = new PersonalInformationRegister()
                .firstname("John")
                .lastname("Doe")
                .email("john@doe.com")
                .address("123 Main St")
                .phoneNumber("0123456789");

        assertEquals("John", pir.getFirstname());
        assertEquals("Doe", pir.getLastname());
        assertEquals("john@doe.com", pir.getEmail());
        assertEquals("123 Main St", pir.getAddress());
        assertEquals("0123456789", pir.getPhoneNumber());
    }

    @Test
    void testPersonalInformationRegisterEquals() {
        PersonalInformationRegister p1 = new PersonalInformationRegister().firstname("John").lastname("Doe").email("j@d.com").address("addr").phoneNumber("012");
        PersonalInformationRegister p2 = new PersonalInformationRegister().firstname("John").lastname("Doe").email("j@d.com").address("addr").phoneNumber("012");
        PersonalInformationRegister p3 = new PersonalInformationRegister().firstname("Jane").lastname("Doe").email("j@d.com").address("addr").phoneNumber("012");

        assertEquals(p1, p2);
        assertEquals(p1, p1);
        assertNotEquals(p1, p3);
        assertNotEquals(p1, null);
        assertNotEquals(p1, "string");
    }

    @Test
    void testPersonalInformationRegisterHashCode() {
        PersonalInformationRegister p1 = new PersonalInformationRegister().firstname("John").lastname("Doe").email("j@d.com");
        PersonalInformationRegister p2 = new PersonalInformationRegister().firstname("John").lastname("Doe").email("j@d.com");

        assertEquals(p1.hashCode(), p2.hashCode());
    }

    @Test
    void testPersonalInformationRegisterToString() {
        PersonalInformationRegister pir = new PersonalInformationRegister().firstname("John").lastname("Doe");
        String str = pir.toString();

        assertNotNull(str);
        assertTrue(str.contains("class PersonalInformationRegister"));
        assertTrue(str.contains("John"));
    }

    @Test
    void testPersonalInformationRegisterNullFields() {
        PersonalInformationRegister pir = new PersonalInformationRegister();

        assertNull(pir.getFirstname());
        assertNull(pir.getLastname());
        assertNull(pir.getEmail());
        assertNull(pir.getAddress());
        assertNull(pir.getPhoneNumber());
    }

    @Test
    void testPersonalInformationRegisterEqualsDifferentFields() {
        PersonalInformationRegister base = new PersonalInformationRegister().firstname("John").lastname("Doe").email("j@d.com").address("addr").phoneNumber("012");
        PersonalInformationRegister diffLastname = new PersonalInformationRegister().firstname("John").lastname("Smith").email("j@d.com").address("addr").phoneNumber("012");
        PersonalInformationRegister diffEmail = new PersonalInformationRegister().firstname("John").lastname("Doe").email("other@d.com").address("addr").phoneNumber("012");
        PersonalInformationRegister diffAddress = new PersonalInformationRegister().firstname("John").lastname("Doe").email("j@d.com").address("other").phoneNumber("012");
        PersonalInformationRegister diffPhone = new PersonalInformationRegister().firstname("John").lastname("Doe").email("j@d.com").address("addr").phoneNumber("999");

        assertNotEquals(base, diffLastname);
        assertNotEquals(base, diffEmail);
        assertNotEquals(base, diffAddress);
        assertNotEquals(base, diffPhone);
    }
}

