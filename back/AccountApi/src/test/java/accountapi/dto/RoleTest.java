package accountapi.dto;

import dto.accountapi.Role;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class RoleTest {

    @Test
    void testRoleGettersSetters() {
        Role role = new Role();
        role.setId(1);
        role.setName("ADMIN");

        assertEquals(1, role.getId());
        assertEquals("ADMIN", role.getName());
    }

    @Test
    void testRoleFluentSetters() {
        Role role = new Role().id(1).name("CUSTOMER");

        assertEquals(1, role.getId());
        assertEquals("CUSTOMER", role.getName());
    }

    @Test
    void testRoleEquals() {
        Role r1 = new Role().id(1).name("ADMIN");
        Role r2 = new Role().id(1).name("ADMIN");
        Role r3 = new Role().id(2).name("CUSTOMER");

        assertEquals(r1, r2);
        assertEquals(r1, r1);
        assertNotEquals(r1, r3);
        assertNotEquals(r1, null);
        assertNotEquals(r1, "string");
    }

    @Test
    void testRoleHashCode() {
        Role r1 = new Role().id(1).name("ADMIN");
        Role r2 = new Role().id(1).name("ADMIN");

        assertEquals(r1.hashCode(), r2.hashCode());
    }

    @Test
    void testRoleToString() {
        Role role = new Role().id(1).name("ADMIN");
        String str = role.toString();

        assertNotNull(str);
        assertTrue(str.contains("class Role"));
        assertTrue(str.contains("ADMIN"));
    }

    @Test
    void testRoleNullFields() {
        Role role = new Role();

        assertNull(role.getId());
        assertNull(role.getName());
    }

    @Test
    void testRoleEqualsDifferentName() {
        Role r1 = new Role().id(1).name("ADMIN");
        Role r2 = new Role().id(1).name("CUSTOMER");

        assertNotEquals(r1, r2);
    }
}

