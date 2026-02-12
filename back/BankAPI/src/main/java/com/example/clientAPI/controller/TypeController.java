package com.example.clientAPI.controller;

import com.example.clientAPI.annotation.AuthenticationRequired;
import com.example.clientAPI.business.TypeBusiness;
import dto.accountapi.RoleEnum;
import dto.bankapi.Type;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.springframework.stereotype.Controller;

import java.util.List;

@Controller
@Path("/")
public class TypeController {
    private final TypeBusiness typeBusiness;

    public TypeController(TypeBusiness typeBusiness) {
        this.typeBusiness = typeBusiness;
    }

    @GET
    @Path("/types")
    @Produces(MediaType.APPLICATION_JSON)
    @AuthenticationRequired(RoleEnum.CUSTOMER)
    public Response getTypes() {
        List<Type> types = typeBusiness.getAllTypes();
        return Response.ok(types).build();
    }

    @POST
    @Path("/types")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @AuthenticationRequired(RoleEnum.ADMIN)
    public Response createType(Type dto) {
        Type created = typeBusiness.createType(dto);
        return Response.status(Response.Status.CREATED).entity(created).build();
    }

}
