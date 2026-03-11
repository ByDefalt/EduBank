package accountapi.dto;

import dto.accountapi.AccountStateEnum;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AccountStateEnumTest {

    @Test
    void testAccountStateEnumValues() {
        assertEquals(3, AccountStateEnum.values().length);
        assertEquals(AccountStateEnum.ACTIVE, AccountStateEnum.valueOf("ACTIVE"));
        assertEquals(AccountStateEnum.INACTIVE, AccountStateEnum.valueOf("INACTIVE"));
        assertEquals(AccountStateEnum.ENCLOSE, AccountStateEnum.valueOf("ENCLOSE"));
    }

    @Test
    void testAccountStateEnumFromValue() {
        assertEquals(AccountStateEnum.ACTIVE, AccountStateEnum.fromValue("ACTIVE"));
        assertEquals(AccountStateEnum.INACTIVE, AccountStateEnum.fromValue("INACTIVE"));
        assertEquals(AccountStateEnum.ENCLOSE, AccountStateEnum.fromValue("ENCLOSE"));
        assertNull(AccountStateEnum.fromValue("UNKNOWN"));
    }

    @Test
    void testAccountStateEnumToString() {
        assertEquals("ACTIVE", AccountStateEnum.ACTIVE.toString());
        assertEquals("INACTIVE", AccountStateEnum.INACTIVE.toString());
        assertEquals("ENCLOSE", AccountStateEnum.ENCLOSE.toString());
    }
}

