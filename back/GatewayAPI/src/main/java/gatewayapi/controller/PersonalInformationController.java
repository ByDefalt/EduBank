package gatewayapi.controller;

import gatewayapi.client.AccountClient;
import gatewayapi.wrapper.FeignExecutor;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.springframework.stereotype.Controller;

@Controller
@Path("/personalInformation")
public class PersonalInformationController {

    @Inject
    private final AccountClient accountClient;
    private final FeignExecutor feignExecutor;

    @Inject
    public PersonalInformationController(AccountClient accountClient, FeignExecutor feignExecutor) {
        this.accountClient = accountClient;
        this.feignExecutor = feignExecutor;
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllPersonalInformation() {
        return feignExecutor.wrap(() -> accountClient.getAllPersonalInformation());
    }

    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getPersonalInformationById(@PathParam("id") Integer id) {
        return feignExecutor.wrap(() -> accountClient.getPersonalInformationById(id));
    }
}