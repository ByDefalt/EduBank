package com.example.clientAPI.controller;

import com.example.clientAPI.annotation.AuthenticationRequired;
import com.example.clientAPI.business.BankAccountBusiness;
import dto.accountapi.RoleEnum;
import dto.bankapi.BankAccount;
import dto.bankapi.BankAccountDetail;
import jakarta.ws.rs.*;
import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.springframework.stereotype.Controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@Path("/")
public class BankAccountController {

    private final BankAccountBusiness bankAccountBusiness;

    public BankAccountController(BankAccountBusiness bankAccountBusiness) {
        this.bankAccountBusiness = bankAccountBusiness;
    }

    // ============== BankAccount Endpoints ==============

    @GET
    @Path("/admin/bank-accounts")
    @Produces(MediaType.APPLICATION_JSON)
    @AuthenticationRequired(RoleEnum.ADMIN)
    public Response getAdminAllBankAccounts() {
        List<BankAccount> accounts = bankAccountBusiness.getAllBankAccounts();
        if(accounts==null){
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.ok(accounts).build();
    }

    @GET
    @Path("/admin/bank-accounts/by-account/{account_id}")
    @Produces(MediaType.APPLICATION_JSON)
    @AuthenticationRequired(RoleEnum.ADMIN)
    public Response getAdminBankAccountsByAccountId(@PathParam("account_id") Integer accountId) {
        List<BankAccount> accounts = bankAccountBusiness.getBankAccountsByAccountId(accountId);
        if(accounts==null){
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.ok(accounts).build();
    }

    @GET
    @Path("/admin/bank-accounts/by-type/{type_id}")
    @Produces(MediaType.APPLICATION_JSON)
    @AuthenticationRequired(RoleEnum.ADMIN)
    public Response getAdminBankAccountsByTypeId(@PathParam("type_id") Integer typeId) {
        List<BankAccount> accounts = bankAccountBusiness.getBankAccountsByTypeId(typeId);
        if(accounts==null){
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.ok(accounts).build();
    }

    @GET
    @Path("/my-bank-accounts")
    @Produces(MediaType.APPLICATION_JSON)
    @AuthenticationRequired(RoleEnum.CUSTOMER)
    public Response getMyBankAccounts(
            @Context ContainerRequestContext requestContext) {

        String userId = (String) requestContext.getProperty("userId");
        Integer userIdInt = Integer.parseInt(userId);

        List<BankAccount> accounts = bankAccountBusiness.getActiveBankAccountsByUserId(userIdInt);
        if(accounts==null){
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.ok(accounts).build();
    }

    @GET
    @Path("/my-bank-accounts/by-type/{type_id}")
    @Produces(MediaType.APPLICATION_JSON)
    @AuthenticationRequired(RoleEnum.CUSTOMER)
    public Response getMyBankAccountsByType(
            @PathParam("type_id") Integer typeId,
            @Context ContainerRequestContext requestContext) {

        String userId = (String) requestContext.getProperty("userId");
        Integer userIdInt = Integer.parseInt(userId);

        List<BankAccount> accounts = bankAccountBusiness.getActiveBankAccountsByUserIdAndTypeId(userIdInt, typeId);
        if(accounts==null){
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.ok(accounts).build();
    }


    @POST
    @Path("/bank-accounts")
    @AuthenticationRequired(RoleEnum.ADMIN)
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response createBankAccount(BankAccount dto) {
        BankAccount created = bankAccountBusiness.createBankAccount(dto);
        return Response.status(Response.Status.CREATED).entity(created).build();
    }

    @GET
    @Path("/bank-accounts/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @AuthenticationRequired(RoleEnum.CUSTOMER)//vérifier que le client a le droit d'accéder à ce compte (lié à son compte ou admin)
    public Response getBankAccountById(@PathParam("id") String id) {

        BankAccount account = bankAccountBusiness.getBankAccountById(id);
        if (account == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.ok(account).build();

    }

    @GET
    @Path("/bank-accounts-detail/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @AuthenticationRequired(RoleEnum.CUSTOMER)//vérifier que le client a le droit d'accéder à ce compte (lié à son compte ou admin)
    public Response getBankAccountDetailById(@PathParam("id") String id) {

        BankAccountDetail detail = bankAccountBusiness.getBankAccountDetailById(id);
        if (detail == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.ok(detail).build();

    }

    @PUT
    @Path("/bank-accounts/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @AuthenticationRequired(RoleEnum.ADMIN)
    public Response updateBankAccount(@PathParam("id") String id, BankAccount dto) {
        BankAccount updated = bankAccountBusiness.updateBankAccount(id, dto);
        if (updated == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.ok(updated).build();
    }

    @DELETE
    @Path("/bank-accounts/{id}")
    @AuthenticationRequired(RoleEnum.ADMIN)
    public Response deleteBankAccount(@PathParam("id") String id) {
        bankAccountBusiness.deleteBankAccount(id);
        return Response.noContent().build();
    }

    @PATCH
    @Path("/bank-accounts/{id}/state")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @AuthenticationRequired(RoleEnum.ADMIN)
    public Response updateBankAccountState(@PathParam("id") String id, Map<String, String> body) {
        String state = body.get("state");

        if (state == null || state.isEmpty()) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }

        // Validation de l'état
        if (!state.equals("active") && !state.equals("inactive") &&
                !state.equals("bloqued") && !state.equals("closed")) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }

        try {
            BankAccount updated = bankAccountBusiness.updateState(id, state);
            return Response.ok(updated).build();
        } catch (Exception e) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
    }

    @GET
    @Path("/bank-accounts/{id}/balance")
    @Produces(MediaType.APPLICATION_JSON)
    @AuthenticationRequired(RoleEnum.ADMIN)
    public Response getBalance(@PathParam("id") String id) {
        try {
            Double balance = bankAccountBusiness.getBalance(id);
            Map<String, Double> response = new HashMap<>();
            response.put("sold", balance);
            return Response.ok(response).build();
        } catch (Exception e) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
    }


}