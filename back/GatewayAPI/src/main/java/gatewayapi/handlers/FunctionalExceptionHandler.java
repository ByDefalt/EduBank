package gatewayapi.handlers;

import dto.operationapi.Error;
import gatewayapi.exception.FunctionalException;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

@Provider
public class FunctionalExceptionHandler implements ExceptionMapper<FunctionalException> {

    @Override
    public Response toResponse(FunctionalException exception) {
        dto.operationapi.Error error = new Error();
        error.setCode(exception.getCode());
        error.setMessage(exception.getMessage());
        return Response.status(Response.Status.BAD_REQUEST).type(MediaType.APPLICATION_JSON).entity(error).build();
    }
}
