package com.example.clientAPI.controller;

import com.example.clientAPI.annotation.AuthenticationRequired;
import com.example.clientAPI.business.BankAccountParameterBusiness;
import dto.accountapi.RoleEnum;
import dto.bankapi.BankAccountParameter;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.springframework.stereotype.Controller;

import java.util.List;

@Controller
@Path("/")
public class BankAccountParameterController {
    private final BankAccountParameterBusiness bankAccountParameterBusiness;

    public BankAccountParameterController(BankAccountParameterBusiness bankAccountParameterBusiness) {
        this.bankAccountParameterBusiness = bankAccountParameterBusiness;
    }

    @GET
    @Path("/parameters")
    @Produces(MediaType.APPLICATION_JSON)
    @AuthenticationRequired(RoleEnum.CUSTOMER)
    public Response getParameters() {
        List<BankAccountParameter> parameters = bankAccountParameterBusiness.getAllParameters();
        return Response.ok(parameters).build();
    }

    @POST
    @Path("/parameters")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @AuthenticationRequired(RoleEnum.ADMIN)
    public Response createParameter(BankAccountParameter dto) {
        BankAccountParameter created = bankAccountParameterBusiness.createParameter(dto);
        return Response.status(Response.Status.CREATED).entity(created).build();
    }


}
