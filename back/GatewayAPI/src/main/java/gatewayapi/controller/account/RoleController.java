package gatewayapi.controller.account;

import gatewayapi.business.account.RoleBusiness;
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

    private final RoleBusiness roleBusiness;
    private final FeignExecutor feignExecutor;

    public RoleController(RoleBusiness roleBusiness, FeignExecutor feignExecutor) {
        this.roleBusiness = roleBusiness;
        this.feignExecutor = feignExecutor;
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllRoles() {
        return feignExecutor.wrap(roleBusiness::getAllRoles);
    }

    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getRoleById(@PathParam("id") Integer id) {
        return feignExecutor.wrap(() -> roleBusiness.getRoleById(id));
    }
}