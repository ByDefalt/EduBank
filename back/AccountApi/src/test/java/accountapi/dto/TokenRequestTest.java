package accountapi.dto;

import dto.accountapi.TokenRequest;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TokenRequestTest {

    @Test
    void testTokenRequestGettersSetters() {
        TokenRequest tr = new TokenRequest();
        tr.setJwt("eyJhbGciOiJIUzI1NiJ9...");

        assertEquals("eyJhbGciOiJIUzI1NiJ9...", tr.getJwt());
    }

    @Test
    void testTokenRequestFluentSetters() {
        TokenRequest tr = new TokenRequest().jwt("eyJhbGciOiJIUzI1NiJ9...");

        assertEquals("eyJhbGciOiJIUzI1NiJ9...", tr.getJwt());
    }

    @Test
    void testTokenRequestEquals() {
        TokenRequest t1 = new TokenRequest().jwt("token123");
        TokenRequest t2 = new TokenRequest().jwt("token123");
        TokenRequest t3 = new TokenRequest().jwt("other");

        assertEquals(t1, t2);
        assertEquals(t1, t1);
        assertNotEquals(t1, t3);
        assertNotEquals(t1, null);
        assertNotEquals(t1, "string");
    }

    @Test
    void testTokenRequestHashCode() {
        TokenRequest t1 = new TokenRequest().jwt("token123");
        TokenRequest t2 = new TokenRequest().jwt("token123");

        assertEquals(t1.hashCode(), t2.hashCode());
    }

    @Test
    void testTokenRequestToString() {
        TokenRequest tr = new TokenRequest().jwt("mytoken");
        String str = tr.toString();

        assertNotNull(str);
        assertTrue(str.contains("class TokenRequest"));
        assertTrue(str.contains("mytoken"));
    }

    @Test
    void testTokenRequestNullFields() {
        TokenRequest tr = new TokenRequest();

        assertNull(tr.getJwt());
    }
}

