package com.example.clientAPI.handlers;

import com.example.clientAPI.exception.UnauthorizedException;
import dto.offerapi.Error;
import jakarta.ws.rs.core.Response;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UnauthorizedExceptionHandlerTest {

    private final UnauthorizedExceptionHandler handler = new UnauthorizedExceptionHandler();

    @Test
    void testToResponseReturnsUnauthorized() {
        UnauthorizedException ex = new UnauthorizedException("UNAUTHORIZED", "Token manquant ou invalide");

        Response response = handler.toResponse(ex);

        assertEquals(Response.Status.UNAUTHORIZED.getStatusCode(), response.getStatus());
    }

    @Test
    void testToResponseBodyContainsCodeAndMessage() {
        UnauthorizedException ex = new UnauthorizedException("UNAUTHORIZED", "Token manquant ou invalide");

        Response response = handler.toResponse(ex);
        Error error = (Error) response.getEntity();

        assertEquals("UNAUTHORIZED", error.getCode());
        assertEquals("Token manquant ou invalide", error.getMessage());
    }

    @Test
    void testToResponseMediaTypeIsJson() {
        UnauthorizedException ex = new UnauthorizedException("UNAUTHORIZED", "Token manquant ou invalide");

        Response response = handler.toResponse(ex);

        assertEquals("application/json", response.getMediaType().toString());
    }
}

