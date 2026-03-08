package com.operationapi.controller;

import com.operationapi.business.OperationBusiness;
import dto.operationapi.Operation;
import dto.operationapi.OperationFilter;
import dto.operationapi.OperationList;
import dto.operationapi.OperationState;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.springframework.stereotype.Controller;

import java.util.Map;

@Controller
@Path("/operations")
public class OperationController {
    private final OperationBusiness operationBusiness;

    public OperationController(OperationBusiness operationBusiness) {
        this.operationBusiness = operationBusiness;
    }

    @GET
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response getOperations(OperationFilter filter) {
        OperationList operations = this.operationBusiness.getOperations(filter);
        return Response.ok(operations).build();
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response saveOperation(Operation operation) {
        Operation created = this.operationBusiness.save(operation);
        return Response.status(Response.Status.CREATED).entity(created).build();
    }

    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getOperation(@PathParam("id") Integer id) {
        Operation operation = this.operationBusiness.getOperationById(id);
        return Response.ok(operation).build();
    }

    @GET
    @Path("/account/{accountId}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response getOperationsByAccountId(@PathParam("accountId") String accountId, OperationFilter filter) {
        OperationList operations = this.operationBusiness.getOperationsByAccountId(accountId, filter);
        return Response.ok(operations).build();
    }

    @PATCH
    @Path("/{id}/state")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateStateOperation(@PathParam("id") Integer id, OperationState state) {
        Operation operation = this.operationBusiness.updateStateOperation(id, state);
        return Response.ok(operation).build();
    }

    @POST
    @Path("/{id}/cancel")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateCancelOperation(@PathParam("id") Integer id) {
        Operation operationOfCancellation = this.operationBusiness.cancelOperation(id);
        return Response.status(Response.Status.CREATED).entity(operationOfCancellation).build();
    }
}
