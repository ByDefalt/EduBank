package com.operationapi.controller;


import com.operationapi.business.BeneficiaryBusiness;
import com.operationapi.mapper.BeneficiaryMapper;
import dto.operationapi.Beneficiary;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.springframework.stereotype.Controller;

import java.util.List;

@Controller
@Path("/account")
public class BeneficiaryController {

    private final BeneficiaryBusiness beneficiaryBusiness;

    public BeneficiaryController(BeneficiaryBusiness beneficiaryBusiness) {
        this.beneficiaryBusiness = beneficiaryBusiness;
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response createBeneficiary(Beneficiary beneficiary) {
        Beneficiary createdBeneficiary = beneficiaryBusiness.createBeneficiary(BeneficiaryMapper.toEntity(beneficiary));
        return Response.status(Response.Status.CREATED).entity(createdBeneficiary).build();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getBeneficiariesByAccountId() {
        List<Beneficiary> beneficiaries = this.beneficiaryBusiness.getBeneficiaries();
        return Response.ok(beneficiaries).build();
    }

    @GET
    @Path("/{accountId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getBeneficiariesByAccountId(@PathParam("accountId") String accountId) {
        Beneficiary beneficiaries = this.beneficiaryBusiness.getBeneficiaryById(accountId);
        return Response.ok(beneficiaries).build();
    }

    @DELETE
    @Path("/{accountId}")
    public Response deleteBeneficiaryById(@PathParam("accountId") String accountId) {
        Beneficiary beneficiary = this.beneficiaryBusiness.deleteBeneficiaryById(accountId);
        if(beneficiary == null) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }
        return Response.status(Response.Status.NO_CONTENT).build();
    }
}
