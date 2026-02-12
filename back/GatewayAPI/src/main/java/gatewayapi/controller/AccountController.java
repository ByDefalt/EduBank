package gatewayapi.controller;

import gatewayapi.annotation.AuthenticationRequired;
import gatewayapi.business.AccountBusiness;
import dto.accountapi.*;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.springframework.stereotype.Controller;

import java.util.List;

@Controller
@Path("/accounts")
public class AccountController {

    private final AccountBusiness accountBusiness;

    public AccountController(AccountBusiness accountBusiness) {
        this.accountBusiness = accountBusiness;
    }

    @GET
    @AuthenticationRequired(RoleEnum.ADMIN)
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllAccounts() {
        List<Account> accounts = accountBusiness.getAllAccounts();
        if (accounts.isEmpty()) {
            return Response.status(Response.Status.NO_CONTENT).build();
        }
        return Response.ok(accounts).build();
    }

    @GET
    @Path("/{idAccount}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAccountById(@PathParam("idAccount") String id) {
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
        Account createdAccount = accountBusiness.createAccount(accountDto);
        if (createdAccount == null) {
            return Response.status(Response.Status.CONFLICT).build();
        }
        return Response.status(Response.Status.CREATED).entity(createdAccount).build();
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
    @AuthenticationRequired(RoleEnum.ADMIN)
    @Path("/{idAccount}")
    public Response deleteAccount(@PathParam("idAccount") String id) {
        boolean deleted = accountBusiness.deleteAccount(id);
        if (!deleted) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.ok(deleted).build();
    }

    @GET
    @AuthenticationRequired(RoleEnum.CUSTOMER)
    @Path("/role/{idAccount}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getRoleByAccountId(@PathParam("idAccount") String id) {
        Role role = accountBusiness.getRoleByAccountId(id);
        if (role == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.ok(role).build();
    }

    @GET
    @AuthenticationRequired(RoleEnum.CUSTOMER)
    @Path("/personalInformation/{idAccount}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getPersonalInformationByAccountId(@PathParam("idAccount") String id) {
        PersonalInformation personalInformation = accountBusiness.getPersonalInformationByAccountId(id);
        if (personalInformation == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.ok(personalInformation).build();
    }

    @PUT
    @AuthenticationRequired(RoleEnum.ADMIN)
    @Path("/deactivate/{idAccount}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response deactivateAccount(@PathParam("idAccount") String id) {
        boolean updatedAccount = accountBusiness.deactivateAccount(id);
        if (!updatedAccount) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.ok(updatedAccount).build();
    }

    @PUT
    @AuthenticationRequired(RoleEnum.ADMIN)
    @Path("/activate/{idAccount}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response activateAccount(@PathParam("idAccount") String id) {
        boolean updatedAccount = accountBusiness.activateAccount(id);
        if (!updatedAccount) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.ok(updatedAccount).build();
    }
}