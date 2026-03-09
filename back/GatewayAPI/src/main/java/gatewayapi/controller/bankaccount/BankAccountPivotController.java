package gatewayapi.controller.bankaccount;

import dto.accountapi.RoleEnum;
import dto.bankapiswagger.*;
import gatewayapi.annotation.AuthenticationRequired;
import gatewayapi.business.bankaccount.BankAccountPivotBusiness;
import gatewayapi.wrapper.FeignExecutor;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.springframework.stereotype.Controller;

@Controller
@Path("/bank")
public class BankAccountPivotController {

    private final BankAccountPivotBusiness bankAccountPivotBusiness;
    private final FeignExecutor feignExecutor;

    public BankAccountPivotController(BankAccountPivotBusiness bankAccountPivotBusiness, FeignExecutor feignExecutor) {
        this.bankAccountPivotBusiness = bankAccountPivotBusiness;
        this.feignExecutor = feignExecutor;
    }

    // ==================== ADMIN ====================

    @POST
    @Path("/bank-accounts-pivot")
    @AuthenticationRequired(RoleEnum.ADMIN)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response createPivot(BankAccountPivot pivot) {
        return feignExecutor.wrap(() -> {
            bankAccountPivotBusiness.createPivot(pivot);
            return null;
        });
    }

    @DELETE
    @Path("/bank-accounts-pivot")
    @AuthenticationRequired(RoleEnum.ADMIN)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response deletePivot(BankAccountPivot pivot) {
        return feignExecutor.wrap(() -> {
            bankAccountPivotBusiness.deletePivot(pivot);
            return null;
        });
    }

    @GET
    @Path("/bank-accounts-pivot/bank-account/{bankAccountId}")
    @AuthenticationRequired(RoleEnum.ADMIN)
    @Produces(MediaType.APPLICATION_JSON)
    public Response getPivotsByBankAccount(@PathParam("bankAccountId") String bankAccountId) {
        return feignExecutor.wrap(() -> bankAccountPivotBusiness.getPivotsByBankAccount(bankAccountId));
    }

    @GET
    @Path("/bank-accounts-pivot/account/{accountId}")
    @AuthenticationRequired(RoleEnum.ADMIN)
    @Produces(MediaType.APPLICATION_JSON)
    public Response getPivotsByAccount(@PathParam("accountId") String accountId) {
        return feignExecutor.wrap(() -> bankAccountPivotBusiness.getPivotsByAccount(accountId));
    }

    @DELETE
    @Path("/bank-accounts-pivot/bank-account/{bankAccountId}")
    @AuthenticationRequired(RoleEnum.ADMIN)
    public Response deleteAllPivotsByBankAccount(@PathParam("bankAccountId") String bankAccountId) {
        return feignExecutor.wrap(() -> {
            bankAccountPivotBusiness.deleteAllPivotsByBankAccount(bankAccountId);
            return null;
        });
    }

    @DELETE
    @Path("/bank-accounts-pivot/account/{accountId}")
    @AuthenticationRequired(RoleEnum.ADMIN)
    public Response deleteAllPivotsByAccount(@PathParam("accountId") String accountId) {
        return feignExecutor.wrap(() -> {
            bankAccountPivotBusiness.deleteAllPivotsByAccount(accountId);
            return null;
        });
    }
}