package accountapi.controller;

import accountapi.business.AccountBusiness;
import accountapi.entity.AccountEntity;
import accountapi.entity.PersonalInformationEntity;
import accountapi.entity.RoleEntity;
import accountapi.mapper.AccountMapper;
import accountapi.mapper.PersonalInformationMapper;
import accountapi.mapper.RoleMapper;
import dto.accountapi.Account;
import dto.accountapi.AccountRegister;
import dto.accountapi.SignInRequest;
import dto.accountapi.TokenRequest;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.springframework.stereotype.Controller;
import java.util.ArrayList;
import java.util.List;

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
        List<AccountEntity> accounts = accountBusiness.getAllAccounts();

        List<Account> dtos = new ArrayList<>();
        for (AccountEntity account : accounts) {
            dtos.add(AccountMapper.toDto(account));
        }
        return Response.ok(dtos).build();
    }

    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAccountById(@PathParam("id") String id) {
        AccountEntity account = accountBusiness.getAccountById(id);

        if (account == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.ok(AccountMapper.toDto(account)).build();
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response createAccount(AccountRegister accountDto) {
        AccountEntity createdEntity = accountBusiness.createAccount(accountDto);
        return Response.status(Response.Status.CREATED)
                .entity(AccountMapper.toDto(createdEntity))
                .build();
    }

    @POST
    @Path("/signin")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response signIn(SignInRequest signInRequest) {
        return accountBusiness.signIn(signInRequest);
    }

    @POST
    @Path("/validate")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response validateToken(TokenRequest tokenRequest) {
        return accountBusiness.validateToken(tokenRequest);
    }

    @DELETE
    @Path("/{id}")
    public Response deleteAccount(@PathParam("id") String id) {
        boolean deleted = accountBusiness.deleteAccount(id);
        if (!deleted) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.noContent().build();
    }

    @GET
    @Path("/role/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getRoleByAccountId(@PathParam("id") String id) {
        RoleEntity roleEntity = accountBusiness.getRoleByAccountId(id);
        if (roleEntity == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.ok(RoleMapper.toDto(roleEntity)).build();
    }

    @GET
    @Path("/personalInformation/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getPersonalInformationByAccountId(@PathParam("id") String id) {
        PersonalInformationEntity personalInformationEntity = accountBusiness.getPersonalInformationByAccountId(id);
        if (personalInformationEntity == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.ok(PersonalInformationMapper.toDto(personalInformationEntity)).build();
    }
}