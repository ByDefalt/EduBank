package com.example.clientAPI.handlers;

import com.example.clientAPI.exception.UnauthorizedException;
import dto.offerapi.Error;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

@Provider
public class UnauthorizedExceptionHandler implements ExceptionMapper<UnauthorizedException> {

    @Override
    public Response toResponse(UnauthorizedException exception) {
        Error error = new Error();
        error.setCode(exception.getCode());
        error.setMessage(exception.getMessage());
        return Response.status(Response.Status.UNAUTHORIZED).type(MediaType.APPLICATION_JSON).entity(error).build();
    }
}
