package com.example.clientAPI.handlers;

import com.example.clientAPI.exception.FunctionalException;
import dto.offerapi.Error;
import jakarta.ws.rs.core.Response;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class FunctionalExceptionHandlerTest {

    private final FunctionalExceptionHandler handler = new FunctionalExceptionHandler();

    @Test
    void testToResponseReturnsBadRequest() {
        FunctionalException ex = new FunctionalException("INVALID_TITLE", "Le titre est obligatoire");

        Response response = handler.toResponse(ex);

        assertEquals(Response.Status.BAD_REQUEST.getStatusCode(), response.getStatus());
    }

    @Test
    void testToResponseBodyContainsCodeAndMessage() {
        FunctionalException ex = new FunctionalException("INVALID_DATES", "Dates invalides");

        Response response = handler.toResponse(ex);
        Error error = (Error) response.getEntity();

        assertEquals("INVALID_DATES", error.getCode());
        assertEquals("Dates invalides", error.getMessage());
    }

    @Test
    void testToResponseMediaTypeIsJson() {
        FunctionalException ex = new FunctionalException("INVALID_INPUT", "Corps manquant");

        Response response = handler.toResponse(ex);

        assertEquals("application/json", response.getMediaType().toString());
    }
}

