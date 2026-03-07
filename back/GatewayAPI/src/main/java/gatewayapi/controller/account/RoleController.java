package gatewayapi.controller.account;

import dto.accountapi.RoleEnum;
import gatewayapi.annotation.AuthenticationRequired;
import gatewayapi.business.account.RoleBusiness;
import gatewayapi.wrapper.FeignExecutor;
import jakarta.ws.rs.*;
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
    @AuthenticationRequired(RoleEnum.ADMIN)
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

    @GET
    @Path("/name/{name}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getRoleByName(@PathParam("name") String name) {
        return feignExecutor.wrap(() -> roleBusiness.getRoleByName(name));
    }
}