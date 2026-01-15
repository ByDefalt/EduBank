package accountapi.controller;

import accountapi.business.RoleBusiness;
import accountapi.entity.RoleEntity;
import accountapi.mapper.RoleMapper;
import dto.accountapi.Role;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.springframework.stereotype.Controller;
import java.util.ArrayList;
import java.util.List;

@Controller
@Path("/roles")
public class RoleController {

    private final RoleBusiness roleBusiness;

    public RoleController(RoleBusiness roleBusiness) {
        this.roleBusiness = roleBusiness;
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllRoles() {
        List<RoleEntity> roles = roleBusiness.getAllRoles();

        List<Role> dtos = new ArrayList<>();
        for (RoleEntity role : roles) {
            dtos.add(RoleMapper.toDto(role));
        }

        return Response.ok(dtos).build();
    }

    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getRoleById(@PathParam("id") Integer id) {
        RoleEntity role = roleBusiness.getRoleById(id);
        if (role == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.ok(RoleMapper.toDto(role)).build();
    }
}
