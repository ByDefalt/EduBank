package com.operationapi.controller;

import com.operationapi.annotation.AuthenticationRequired;
import com.operationapi.business.OperationBusiness;
import dto.operationapi.Operation;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.springframework.stereotype.Controller;

import java.util.List;
import java.util.Map;

@Controller
@Path("/operations")
@AuthenticationRequired
public class OperationController {
    private final OperationBusiness operationBusiness;

    public OperationController(OperationBusiness operationBusiness) {
        this.operationBusiness = operationBusiness;
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getOperations() {
        List<Operation> operations = this.operationBusiness.getOperations();
        return Response.ok(Map.of("data", operations)).build();
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

    @PATCH
    @Path("/{id}/state")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateStateOperation(@PathParam("id") Integer id, Map<String, String> body) {
        if (body == null || body.get("state") == null) {
            dto.operationapi.Error error = new dto.operationapi.Error();
            error.setCode("400");
            error.setMessage("Le champ 'state' est obligatoire");
            return Response.status(Response.Status.BAD_REQUEST).entity(error).build();
        }
        Operation.StateEnum state;
        try {
            state = Operation.StateEnum.fromValue(body.get("state"));
        } catch (Exception e) {
            dto.operationapi.Error error = new dto.operationapi.Error();
            error.setCode("400");
            error.setMessage("Valeur de 'state' invalide : " + body.get("state"));
            return Response.status(Response.Status.BAD_REQUEST).entity(error).build();
        }
        Operation operation = this.operationBusiness.updateStateOperation(id, state);
        return Response.ok(operation).build();
    }

    @POST
    @Path("/{id}/cancel")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateCancelOperation(@PathParam("id") Integer id) {
        Map<String, Operation> result = this.operationBusiness.cancelOperation(id);
        return Response.status(Response.Status.CREATED).entity(result).build();
    }
}
