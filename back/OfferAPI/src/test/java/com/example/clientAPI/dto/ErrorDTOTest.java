package com.example.clientAPI.dto;

import dto.offerapi.Error;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ErrorDTOTest {

    @Test
    void testErrorGettersSetters() {
        Error error = new Error();
        error.setCode("400");
        error.setMessage("Bad Request");
        error.setDetails("Invalid field");

        assertEquals("400", error.getCode());
        assertEquals("Bad Request", error.getMessage());
        assertEquals("Invalid field", error.getDetails());
    }

    @Test
    void testErrorFluentSetters() {
        Error error = new Error()
                .code("404")
                .message("Not Found")
                .details("Resource not found");

        assertEquals("404", error.getCode());
        assertEquals("Not Found", error.getMessage());
        assertEquals("Resource not found", error.getDetails());
    }

    @Test
    void testErrorEquals() {
        Error e1 = new Error().code("400").message("Bad").details("detail");
        Error e2 = new Error().code("400").message("Bad").details("detail");
        Error e3 = new Error().code("500").message("Bad").details("detail");

        assertEquals(e1, e2);
        assertEquals(e1, e1);
        assertNotEquals(e1, e3);
        assertNotEquals(e1, null);
        assertNotEquals(e1, "string");
    }

    @Test
    void testErrorHashCode() {
        Error e1 = new Error().code("400").message("Bad").details("detail");
        Error e2 = new Error().code("400").message("Bad").details("detail");

        assertEquals(e1.hashCode(), e2.hashCode());
    }

    @Test
    void testErrorToString() {
        Error error = new Error().code("400").message("Bad");
        String str = error.toString();
        assertNotNull(str);
        assertTrue(str.contains("400"));
        assertTrue(str.contains("Bad"));
    }

    @Test
    void testErrorNullFields() {
        Error error = new Error();
        assertNull(error.getCode());
        assertNull(error.getMessage());
        assertNull(error.getDetails());
    }
}

