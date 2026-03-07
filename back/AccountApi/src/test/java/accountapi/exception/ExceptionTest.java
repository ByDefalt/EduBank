package accountapi.exception;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ExceptionTest {

    @Test
    void testNotFoundException() {
        NotFoundException ex = new NotFoundException("404", "Ressource non trouvée");
        assertEquals("404", ex.getCode());
        assertEquals("Ressource non trouvée", ex.getMessage());
        assertInstanceOf(RuntimeException.class, ex);
    }

    @Test
    void testFunctionalException() {
        FunctionalException ex = new FunctionalException("400", "Requête invalide");
        assertEquals("400", ex.getCode());
        assertEquals("Requête invalide", ex.getMessage());
        assertInstanceOf(RuntimeException.class, ex);
    }

    @Test
    void testUnauthorizedException() {
        UnauthorizedException ex = new UnauthorizedException("401", "Non autorisé");
        assertEquals("401", ex.getCode());
        assertEquals("Non autorisé", ex.getMessage());
        assertInstanceOf(RuntimeException.class, ex);
    }
}

