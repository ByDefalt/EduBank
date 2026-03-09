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

    // ==================== ADMIN - BankAccounts ====================

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

    // ==================== ADMIN - Parameters ====================

    @PATCH
    @Path("/admin/bank-accounts/{bankAccountId}/parameters")
    @AuthenticationRequired(RoleEnum.ADMIN)
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateParameters(
            @PathParam("bankAccountId") String bankAccountId,
            BankAccountParameter parameters) {
        return feignExecutor.wrap(() -> {
            bankAccountBusiness.updateParameters(bankAccountId, parameters);
            return null;
        });
    }

    // ==================== ADMIN - Types ====================

    @GET
    @Path("/admin/types")
    @AuthenticationRequired(RoleEnum.ADMIN)
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllTypes() {
        return feignExecutor.wrap(bankAccountBusiness::getAllTypes);
    }

    @GET
    @Path("/admin/types/{id}")
    @AuthenticationRequired(RoleEnum.ADMIN)
    @Produces(MediaType.APPLICATION_JSON)
    public Response getTypeById(@PathParam("id") Integer id) {
        return feignExecutor.wrap(() -> bankAccountBusiness.getTypeById(id));
    }

    @POST
    @Path("/admin/types")
    @AuthenticationRequired(RoleEnum.ADMIN)
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response createType(Type type) {
        return feignExecutor.wrap(() -> bankAccountBusiness.createType(type));
    }

    // ==================== ADMIN - Pivot ====================

    @POST
    @Path("/bank-accounts-pivot")
    @AuthenticationRequired(RoleEnum.ADMIN)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response createPivot(BankAccountPivot pivot) {
        return feignExecutor.wrap(() -> {
            bankAccountBusiness.createPivot(pivot);
            return null;
        });
    }

    @DELETE
    @Path("/bank-accounts-pivot")
    @AuthenticationRequired(RoleEnum.ADMIN)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response deletePivot(BankAccountPivot pivot) {
        return feignExecutor.wrap(() -> {
            bankAccountBusiness.deletePivot(pivot);
            return null;
        });
    }

    @GET
    @Path("/bank-accounts-pivot/bank-account/{bankAccountId}")
    @AuthenticationRequired(RoleEnum.ADMIN)
    @Produces(MediaType.APPLICATION_JSON)
    public Response getPivotsByBankAccount(@PathParam("bankAccountId") String bankAccountId) {
        return feignExecutor.wrap(() -> bankAccountBusiness.getPivotsByBankAccount(bankAccountId));
    }

    @GET
    @Path("/bank-accounts-pivot/account/{accountId}")
    @AuthenticationRequired(RoleEnum.ADMIN)
    @Produces(MediaType.APPLICATION_JSON)
    public Response getPivotsByAccount(@PathParam("accountId") String accountId) {
        return feignExecutor.wrap(() -> bankAccountBusiness.getPivotsByAccount(accountId));
    }

    @DELETE
    @Path("/bank-accounts-pivot/bank-account/{bankAccountId}")
    @AuthenticationRequired(RoleEnum.ADMIN)
    public Response deleteAllPivotsByBankAccount(@PathParam("bankAccountId") String bankAccountId) {
        return feignExecutor.wrap(() -> {
            bankAccountBusiness.deleteAllPivotsByBankAccount(bankAccountId);
            return null;
        });
    }

    @DELETE
    @Path("/bank-accounts-pivot/account/{accountId}")
    @AuthenticationRequired(RoleEnum.ADMIN)
    public Response deleteAllPivotsByAccount(@PathParam("accountId") String accountId) {
        return feignExecutor.wrap(() -> {
            bankAccountBusiness.deleteAllPivotsByAccount(accountId);
            return null;
        });
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