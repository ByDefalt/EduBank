package accountapi.controller;

import accountapi.annotation.AuthenticationRequired;
import accountapi.business.RoleBusiness;
import accountapi.entity.RoleEntity;
import accountapi.mapper.RoleMapper;
import dto.accountapi.Role;
import dto.accountapi.RoleEnum;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.springframework.stereotype.Controller;

import java.util.List;

@Controller
@Path("/roles")
public class RoleController {

    private final RoleBusiness roleBusiness;

    public RoleController(RoleBusiness roleBusiness) {
        this.roleBusiness = roleBusiness;
    }

    @GET
    @AuthenticationRequired(RoleEnum.ADMIN)
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllRoles() {
        List<Role> roles = roleBusiness.getAllRoles();
        if (roles.isEmpty()) {
            return Response.status(Response.Status.NO_CONTENT).build();
        }
        return Response.ok(roles).build();
    }

    @GET
    @AuthenticationRequired(RoleEnum.CUSTOMER)
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getRoleById(@PathParam("id") Integer id) {
        Role role = roleBusiness.getRoleById(id);
        if (role == null) {
            return Response.status(Response.Status.NO_CONTENT).build();
        }
        return Response.ok(role).build();
    }
}
