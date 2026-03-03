package com.operationapi.controller;

import com.operationapi.business.OperationBusiness;
import dto.operationapi.Beneficiary;
import dto.operationapi.Operation;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Produces;
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
        Operation savedOperation = this.operationBusiness.save(operation);
        return Response.ok(savedOperation).build();
    }
}
