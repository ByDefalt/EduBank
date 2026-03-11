package accountapi.dto;

import dto.accountapi.PersonalInformation;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PersonalInformationTest {

    @Test
    void testPersonalInformationGettersSetters() {
        PersonalInformation pi = new PersonalInformation();
        pi.setId(1);
        pi.setFirstname("John");
        pi.setLastname("Doe");
        pi.setEmail("john@doe.com");
        pi.setAddress("123 Main St");
        pi.setPhoneNumber("0123456789");

        assertEquals(1, pi.getId());
        assertEquals("John", pi.getFirstname());
        assertEquals("Doe", pi.getLastname());
        assertEquals("john@doe.com", pi.getEmail());
        assertEquals("123 Main St", pi.getAddress());
        assertEquals("0123456789", pi.getPhoneNumber());
    }

    @Test
    void testPersonalInformationFluentSetters() {
        PersonalInformation pi = new PersonalInformation()
                .id(1)
                .firstname("John")
                .lastname("Doe")
                .email("john@doe.com")
                .address("123 Main St")
                .phoneNumber("0123456789");

        assertEquals(1, pi.getId());
        assertEquals("John", pi.getFirstname());
        assertEquals("Doe", pi.getLastname());
        assertEquals("john@doe.com", pi.getEmail());
        assertEquals("123 Main St", pi.getAddress());
        assertEquals("0123456789", pi.getPhoneNumber());
    }

    @Test
    void testPersonalInformationEquals() {
        PersonalInformation pi1 = new PersonalInformation().id(1).firstname("John").lastname("Doe").email("j@d.com").address("addr").phoneNumber("012");
        PersonalInformation pi2 = new PersonalInformation().id(1).firstname("John").lastname("Doe").email("j@d.com").address("addr").phoneNumber("012");
        PersonalInformation pi3 = new PersonalInformation().id(2).firstname("Jane").lastname("Doe").email("j@d.com").address("addr").phoneNumber("012");

        assertEquals(pi1, pi2);
        assertEquals(pi1, pi1);
        assertNotEquals(pi1, pi3);
        assertNotEquals(pi1, null);
        assertNotEquals(pi1, "string");
    }

    @Test
    void testPersonalInformationHashCode() {
        PersonalInformation pi1 = new PersonalInformation().id(1).firstname("John").lastname("Doe").email("j@d.com").address("addr").phoneNumber("012");
        PersonalInformation pi2 = new PersonalInformation().id(1).firstname("John").lastname("Doe").email("j@d.com").address("addr").phoneNumber("012");

        assertEquals(pi1.hashCode(), pi2.hashCode());
    }

    @Test
    void testPersonalInformationToString() {
        PersonalInformation pi = new PersonalInformation().id(1).firstname("John").lastname("Doe");
        String str = pi.toString();

        assertNotNull(str);
        assertTrue(str.contains("class PersonalInformation"));
        assertTrue(str.contains("John"));
    }

    @Test
    void testPersonalInformationNullFields() {
        PersonalInformation pi = new PersonalInformation();

        assertNull(pi.getId());
        assertNull(pi.getFirstname());
        assertNull(pi.getLastname());
        assertNull(pi.getEmail());
        assertNull(pi.getAddress());
        assertNull(pi.getPhoneNumber());
    }

    @Test
    void testPersonalInformationEqualsDifferentFields() {
        PersonalInformation base = new PersonalInformation().id(1).firstname("John").lastname("Doe").email("j@d.com").address("addr").phoneNumber("012");
        PersonalInformation diffLastname = new PersonalInformation().id(1).firstname("John").lastname("Smith").email("j@d.com").address("addr").phoneNumber("012");
        PersonalInformation diffEmail = new PersonalInformation().id(1).firstname("John").lastname("Doe").email("other@d.com").address("addr").phoneNumber("012");
        PersonalInformation diffAddress = new PersonalInformation().id(1).firstname("John").lastname("Doe").email("j@d.com").address("other").phoneNumber("012");
        PersonalInformation diffPhone = new PersonalInformation().id(1).firstname("John").lastname("Doe").email("j@d.com").address("addr").phoneNumber("999");

        assertNotEquals(base, diffLastname);
        assertNotEquals(base, diffEmail);
        assertNotEquals(base, diffAddress);
        assertNotEquals(base, diffPhone);
    }
}

