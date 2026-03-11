package accountapi.dto;

import dto.accountapi.TokenResponse;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TokenResponseTest {

    @Test
    void testTokenResponseGettersSetters() {
        TokenResponse tr = new TokenResponse();
        tr.setId("user123");
        tr.setRole("ADMIN");

        assertEquals("user123", tr.getId());
        assertEquals("ADMIN", tr.getRole());
    }

    @Test
    void testTokenResponseFluentSetters() {
        TokenResponse tr = new TokenResponse().id("user123").role("ADMIN");

        assertEquals("user123", tr.getId());
        assertEquals("ADMIN", tr.getRole());
    }

    @Test
    void testTokenResponseEquals() {
        TokenResponse t1 = new TokenResponse().id("user123").role("ADMIN");
        TokenResponse t2 = new TokenResponse().id("user123").role("ADMIN");
        TokenResponse t3 = new TokenResponse().id("other").role("ADMIN");

        assertEquals(t1, t2);
        assertEquals(t1, t1);
        assertNotEquals(t1, t3);
        assertNotEquals(t1, null);
        assertNotEquals(t1, "string");
    }

    @Test
    void testTokenResponseHashCode() {
        TokenResponse t1 = new TokenResponse().id("user123").role("ADMIN");
        TokenResponse t2 = new TokenResponse().id("user123").role("ADMIN");

        assertEquals(t1.hashCode(), t2.hashCode());
    }

    @Test
    void testTokenResponseToString() {
        TokenResponse tr = new TokenResponse().id("user123").role("ADMIN");
        String str = tr.toString();

        assertNotNull(str);
        assertTrue(str.contains("class TokenResponse"));
        assertTrue(str.contains("user123"));
        assertTrue(str.contains("ADMIN"));
    }

    @Test
    void testTokenResponseNullFields() {
        TokenResponse tr = new TokenResponse();

        assertNull(tr.getId());
        assertNull(tr.getRole());
    }

    @Test
    void testTokenResponseEqualsDifferentRole() {
        TokenResponse t1 = new TokenResponse().id("user123").role("ADMIN");
        TokenResponse t2 = new TokenResponse().id("user123").role("CUSTOMER");

        assertNotEquals(t1, t2);
    }
}

