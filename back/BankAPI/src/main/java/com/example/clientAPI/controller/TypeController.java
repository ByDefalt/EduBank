package com.example.clientAPI.controller;

import com.example.clientAPI.business.TypeBusiness;
import dto.bankapi.Type;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.springframework.stereotype.Controller;

import java.util.List;

@Controller
@Path("bank/admin/types")
public class TypeController {

    private final TypeBusiness typeBusiness;

    public TypeController(TypeBusiness typeBusiness) {
        this.typeBusiness = typeBusiness;
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getTypes() {
        List<Type> dtos = typeBusiness.getAllTypes();
        return Response.ok(dtos).build();
    }

    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getTypeById(@PathParam("id") Integer id) {
        Type dto = typeBusiness.getTypeById(id);
        return Response.ok(dto).build();
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response createType(Type requestDto) {
        Type createdDto = typeBusiness.createType(requestDto);
        return Response.status(Response.Status.CREATED).entity(createdDto).build();
    }
}