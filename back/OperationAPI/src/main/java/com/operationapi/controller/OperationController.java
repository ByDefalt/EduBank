package com.operationapi.controller;

import com.operationapi.business.OperationBusiness;
import dto.operationapi.Operation;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.List;

public class OperationController {
    private final OperationBusiness operationBusiness;

    public OperationController(OperationBusiness operationBusiness) {
        this.operationBusiness = operationBusiness;
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getOperations() {
        List<Operation> operations = this.operationBusiness.getOperations();
        return Response.ok(operations).build();
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response saveOperation(Operation operation) {
        this.operationBusiness.save(operation);
        return Response.ok(Response.Status.CREATED).build();
    }

    @POST
    @Path("/{operationId}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response getOperation(@PathParam("operationId") String operationId) {
        Operation operation = this.operationBusiness.getOperationById(Integer.valueOf(operationId));
        if (operation == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.ok(operation).build();
    }

    @POST
    @Path("/{operationId}/state")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateStateOperation(@PathParam("operationId") String operationId, Operation.StateEnum state) {
        Operation operation = this.operationBusiness.updateStateOperation(Integer.valueOf(operationId), state);
        if (operation == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.ok(operation).build();
    }

    @POST
    @Path("/{operationId}/cancel")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateCancelOperation(@PathParam("operationId") String operationId) {
        Operation operation = this.operationBusiness.cancelOperation(Integer.valueOf(operationId));
        if (operation == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.ok(operation).build();
    }
}
