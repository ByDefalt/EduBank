package accountapi.handlers;

import accountapi.exceptions.FunctionalException;
import dto.accountapi.Error;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

@Provider
public class FunctionalExceptionHandler implements ExceptionMapper<FunctionalException> {
    @Override
    public Response toResponse(FunctionalException exception) {
        Error error = new Error();
        error.setCode(exception.getCode());
        error.setMessage(exception.getMessage());
        return Response.status(Response
                .Status.BAD_REQUEST)
                .entity(error)
                .build();
    }
}
