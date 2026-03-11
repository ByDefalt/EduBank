package com.example.clientAPI.handlers;

import com.example.clientAPI.exception.NotFoundException;
import dto.offerapi.Error;
import jakarta.ws.rs.core.Response;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class NotFoundExceptionHandlerTest {

    private final NotFoundExceptionHandler handler = new NotFoundExceptionHandler();

    @Test
    void testToResponseReturnsNotFound() {
        NotFoundException ex = new NotFoundException("OFFER_NOT_FOUND", "Offre introuvable avec l'id : 99");

        Response response = handler.toResponse(ex);

        assertEquals(Response.Status.NOT_FOUND.getStatusCode(), response.getStatus());
    }

    @Test
    void testToResponseBodyContainsCodeAndMessage() {
        NotFoundException ex = new NotFoundException("OFFER_NOT_FOUND", "Offre introuvable avec l'id : 99");

        Response response = handler.toResponse(ex);
        Error error = (Error) response.getEntity();

        assertEquals("OFFER_NOT_FOUND", error.getCode());
        assertEquals("Offre introuvable avec l'id : 99", error.getMessage());
    }

    @Test
    void testToResponseMediaTypeIsJson() {
        NotFoundException ex = new NotFoundException("OFFER_NOT_FOUND", "Offre introuvable avec l'id : 99");

        Response response = handler.toResponse(ex);

        assertEquals("application/json", response.getMediaType().toString());
    }
}

