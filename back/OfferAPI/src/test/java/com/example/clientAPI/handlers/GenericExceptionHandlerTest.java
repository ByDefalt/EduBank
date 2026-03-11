package com.example.clientAPI.handlers;

import dto.offerapi.Error;
import jakarta.ws.rs.core.Response;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class GenericExceptionHandlerTest {

    private final GenericExceptionHandler handler = new GenericExceptionHandler();

    @Test
    void testToResponseReturnsInternalServerError() {
        Exception ex = new RuntimeException("Erreur inattendue");

        Response response = handler.toResponse(ex);

        assertEquals(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode(), response.getStatus());
    }

    @Test
    void testToResponseBodyContainsGenericMessage() {
        Exception ex = new RuntimeException("Erreur inattendue");

        Response response = handler.toResponse(ex);
        Error error = (Error) response.getEntity();

        assertEquals("500", error.getCode());
        assertEquals("Erreur interne du serveur", error.getMessage());
    }

    @Test
    void testToResponseMediaTypeIsJson() {
        Exception ex = new NullPointerException("null");

        Response response = handler.toResponse(ex);

        assertEquals("application/json", response.getMediaType().toString());
    }

    @Test
    void testToResponseAnyExceptionType() {
        Exception ex = new IllegalArgumentException("argument illégal");

        Response response = handler.toResponse(ex);
        Error error = (Error) response.getEntity();

        assertEquals(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode(), response.getStatus());
        assertEquals("500", error.getCode());
    }
}

