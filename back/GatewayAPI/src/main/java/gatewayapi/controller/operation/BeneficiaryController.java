package gatewayapi.controller.operation;

import dto.accountapi.RoleEnum;
import dto.operationapi.Beneficiary;
import gatewayapi.annotation.AuthenticationRequired;
import gatewayapi.client.OperationClient;
import gatewayapi.wrapper.FeignExecutor;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.springframework.stereotype.Controller;

@Controller
@Path("/beneficiaries")
public class BeneficiaryController {

    private final OperationClient operationClient;
    private final FeignExecutor feignExecutor;

    public BeneficiaryController(OperationClient operationClient, FeignExecutor feignExecutor) {
        this.operationClient = operationClient;
        this.feignExecutor = feignExecutor;
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @AuthenticationRequired(RoleEnum.ADMIN)
    public Response getAllBeneficiaries() {
        return feignExecutor.wrap(operationClient::getAllBeneficiaries);
    }

    @GET
    @Path("/{accountId}")
    @Produces(MediaType.APPLICATION_JSON)
    @AuthenticationRequired(RoleEnum.CUSTOMER)
    public Response getBeneficiariesByAccountId(@PathParam("accountId") String accountId) {
        return feignExecutor.wrap(() -> operationClient.getBeneficiariesByAccountId(accountId));
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @AuthenticationRequired(RoleEnum.CUSTOMER)
    public Response createBeneficiary(Beneficiary beneficiary) {
        return feignExecutor.wrap(() -> operationClient.createBeneficiary(beneficiary));
    }

    @PUT
    @Path("/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @AuthenticationRequired(RoleEnum.CUSTOMER)
    public Response updateBeneficiary(@PathParam("id") Integer id, Beneficiary beneficiary) {
        return feignExecutor.wrap(() -> operationClient.updateBeneficiary(id, beneficiary));
    }

    @DELETE
    @Path("/{id}")
    @AuthenticationRequired(RoleEnum.CUSTOMER)
    public Response deleteBeneficiary(@PathParam("id") Integer id) {
        return feignExecutor.wrap(() -> operationClient.deleteBeneficiary(id));
    }
}
