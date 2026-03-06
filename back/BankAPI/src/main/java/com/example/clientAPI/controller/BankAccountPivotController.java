package com.example.clientAPI.controller;

import com.example.clientAPI.annotation.AuthenticationRequired;
import com.example.clientAPI.business.BankAccountPivotBusiness;
import com.example.clientAPI.entity.BankAccountPivotEntity;
import com.example.clientAPI.mapper.BankAccountPivotMapper;
import dto.accountapi.RoleEnum;
import dto.bankapi.BankAccountPivot;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.springframework.stereotype.Controller;

import java.util.List;
import java.util.stream.Collectors;


@Controller
@Path("/bank-accounts-pivot")
public class BankAccountPivotController {

    private final BankAccountPivotBusiness bankAccountPivotBusiness;

    public BankAccountPivotController(BankAccountPivotBusiness bankAccountPivotBusiness) {
        this.bankAccountPivotBusiness = bankAccountPivotBusiness;
    }

    /**
     * Créer un lien (ajouter un co-titulaire)
     */
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @AuthenticationRequired(RoleEnum.ADMIN)
    public Response createLink(BankAccountPivot requestDto) {
        BankAccountPivotEntity entity = BankAccountPivotMapper.toEntity(requestDto);
        bankAccountPivotBusiness.createLink(entity);
        return Response.status(Response.Status.CREATED).build();
    }

    /**
     * Supprimer un lien (retirer un co-titulaire)
     */
    @DELETE
    @Consumes(MediaType.APPLICATION_JSON)
    @AuthenticationRequired(RoleEnum.ADMIN)
    public Response deleteLink(BankAccountPivot requestDto) {
        BankAccountPivotEntity entity = BankAccountPivotMapper.toEntity(requestDto);
        bankAccountPivotBusiness.deleteLink(entity);
        return Response.noContent().build();
    }

    /**
     * Supprimer tous les co-titulaires d'un compte
     */
    @DELETE
    @Path("/bank-account/{bankAccountId}")
    @AuthenticationRequired(RoleEnum.ADMIN)
    public Response deleteAllByBankAccount(@PathParam("bankAccountId") String bankAccountId) {
        bankAccountPivotBusiness.deleteAllByBankAccount(bankAccountId);
        return Response.noContent().build();
    }

    /**
     * Supprimer tous les comptes d'un utilisateur
     */
    @DELETE
    @Path("/account/{accountId}")
    @AuthenticationRequired(RoleEnum.ADMIN)
    public Response deleteAllByAccount(@PathParam("accountId") String accountId) {
        bankAccountPivotBusiness.deleteAllByAccount(accountId);
        return Response.noContent().build();
    }

    /**
     * Récupérer tous les co-titulaires d'un compte
     */
    @GET
    @Path("/bank-account/{bankAccountId}")
    @Produces(MediaType.APPLICATION_JSON)
    @AuthenticationRequired(RoleEnum.ADMIN)
    public Response getLinksByBankAccount(@PathParam("bankAccountId") String bankAccountId) {
        List<BankAccountPivotEntity> entities = bankAccountPivotBusiness.getLinksByBankAccount(bankAccountId);
        List<BankAccountPivot> dtos = entities.stream()
                .map(BankAccountPivotMapper::toDto)
                .collect(Collectors.toList());
        return Response.ok(dtos).build();
    }

    /**
     * Récupérer tous les comptes d'un utilisateur
     */
    @GET
    @Path("/account/{accountId}")
    @Produces(MediaType.APPLICATION_JSON)
    @AuthenticationRequired(RoleEnum.CUSTOMER)
    public Response getLinksByAccount(@PathParam("accountId") String accountId) {
        List<BankAccountPivotEntity> entities = bankAccountPivotBusiness.getLinksByAccount(accountId);
        List<BankAccountPivot> dtos = entities.stream()
                .map(BankAccountPivotMapper::toDto)
                .collect(Collectors.toList());
        return Response.ok(dtos).build();
    }
}