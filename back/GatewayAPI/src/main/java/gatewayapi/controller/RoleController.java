package gatewayapi.controller;

import gatewayapi.client.AccountClient;
import gatewayapi.wrapper.FeignExecutor;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.springframework.stereotype.Controller;

@Controller
@Path("/roles")
public class RoleController {

    private final AccountClient accountClient;
    private final FeignExecutor feignExecutor;

    @Inject
    public RoleController(AccountClient accountClient, FeignExecutor feignExecutor) {
        this.accountClient = accountClient;
        this.feignExecutor = feignExecutor;
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllRoles() {
        return feignExecutor.wrap(() -> accountClient.getAllRoles());
    }

    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getRoleById(@PathParam("id") Integer id) {
        return feignExecutor.wrap(() -> accountClient.getRoleById(id));
    }
}