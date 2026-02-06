package com.example.clientAPI.controller;

import com.example.clientAPI.business.BankAccountBusiness;
import dto.bankapi.BankAccount;
import dto.bankapi.BankAccountParameter;
import dto.bankapi.Type;
import jakarta.ws.rs.*;
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
    @Path("/bank-accounts")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getBankAccounts(
            @QueryParam("account_id") Integer accountId,
            @QueryParam("type_id") Integer typeId) {

        List<BankAccount> accounts;

        if (accountId != null) {
            accounts = bankAccountBusiness.getBankAccountsByAccountId(accountId);
        } else if (typeId != null) {
            accounts = bankAccountBusiness.getBankAccountsByTypeId(typeId);
        } else {
            accounts = bankAccountBusiness.getAllBankAccounts();
        }

        return Response.ok(accounts).build();
    }

    @POST
    @Path("/bank-accounts")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response createBankAccount(BankAccount dto) {
        BankAccount created = bankAccountBusiness.createBankAccount(dto);
        return Response.status(Response.Status.CREATED).entity(created).build();
    }

    @GET
    @Path("/bank-accounts/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getBankAccountById(@PathParam("id") String id) {
        try {
            BankAccount account = bankAccountBusiness.getBankAccountById(id);
            if (account == null) {
                return Response.status(Response.Status.NOT_FOUND).build();
            }
            return Response.ok(account).build();
        } catch (Exception e) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
    }

    @PUT
    @Path("/bank-accounts/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateBankAccount(@PathParam("id") String id, BankAccount dto) {
        BankAccount updated = bankAccountBusiness.updateBankAccount(id, dto);
        return Response.ok(updated).build();
    }

    @DELETE
    @Path("/bank-accounts/{id}")
    public Response deleteBankAccount(@PathParam("id") String id) {
        bankAccountBusiness.deleteBankAccount(id);
        return Response.noContent().build();
    }

    @PATCH
    @Path("/bank-accounts/{id}/state")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
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

    // ============== Type Endpoints ==============

    @GET
    @Path("/types")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getTypes() {
        List<Type> types = bankAccountBusiness.getAllTypes();
        return Response.ok(types).build();
    }

    @POST
    @Path("/types")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response createType(Type dto) {
        Type created = bankAccountBusiness.createType(dto);
        return Response.status(Response.Status.CREATED).entity(created).build();
    }

    // ============== BankAccountParameter Endpoints ==============

    @GET
    @Path("/parameters")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getParameters() {
        List<BankAccountParameter> parameters = bankAccountBusiness.getAllParameters();
        return Response.ok(parameters).build();
    }

    @POST
    @Path("/parameters")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response createParameter(BankAccountParameter dto) {
        BankAccountParameter created = bankAccountBusiness.createParameter(dto);
        return Response.status(Response.Status.CREATED).entity(created).build();
    }

    // ============== BankAccountPivot Endpoints ==============

    @POST
    @Path("/bank-accounts/{bankAccountId}/link/{accountId}")
    public Response linkAccountToBankAccount(
            @PathParam("bankAccountId") String bankAccountId,
            @PathParam("accountId") Integer accountId) {
        bankAccountBusiness.linkAccountToBankAccount(bankAccountId, accountId);
        return Response.status(Response.Status.CREATED).build();
    }

    @DELETE
    @Path("/bank-accounts/{bankAccountId}/unlink/{accountId}")
    public Response unlinkAccountFromBankAccount(
            @PathParam("bankAccountId") String bankAccountId,
            @PathParam("accountId") Integer accountId) {
        bankAccountBusiness.unlinkAccountFromBankAccount(bankAccountId, accountId);
        return Response.noContent().build();
    }

    @DELETE
    @Path("/bank-accounts/{bankAccountId}/unlink-all")
    public Response unlinkAllAccountsFromBankAccount(@PathParam("bankAccountId") String bankAccountId) {
        bankAccountBusiness.unlinkAllAccountsFromBankAccount(bankAccountId);
        return Response.noContent().build();
    }

    @DELETE
    @Path("/accounts/{accountId}/unlink-all")
    public Response unlinkAllBankAccountsFromAccount(@PathParam("accountId") Integer accountId) {
        bankAccountBusiness.unlinkAllBankAccountsFromAccount(accountId);
        return Response.noContent().build();
    }

    @GET
    @Path("/bank-accounts/{bankAccountId}/accounts")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAccountsLinkedToBankAccount(@PathParam("bankAccountId") String bankAccountId) {
        List<Integer> accountIds = bankAccountBusiness.getAccountsLinkedToBankAccount(bankAccountId);
        return Response.ok(accountIds).build();
    }

    @GET
    @Path("/accounts/{accountId}/bank-accounts")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getBankAccountsLinkedToAccount(@PathParam("accountId") Integer accountId) {
        List<String> bankAccountIds = bankAccountBusiness.getBankAccountsLinkedToAccount(accountId);
        return Response.ok(bankAccountIds).build();
    }
}