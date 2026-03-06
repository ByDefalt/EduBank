package gatewayapi.controller.account;

import dto.accountapi.*;
import gatewayapi.annotation.AuthenticationRequired;
import gatewayapi.business.account.AccountBusiness;
import gatewayapi.wrapper.FeignExecutor;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.springframework.stereotype.Controller;

@Controller
@Path("/accounts")
public class AccountController {

    private final AccountBusiness accountBusiness;
    private final FeignExecutor feignExecutor;

    public AccountController(AccountBusiness accountBusiness, FeignExecutor feignExecutor) {
        this.accountBusiness = accountBusiness;
        this.feignExecutor = feignExecutor;
    }

    @GET
    @AuthenticationRequired(RoleEnum.ADMIN)
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllAccounts() {
        return feignExecutor.wrap(accountBusiness::getAllAccounts);
    }

    @GET
    @Path("/{idAccount}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAccountById(@PathParam("idAccount") String id) {
        return feignExecutor.wrap(() -> accountBusiness.getAccountById(id));
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response createAccount(AccountRegister accountDto) {
        return feignExecutor.wrap(() -> accountBusiness.createAccount(accountDto));
    }

    @POST
    @Path("/signin")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response signIn(SignInRequest signInRequest) {
        return feignExecutor.wrap(() -> accountBusiness.signIn(signInRequest));
    }

    @POST
    @Path("/validate")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response validateToken(TokenRequest tokenRequest) {
        return feignExecutor.wrap(() -> accountBusiness.validateToken(tokenRequest));
    }

    @DELETE
    @AuthenticationRequired(RoleEnum.ADMIN)
    @Path("/{idAccount}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteAccount(@PathParam("idAccount") String id) {
        return feignExecutor.wrap(() -> accountBusiness.deleteAccount(id));
    }

    @GET
    @Path("/role/{idAccount}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getRoleByAccountId(@PathParam("idAccount") String id) {
        return feignExecutor.wrap(() -> accountBusiness.getRoleByAccountId(id));
    }

    @GET
    @Path("/personalInformation/{idAccount}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getPersonalInformationByAccountId(@PathParam("idAccount") String id) {
        return feignExecutor.wrap(() -> accountBusiness.getPersonalInformationByAccountId(id));
    }

    @PUT
    @AuthenticationRequired(RoleEnum.ADMIN)
    @Path("/deactivate/{idAccount}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response deactivateAccount(@PathParam("idAccount") String id) {
        return feignExecutor.wrap(() -> accountBusiness.deactivateAccount(id));
    }

    @PUT
    @AuthenticationRequired(RoleEnum.ADMIN)
    @Path("/activate/{idAccount}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response activateAccount(@PathParam("idAccount") String id) {
        return feignExecutor.wrap(() -> accountBusiness.activateAccount(id));
    }
}