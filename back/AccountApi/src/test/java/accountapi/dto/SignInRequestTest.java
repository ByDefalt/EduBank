package accountapi.dto;

import dto.accountapi.SignInRequest;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SignInRequestTest {

    @Test
    void testSignInRequestGettersSetters() {
        SignInRequest sir = new SignInRequest();
        sir.setId("user123");
        sir.setPassword("password");

        assertEquals("user123", sir.getId());
        assertEquals("password", sir.getPassword());
    }

    @Test
    void testSignInRequestFluentSetters() {
        SignInRequest sir = new SignInRequest().id("user123").password("password");

        assertEquals("user123", sir.getId());
        assertEquals("password", sir.getPassword());
    }

    @Test
    void testSignInRequestEquals() {
        SignInRequest s1 = new SignInRequest().id("user123").password("pass");
        SignInRequest s2 = new SignInRequest().id("user123").password("pass");
        SignInRequest s3 = new SignInRequest().id("other").password("pass");

        assertEquals(s1, s2);
        assertEquals(s1, s1);
        assertNotEquals(s1, s3);
        assertNotEquals(s1, null);
        assertNotEquals(s1, "string");
    }

    @Test
    void testSignInRequestHashCode() {
        SignInRequest s1 = new SignInRequest().id("user123").password("pass");
        SignInRequest s2 = new SignInRequest().id("user123").password("pass");

        assertEquals(s1.hashCode(), s2.hashCode());
    }

    @Test
    void testSignInRequestToString() {
        SignInRequest sir = new SignInRequest().id("user123").password("pass");
        String str = sir.toString();

        assertNotNull(str);
        assertTrue(str.contains("class SignInRequest"));
        assertTrue(str.contains("user123"));
    }

    @Test
    void testSignInRequestNullFields() {
        SignInRequest sir = new SignInRequest();

        assertNull(sir.getId());
        assertNull(sir.getPassword());
    }

    @Test
    void testSignInRequestEqualsDifferentPassword() {
        SignInRequest s1 = new SignInRequest().id("user123").password("pass1");
        SignInRequest s2 = new SignInRequest().id("user123").password("pass2");

        assertNotEquals(s1, s2);
    }
}

