package accountapi.dto;

import dto.accountapi.RoleEnum;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class RoleEnumTest {

    @Test
    void testRoleEnumValues() {
        assertEquals(2, RoleEnum.values().length);
        assertEquals(RoleEnum.ADMIN, RoleEnum.valueOf("ADMIN"));
        assertEquals(RoleEnum.CUSTOMER, RoleEnum.valueOf("CUSTOMER"));
    }

    @Test
    void testRoleEnumFromValue() {
        assertEquals(RoleEnum.ADMIN, RoleEnum.fromValue("ADMIN"));
        assertEquals(RoleEnum.CUSTOMER, RoleEnum.fromValue("CUSTOMER"));
        assertNull(RoleEnum.fromValue("UNKNOWN"));
    }

    @Test
    void testRoleEnumToString() {
        assertEquals("ADMIN", RoleEnum.ADMIN.toString());
        assertEquals("CUSTOMER", RoleEnum.CUSTOMER.toString());
    }
}

