package gatewayapi.controller.bankaccount;

import dto.accountapi.RoleEnum;
import dto.bankapiswagger.*;
import gatewayapi.annotation.AuthenticationRequired;
import gatewayapi.business.bankaccount.BankAccountBusiness;
import gatewayapi.wrapper.FeignExecutor;
import jakarta.ws.rs.*;
import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.springframework.stereotype.Controller;

@Controller
@Path("/bank")
public class BankAccountController {

    private final BankAccountBusiness bankAccountBusiness;
    private final FeignExecutor feignExecutor;

    public BankAccountController(BankAccountBusiness bankAccountBusiness, FeignExecutor feignExecutor) {
        this.bankAccountBusiness = bankAccountBusiness;
        this.feignExecutor = feignExecutor;
    }

    // ==================== ADMIN ====================

    @GET
    @Path("/admin/bank-accounts")
    @AuthenticationRequired(RoleEnum.ADMIN)
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllBankAccounts() {
        return feignExecutor.wrap(bankAccountBusiness::getAllBankAccounts);
    }

    @GET
    @Path("/admin/bank-accounts/{id}")
    @AuthenticationRequired(RoleEnum.ADMIN)
    @Produces(MediaType.APPLICATION_JSON)
    public Response getBankAccountById(@PathParam("id") String id) {
        return feignExecutor.wrap(() -> bankAccountBusiness.getBankAccountById(id));
    }

    @DELETE
    @Path("/admin/bank-accounts/{id}")
    @AuthenticationRequired(RoleEnum.ADMIN)
    public Response deleteBankAccount(@PathParam("id") String id) {
        return feignExecutor.wrap(() -> {
            bankAccountBusiness.deleteBankAccount(id);
            return null;
        });
    }

    @GET
    @Path("/admin/accounts/{accountId}/bank-accounts")
    @AuthenticationRequired(RoleEnum.ADMIN)
    @Produces(MediaType.APPLICATION_JSON)
    public Response getBankAccountsByAccountId(@PathParam("accountId") String accountId) {
        return feignExecutor.wrap(() -> bankAccountBusiness.getBankAccountsByAccountId(accountId));
    }

    @POST
    @Path("/admin/accounts/{accountId}/bank-accounts")
    @AuthenticationRequired(RoleEnum.ADMIN)
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response createBankAccount(
            @PathParam("accountId") String accountId,
            BankAccountCreateRequest request) {
        return feignExecutor.wrap(() -> bankAccountBusiness.createBankAccount(accountId, request));
    }

    // ==================== CLIENT ====================

    @GET
    @Path("/my-bank-accounts")
    @AuthenticationRequired(RoleEnum.CUSTOMER)
    @Produces(MediaType.APPLICATION_JSON)
    public Response getMyBankAccounts(
            @QueryParam("type_id") Integer typeId,
            @Context ContainerRequestContext requestContext) {
        String userId = (String) requestContext.getProperty("userId");
        return feignExecutor.wrap(() -> bankAccountBusiness.getMyBankAccounts(userId, typeId));
    }

    @GET
    @Path("/my-bank-accounts/{id}")
    @AuthenticationRequired(RoleEnum.CUSTOMER)
    @Produces(MediaType.APPLICATION_JSON)
    public Response getMyBankAccountById(
            @PathParam("id") String id,
            @Context ContainerRequestContext requestContext) {
        String userId = (String) requestContext.getProperty("userId");
        return feignExecutor.wrap(() -> bankAccountBusiness.getMyBankAccountById(userId, id));
    }

    @GET
    @Path("/my-bank-accounts/{id}/co-holders")
    @AuthenticationRequired(RoleEnum.CUSTOMER)
    @Produces(MediaType.APPLICATION_JSON)
    public Response getMyCoHolders(
            @PathParam("id") String id,
            @Context ContainerRequestContext requestContext) {
        String userId = (String) requestContext.getProperty("userId");
        return feignExecutor.wrap(() -> bankAccountBusiness.getMyCoHolders(userId, id));
    }
}