package gatewayapi.controller;

import dto.accountapi.*;
import gatewayapi.client.AccountClient;
import gatewayapi.wrapper.FeignExecutor;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.springframework.stereotype.Controller;

@Controller
@Path("/accounts")
public class AccountController {

    @Inject
    private final AccountClient accountClient;
    private final FeignExecutor feignExecutor;

    public AccountController(AccountClient accountClient, FeignExecutor feignExecutor) {
        this.accountClient = accountClient;
        this.feignExecutor = feignExecutor;
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllAccounts() {
        return feignExecutor.wrap(() -> accountClient.getAllAccounts());
    }

    @GET
    @Path("/{idAccount}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAccountById(@PathParam("idAccount") String id) {
        return feignExecutor.wrap(() -> accountClient.getAccountById(id));
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response createAccount(AccountRegister accountDto) {
        return feignExecutor.wrap(() -> accountClient.createAccount(accountDto));
    }

    @POST
    @Path("/signin")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response signIn(SignInRequest signInRequest) {
        return feignExecutor.wrap(() -> accountClient.signIn(signInRequest));
    }

    @POST
    @Path("/validate")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response validateToken(TokenRequest tokenRequest) {
        return feignExecutor.wrap(() -> accountClient.validateToken(tokenRequest));
    }

    @DELETE
    @Path("/{idAccount}")
    public Response deleteAccount(@PathParam("idAccount") String id) {
        return feignExecutor.wrap(() -> accountClient.deleteAccount(id));
    }

    @GET
    @Path("/role/{idAccount}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getRoleByAccountId(@PathParam("idAccount") String id) {
        return feignExecutor.wrap(() -> accountClient.getRoleByAccountId(id));
    }

    @GET
    @Path("/personalInformation/{idAccount}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getPersonalInformationByAccountId(@PathParam("idAccount") String id) {
        return feignExecutor.wrap(() -> accountClient.getPersonalInformationByAccountId(id));
    }

    @PUT
    @Path("/deactivate/{idAccount}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response deactivateAccount(@PathParam("idAccount") String id) {
        return feignExecutor.wrap(() -> accountClient.deactivateAccount(id));
    }

    @PUT
    @Path("/activate/{idAccount}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response activateAccount(@PathParam("idAccount") String id) {
        return feignExecutor.wrap(() -> accountClient.activateAccount(id));
    }
}