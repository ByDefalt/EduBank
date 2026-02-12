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
@Path("/beneficiaries")
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
    public Response getBeneficiaries() {
        List<Beneficiary> beneficiaries = this.beneficiaryBusiness.getBeneficiaries();
        return Response.ok(beneficiaries).build();
    }

    @GET
    @Path("/{accountId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getBeneficiariesByAccountId(@PathParam("accountId") String accountId) {
        List<Beneficiary> beneficiaries = this.beneficiaryBusiness.getBeneficiariesByAccountId(accountId);
        return Response.ok(beneficiaries).build();
    }

    @PUT
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateBeneficiary(@PathParam("id") Integer id, Beneficiary beneficiary) {
        Beneficiary updatedBeneficiary = this.beneficiaryBusiness.updateBeneficiary(id, beneficiary);
        return Response.ok(updatedBeneficiary).build();
    }

    @DELETE
    @Path("/{id}")
    public Response deleteBeneficiaryById(@PathParam("id") Integer id) {
        this.beneficiaryBusiness.deleteBeneficiaryById(id);
        return Response.status(Response.Status.NO_CONTENT).build();
    }
}
