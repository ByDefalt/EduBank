package gatewayapi.controller.bankaccount;

import dto.accountapi.RoleEnum;
import dto.bankapiswagger.Type;
import gatewayapi.annotation.AuthenticationRequired;
import gatewayapi.business.bankaccount.TypeBusiness;
import gatewayapi.wrapper.FeignExecutor;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.springframework.stereotype.Controller;

@Controller
@Path("/bank")
public class TypeController {

    private final TypeBusiness typeBusiness;
    private final FeignExecutor feignExecutor;

    public TypeController(TypeBusiness typeBusiness, FeignExecutor feignExecutor) {
        this.typeBusiness = typeBusiness;
        this.feignExecutor = feignExecutor;
    }

    // ==================== ADMIN ====================

    @GET
    @Path("/admin/types")
    @AuthenticationRequired(RoleEnum.ADMIN)
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllTypes() {
        return feignExecutor.wrap(() -> typeBusiness.getAllTypes());
    }

    @GET
    @Path("/admin/types/{id}")
    @AuthenticationRequired(RoleEnum.ADMIN)
    @Produces(MediaType.APPLICATION_JSON)
    public Response getTypeById(@PathParam("id") Integer id) {
        return feignExecutor.wrap(() -> typeBusiness.getTypeById(id));
    }

    @POST
    @Path("/admin/types")
    @AuthenticationRequired(RoleEnum.ADMIN)
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response createType(Type type) {
        return feignExecutor.wrap(() -> typeBusiness.createType(type));
    }
}
