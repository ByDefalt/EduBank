package com.example.clientAPI.controller;

import com.example.clientAPI.annotation.AuthenticationRequired;
import com.example.clientAPI.business.BankAccountBusiness;
import dto.accountapi.RoleEnum;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.springframework.stereotype.Controller;

import java.util.List;

@Controller
@Path("/")
public class BankAccountPivotController {
    private final BankAccountBusiness bankAccountBusiness;

    public BankAccountPivotController(BankAccountBusiness bankAccountBusiness) {
        this.bankAccountBusiness = bankAccountBusiness;
    }

    @POST
    @Path("/bank-accounts/{bankAccountId}/link/{accountId}")
    @AuthenticationRequired(RoleEnum.ADMIN)
    public Response linkAccountToBankAccount(
            @PathParam("bankAccountId") String bankAccountId,
            @PathParam("accountId") Integer accountId) {
        bankAccountBusiness.linkAccountToBankAccount(bankAccountId, accountId);
        return Response.status(Response.Status.CREATED).build();
    }

    @DELETE
    @Path("/bank-accounts/{bankAccountId}/unlink/{accountId}")
    @AuthenticationRequired(RoleEnum.ADMIN)
    public Response unlinkAccountFromBankAccount(
            @PathParam("bankAccountId") String bankAccountId,
            @PathParam("accountId") Integer accountId) {
        bankAccountBusiness.unlinkAccountFromBankAccount(bankAccountId, accountId);
        return Response.noContent().build();
    }

    @DELETE
    @Path("/bank-accounts/{bankAccountId}/unlink-all")
    @AuthenticationRequired(RoleEnum.ADMIN)
    public Response unlinkAllAccountsFromBankAccount(@PathParam("bankAccountId") String bankAccountId) {
        bankAccountBusiness.unlinkAllAccountsFromBankAccount(bankAccountId);
        return Response.noContent().build();
    }

    @DELETE
    @Path("/accounts/{accountId}/unlink-all")
    @AuthenticationRequired(RoleEnum.ADMIN)
    public Response unlinkAllBankAccountsFromAccount(@PathParam("accountId") Integer accountId) {
        bankAccountBusiness.unlinkAllBankAccountsFromAccount(accountId);
        return Response.noContent().build();
    }

    @GET
    @Path("/bank-accounts/{bankAccountId}/accounts")
    @Produces(MediaType.APPLICATION_JSON)
    @AuthenticationRequired(RoleEnum.ADMIN)
    public Response getAccountsLinkedToBankAccount(@PathParam("bankAccountId") String bankAccountId) {
        List<Integer> accountIds = bankAccountBusiness.getAccountsLinkedToBankAccount(bankAccountId);
        return Response.ok(accountIds).build();
    }

    @GET
    @Path("/accounts/{accountId}/bank-accounts")
    @Produces(MediaType.APPLICATION_JSON)
    @AuthenticationRequired(RoleEnum.CUSTOMER)
    public Response getBankAccountsLinkedToAccount(@PathParam("accountId") Integer accountId) {
        List<String> bankAccountIds = bankAccountBusiness.getBankAccountsLinkedToAccount(accountId);
        return Response.ok(bankAccountIds).build();
    }
}
