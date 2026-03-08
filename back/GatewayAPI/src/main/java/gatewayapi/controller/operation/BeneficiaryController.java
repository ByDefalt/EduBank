package gatewayapi.controller.operation;

import dto.accountapi.RoleEnum;
import dto.operationapi.Beneficiary;
import gatewayapi.annotation.AuthenticationRequired;
import gatewayapi.business.operation.BeneficiaryBusiness;
import gatewayapi.wrapper.FeignExecutor;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.springframework.stereotype.Controller;

@Controller
@Path("/beneficiaries")
public class BeneficiaryController {

    private final BeneficiaryBusiness beneficiaryBusiness;
    private final FeignExecutor feignExecutor;

    public BeneficiaryController(BeneficiaryBusiness beneficiaryBusiness, FeignExecutor feignExecutor) {
        this.beneficiaryBusiness = beneficiaryBusiness;
        this.feignExecutor = feignExecutor;
    }

    @GET
    @AuthenticationRequired(RoleEnum.ADMIN)
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllBeneficiaries() {
        return feignExecutor.wrap(beneficiaryBusiness::getAllBeneficiaries);
    }

    @GET
    @Path("/{accountId}")
    @AuthenticationRequired(RoleEnum.CUSTOMER)
    @Produces(MediaType.APPLICATION_JSON)
    public Response getBeneficiariesByAccountId(@PathParam("accountId") String accountId) {
        return feignExecutor.wrap(() -> beneficiaryBusiness.getBeneficiariesByAccountId(accountId));
    }

    @POST
    @AuthenticationRequired(RoleEnum.CUSTOMER)
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response createBeneficiary(Beneficiary beneficiary) {
        return feignExecutor.wrap(() -> beneficiaryBusiness.createBeneficiary(beneficiary));
    }

    @PUT
    @Path("/{id}")
    @AuthenticationRequired(RoleEnum.CUSTOMER)
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateBeneficiary(@PathParam("id") Integer id, Beneficiary beneficiary) {
        return feignExecutor.wrap(() -> beneficiaryBusiness.updateBeneficiary(id, beneficiary));
    }

    @DELETE
    @AuthenticationRequired(RoleEnum.CUSTOMER)
    @Path("/{id}")
    public Response deleteBeneficiary(@PathParam("id") Integer id) {
        return feignExecutor.wrap(() -> {
            beneficiaryBusiness.deleteBeneficiary(id);
            return null;
        });
    }
}
