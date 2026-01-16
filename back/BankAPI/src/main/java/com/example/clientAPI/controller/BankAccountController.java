package com.example.clientAPI.controller;

import com.example.clientAPI.business.BankAccountBusiness;
import dto.bankapiswagger.BankAccount;
import jakarta.ws.rs.*;

import jakarta.ws.rs.core.Response;
import org.springframework.stereotype.Controller;

import java.util.List;

@Controller
@Path("/bank-accounts")
public class BankAccountController {

    private final BankAccountBusiness business;

    public BankAccountController(BankAccountBusiness business) {
        this.business = business;
    }

    @GET
    public List<BankAccount> getAll() {
        return business.getAll();
    }

    @GET
    @Path("/{id}")
    public BankAccount getById(@PathParam("id") String id) {
        return business.getById(id);
    }

    @POST
    public Response create(BankAccount dto) {
        return Response.status(Response.Status.CREATED)
                .entity(business.create(dto))
                .build();
    }

    @PUT
    @Path("/{id}")
    public BankAccount update(@PathParam("id") String id, BankAccount dto) {
        return business.update(id, dto);
    }

    @DELETE
    @Path("/{id}")
    public Response delete(@PathParam("id") String id) {
        business.delete(id);
        return Response.noContent().build();
    }
}
