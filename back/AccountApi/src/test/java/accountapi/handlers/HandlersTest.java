package accountapi.handlers;

import accountapi.exception.FunctionalException;
import accountapi.exception.NotFoundException;
import accountapi.exception.UnauthorizedException;
import dto.accountapi.Error;
import jakarta.ws.rs.core.Response;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class HandlersTest {

    @Test
    void testNotFoundExceptionHandler() {
        NotFoundExceptionHandler handler = new NotFoundExceptionHandler();
        NotFoundException ex = new NotFoundException("404", "Non trouvé");

        Response response = handler.toResponse(ex);

        assertEquals(404, response.getStatus());
        Error error = (Error) response.getEntity();
        assertEquals("404", error.getCode());
        assertEquals("Non trouvé", error.getMessage());
    }

    @Test
    void testFunctionalExceptionHandler() {
        FunctionalExceptionHandler handler = new FunctionalExceptionHandler();
        FunctionalException ex = new FunctionalException("400", "Requête invalide");

        Response response = handler.toResponse(ex);

        assertEquals(400, response.getStatus());
        Error error = (Error) response.getEntity();
        assertEquals("400", error.getCode());
        assertEquals("Requête invalide", error.getMessage());
    }

    @Test
    void testUnauthorizedExceptionHandler() {
        UnauthorizedExceptionHandler handler = new UnauthorizedExceptionHandler();
        UnauthorizedException ex = new UnauthorizedException("401", "Non autorisé");

        Response response = handler.toResponse(ex);

        assertEquals(401, response.getStatus());
        Error error = (Error) response.getEntity();
        assertEquals("401", error.getCode());
        assertEquals("Non autorisé", error.getMessage());
    }
}

