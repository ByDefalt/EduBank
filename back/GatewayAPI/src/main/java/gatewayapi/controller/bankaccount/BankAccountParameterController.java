package gatewayapi.controller.bankaccount;

import dto.accountapi.RoleEnum;
import dto.bankapiswagger.*;
import gatewayapi.annotation.AuthenticationRequired;
import gatewayapi.business.bankaccount.BankAccountParameterBusiness;
import gatewayapi.wrapper.FeignExecutor;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.springframework.stereotype.Controller;

@Controller
@Path("/bank")
public class BankAccountParameterController {

    private final BankAccountParameterBusiness bankAccountParameterBusiness;
    private final FeignExecutor feignExecutor;

    public BankAccountParameterController(BankAccountParameterBusiness bankAccountParameterBusiness, FeignExecutor feignExecutor) {
        this.bankAccountParameterBusiness = bankAccountParameterBusiness;
        this.feignExecutor = feignExecutor;
    }

    // ==================== ADMIN ====================

    @PUT
    @Path("/admin/bank-accounts/{bankAccountId}/parameters")
    @AuthenticationRequired(RoleEnum.ADMIN)
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateParameters(
            @PathParam("bankAccountId") String bankAccountId,
            BankAccountParameter parameters) {
        return feignExecutor.wrap(() -> {
            bankAccountParameterBusiness.updateParameters(bankAccountId, parameters);
            return null;
        });
    }
}