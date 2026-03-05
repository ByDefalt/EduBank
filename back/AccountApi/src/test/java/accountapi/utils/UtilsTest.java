package accountapi.utils;

import dto.accountapi.TokenResponse;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UtilsTest {

    @Test
    void testGenerateIdNotNull() {
        String id = GenerateID.generateId();
        assertNotNull(id);
        assertFalse(id.isBlank());
    }

    @Test
    void testGenerateIdIsNumeric() {
        String id = GenerateID.generateId();
        assertDoesNotThrow(() -> Long.parseLong(id));
    }

    @Test
    void testJwtUtilsGenerateAndValidate() {
        JwtUtils jwtUtils = new JwtUtils();
        String token = jwtUtils.generateKey("ACC123", "ADMIN");

        assertNotNull(token);
        assertFalse(token.isBlank());

        TokenResponse response = jwtUtils.validateToken(token);
        assertNotNull(response);
        assertEquals("ACC123", response.getId());
        assertEquals("ADMIN", response.getRole());
    }

    @Test
    void testJwtUtilsValidateInvalidToken() {
        JwtUtils jwtUtils = new JwtUtils();
        TokenResponse response = jwtUtils.validateToken("token.invalide.xyz");
        assertNull(response);
    }

    @Test
    void testJwtUtilsValidateExpiredToken() {
        JwtUtils jwtUtils = new JwtUtils();
        String fakeExpiredToken = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJBQ0MxMjMiLCJpYXQiOjE3MDAwMDAwMDAsImV4cCI6MTcwMDAwMDAwMX0.invalidsig";
        TokenResponse response = jwtUtils.validateToken(fakeExpiredToken);
        assertNull(response);
    }
}

