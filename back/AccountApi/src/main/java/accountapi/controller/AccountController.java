package accountapi.controller;

import accountapi.annotation.AuthenticationRequired;
import accountapi.business.AccountBusiness;
import dto.accountapi.*;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.springframework.stereotype.Controller;

@Controller
@Path("/accounts")
public class AccountController {

    private final AccountBusiness accountBusiness;

    public AccountController(AccountBusiness accountBusiness) {
        this.accountBusiness = accountBusiness;
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllAccounts() {
        return Response.ok(accountBusiness.getAllAccounts()).build();
    }

    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAccountById(@PathParam("id") String id) {
        Account account = accountBusiness.getAccountById(id);

        if (account == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.ok(account).build();
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response createAccount(AccountRegister accountDto) {
        return Response.status(Response.Status.CREATED).entity(accountBusiness.createAccount(accountDto)).build();
    }

    @POST
    @Path("/signin")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response signIn(SignInRequest signInRequest) {
        TokenRequest tokenRequest = accountBusiness.signIn(signInRequest);
        if (tokenRequest == null) {
            return Response.status(Response.Status.UNAUTHORIZED).build();
        } else {
            return Response.status(Response.Status.OK).entity(tokenRequest).build();
        }
    }

    @POST
    @Path("/validate")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response validateToken(TokenRequest tokenRequest) {
        TokenResponse tokenResponse = accountBusiness.validateToken(tokenRequest);
        if (tokenResponse == null) {
            return Response.status(Response.Status.UNAUTHORIZED).build();
        } else {
            return Response.ok(tokenResponse).build();
        }
    }

    @DELETE
    @Path("/{id}")
    public Response deleteAccount(@PathParam("id") String id) {
        boolean deleted = accountBusiness.deleteAccount(id);
        if (!deleted) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.ok(deleted).build();
    }

    @GET
    @Path("/role/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getRoleByAccountId(@PathParam("id") String id) {
        Role role = accountBusiness.getRoleByAccountId(id);
        if (role == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.ok(role).build();
    }

    @GET
    @Path("/personalInformation/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getPersonalInformationByAccountId(@PathParam("id") String id) {
        PersonalInformation personalInformation = accountBusiness.getPersonalInformationByAccountId(id);
        if (personalInformation == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.ok(personalInformation).build();
    }

    @PUT
    @AuthenticationRequired(RoleEnum.ADMIN)
    @Path("/deactivate/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response deactivateAccount(@PathParam("id") String id) {
        boolean updatedAccount = accountBusiness.deactivateAccount(id);
        if (!updatedAccount) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.ok(updatedAccount).build();
    }

    @PUT
    @AuthenticationRequired(RoleEnum.ADMIN)
    @Path("/activate/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response activateAccount(@PathParam("id") String id) {
        boolean updatedAccount = accountBusiness.activateAccount(id);
        if (!updatedAccount) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.ok(updatedAccount).build();
    }
}