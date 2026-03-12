package com.example.clientAPI.controller;

import com.example.clientAPI.business.BankAccountBusiness;
import com.example.clientAPI.mapper.BankAccountCreateRequestMapper;
import com.example.clientAPI.mapper.BankAccountMapper;
import dto.bankapi.BankAccount;
import dto.bankapi.BankAccountCreateRequest;
import dto.bankapi.BankAccountDetail;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.springframework.stereotype.Controller;

import java.util.List;

@Controller
@Path("/bank")
public class BankAccountController {

    private final BankAccountBusiness bankAccountBusiness;

    public BankAccountController(BankAccountBusiness bankAccountBusiness) {
        this.bankAccountBusiness = bankAccountBusiness;
    }

    // ==================== ROUTES ADMIN ====================

    @GET
    @Path("/admin/bank-accounts")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllBankAccounts() {
        List<BankAccount> dtos = bankAccountBusiness.getAllBankAccounts();
        return Response.ok(dtos).build();
    }

    @GET
    @Path("/admin/bank-accounts/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAdminBankAccountById(@PathParam("id") String id) {
        BankAccountDetail dto = bankAccountBusiness.getBankAccountDetailById(id);
        return Response.ok(dto).build();
    }

    @GET
    @Path("/admin/bank-accounts/iban/{iban}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAdminBankAccountByIban(@PathParam("iban") String iban) {
        BankAccount dto = bankAccountBusiness.getBankAccountByIban(iban);
        return Response.ok(dto).build();
    }

    @PUT
    @Path("/admin/bank-accounts/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateBankAccount(@PathParam("id") String id, BankAccount dto) {
        BankAccount updated = bankAccountBusiness.updateBankAccount(id, BankAccountMapper.toEntity(dto));
        return Response.ok(updated).build();
    }

    @DELETE
    @Path("/admin/bank-accounts/{id}")
    public Response deleteBankAccount(@PathParam("id") String id) {
        bankAccountBusiness.deleteBankAccount(id);
        return Response.noContent().build();
    }

    @GET
    @Path("/admin/accounts/{account_id}/bank-accounts")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAdminBankAccountsByAccountId(@PathParam("account_id") String accountId) {
        List<BankAccount> dtos = bankAccountBusiness.getBankAccountsByAccountId(accountId);
        return Response.ok(dtos).build();
    }

    @POST
    @Path("/admin/accounts/{account_id}/bank-accounts")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response createBankAccount(
            @PathParam("account_id") String accountId,
            BankAccountCreateRequest requestDto) {

        BankAccountDetail createdDto = bankAccountBusiness.createBankAccountForUser(
                accountId,
                BankAccountCreateRequestMapper.toBankAccountEntity(requestDto),
                BankAccountCreateRequestMapper.toParameterEntity(requestDto)
        );
        return Response.status(Response.Status.CREATED).entity(createdDto).build();
    }

    // ==================== ROUTES CLIENT ====================

    @GET
    @Path("/my-bank-accounts")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getMyBankAccounts(
            @QueryParam("type_id") Integer typeId,
            @HeaderParam("X-User-Id") String userId) {

        List<BankAccount> dtos = bankAccountBusiness.getMyBankAccounts(userId, typeId);
        return Response.ok(dtos).build();
    }

    @GET
    @Path("/my-bank-accounts/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getMyBankAccountById(
            @PathParam("id") String id,
            @HeaderParam("X-User-Id") String userId) {

        BankAccountDetail dto = bankAccountBusiness.getMyBankAccountById(userId, id);
        return Response.ok(dto).build();
    }

    @GET
    @Path("/my-bank-accounts/{id}/co-holders")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getMyCoHolders(
            @PathParam("id") String id,
            @HeaderParam("X-User-Id") String userId) {

        List<String> coHolderIds = bankAccountBusiness.getCoHolderIds(userId, id);
        return Response.ok(coHolderIds).build();
    }
}