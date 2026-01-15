package com.example.clientAPI.controller;


import com.example.clientAPI.business.AccountBusiness;
import dto.accountApi.Account;
import dto.accountApi.JwtToken;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@Path("/account")
public class AccountController {

    private final AccountBusiness accountBusiness;

    public AccountController(AccountBusiness accountBusiness) {
        this.accountBusiness = accountBusiness;
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response createAccount(Account dto) {
        Account created = accountBusiness.createAccount(dto);
        return Response.ok(created).build();
    }

    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAccountById(@PathParam("id") String id) {
        Account account = accountBusiness.getAccountById(id);
        if (account == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.ok(account).build();
    }

    @PUT
    @Path("/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateAccount(@PathParam("id") String id, Account dto) {
        Account updated = accountBusiness.updateAccount(id, dto);
        return Response.ok(updated).build();
    }

    @DELETE
    @Path("/{id}")
    public Response deleteAccount(@PathParam("id") String id) {
        accountBusiness.deleteAccount(id);
        return Response.noContent().build();
    }

    @POST
    @Path("/signin")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response signIn(Account dto) {
        JwtToken token = accountBusiness.signIn(dto);
        return Response.ok(token).build();
    }

    @POST
    @Path("/validate")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response validateToken(@HeaderParam("Authorization") String token) {
        if( token == null || !token.startsWith("Bearer ")) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }
        boolean isValid = accountBusiness.validateToken(token.split("Bearer ")[1]);
        if(!isValid){
            return Response.status(Response.Status.UNAUTHORIZED).build();
        }else{
            JwtToken jwtToken = new JwtToken();
            jwtToken.setToken(token);
            return Response.ok(jwtToken).build();
        }
    }
}
