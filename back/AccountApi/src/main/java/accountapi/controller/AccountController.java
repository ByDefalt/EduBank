package accountapi.controller;

import accountapi.annotation.AuthenticationRequired;
import accountapi.business.AccountBusiness;
import dto.accountapi.*;
import dto.accountapi.Error;
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
        return Response.ok(accounts).build();
    }

    @GET
    @Path("/{idAccount}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAccountById(@PathParam("idAccount") String id) {
        Account account = accountBusiness.getAccountById(id);
        if (account == null) {
            Error error = new Error()
                    .code("404")
                    .message("Compte non trouvé")
                    .details("Aucun compte trouvé avec l'ID : " + id);
            return Response.status(Response.Status.NOT_FOUND).entity(error).build();
        }
        return Response.ok(account).build();
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response createAccount(AccountRegister accountDto) {
        Account createdAccount = accountBusiness.createAccount(accountDto);
        if (createdAccount == null) {
            Error error = new Error()
                    .code("400")
                    .message("Requête invalide")
                    .details("Impossible de créer le compte");
            return Response.status(Response.Status.BAD_REQUEST).entity(error).build();
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
            Error error = new Error()
                    .code("401")
                    .message("Mot de passe incorrect")
                    .details("Les identifiants fournis sont incorrects ou le compte est inactif");
            return Response.status(Response.Status.UNAUTHORIZED).entity(error).build();
        }
        return Response.ok(tokenRequest).build();
    }

    @POST
    @Path("/validate")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response validateToken(TokenRequest tokenRequest) {
        TokenResponse tokenResponse = accountBusiness.validateToken(tokenRequest);
        if (tokenResponse == null) {
            Error error = new Error()
                    .code("401")
                    .message("Token invalide ou expiré")
                    .details("Le token JWT fourni est invalide ou a expiré");
            return Response.status(Response.Status.UNAUTHORIZED).entity(error).build();
        }
        return Response.ok(tokenResponse).build();
    }

    @DELETE
    @AuthenticationRequired(RoleEnum.ADMIN)
    @Path("/{idAccount}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteAccount(@PathParam("idAccount") String id) {
        boolean deleted = accountBusiness.deleteAccount(id);
        if (!deleted) {
            Error error = new Error()
                    .code("404")
                    .message("Compte non trouvé")
                    .details("Aucun compte trouvé avec l'ID : " + id);
            return Response.status(Response.Status.NOT_FOUND).entity(error).build();
        }
        return Response.ok(deleted).build();
    }

    @GET
    @Path("/role/{idAccount}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getRoleByAccountId(@PathParam("idAccount") String id) {
        Role role = accountBusiness.getRoleByAccountId(id);
        if (role == null) {
            Error error = new Error()
                    .code("404")
                    .message("Compte ou Rôle non trouvé")
                    .details("Aucun rôle trouvé pour le compte avec l'ID : " + id);
            return Response.status(Response.Status.NOT_FOUND).entity(error).build();
        }
        return Response.ok(role).build();
    }

    @GET
    @Path("/personalInformation/{idAccount}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getPersonalInformationByAccountId(@PathParam("idAccount") String id) {
        PersonalInformation personalInformation = accountBusiness.getPersonalInformationByAccountId(id);
        if (personalInformation == null) {
            Error error = new Error()
                    .code("404")
                    .message("Compte ou Infos non trouvées")
                    .details("Aucune information personnelle trouvée pour le compte avec l'ID : " + id);
            return Response.status(Response.Status.NOT_FOUND).entity(error).build();
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
            Error error = new Error()
                    .code("404")
                    .message("Compte non trouvé")
                    .details("Aucun compte trouvé avec l'ID : " + id);
            return Response.status(Response.Status.NOT_FOUND).entity(error).build();
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
            Error error = new Error()
                    .code("404")
                    .message("Compte non trouvé")
                    .details("Aucun compte trouvé avec l'ID : " + id);
            return Response.status(Response.Status.NOT_FOUND).entity(error).build();
        }
        return Response.ok(updatedAccount).build();
    }
}