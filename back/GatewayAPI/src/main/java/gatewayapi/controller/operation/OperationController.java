package gatewayapi.controller.operation;

import dto.accountapi.RoleEnum;
import dto.operationapi.Operation;
import dto.operationapi.OperationFilter;
import dto.operationapi.OperationState;
import gatewayapi.annotation.AuthenticationRequired;
import gatewayapi.business.operation.OperationBusiness;
import gatewayapi.wrapper.FeignExecutor;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.springframework.stereotype.Controller;

@Controller
@Path("/operations")
public class OperationController {

    private final OperationBusiness operationBusiness;
    private final FeignExecutor feignExecutor;

    public OperationController(OperationBusiness operationBusiness, FeignExecutor feignExecutor) {
        this.operationBusiness = operationBusiness;
        this.feignExecutor = feignExecutor;
    }

    @GET
    @AuthenticationRequired(RoleEnum.ADMIN)
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllOperations(@BeanParam OperationFilter filter) {
        return feignExecutor.wrap(() -> operationBusiness.getAllOperations(filter));
    }

    @GET
    @Path("/account/{accountId}")
    @AuthenticationRequired(RoleEnum.CUSTOMER)
    @Produces(MediaType.APPLICATION_JSON)
    public Response getOperationsByAccountId(@PathParam("accountId") String accountId, @BeanParam OperationFilter filter) {
        return feignExecutor.wrap(() -> operationBusiness.getOperationsByAccountId(accountId, filter));
    }

    @POST
    @AuthenticationRequired(RoleEnum.CUSTOMER)
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response createOperation(Operation operation) {
        return feignExecutor.wrap(() -> operationBusiness.createOperation(operation));
    }

    @GET
    @Path("/{id}")
    @AuthenticationRequired(RoleEnum.CUSTOMER)
    @Produces(MediaType.APPLICATION_JSON)
    public Response getOperationById(@PathParam("id") Integer id) {
        return feignExecutor.wrap(() -> operationBusiness.getOperationById(id));
    }

    @PATCH
    @Path("/{id}/state")
    @AuthenticationRequired(RoleEnum.CUSTOMER)
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateOperationState(@PathParam("id") Integer id, OperationState operationState) {
        return feignExecutor.wrap(() -> operationBusiness.updateOperationState(id, operationState));
    }

    @POST
    @Path("/{id}/cancel")
    @AuthenticationRequired(RoleEnum.CUSTOMER)
    @Produces(MediaType.APPLICATION_JSON)
    public Response cancelOperation(@PathParam("id") Integer id) {
        return feignExecutor.wrap(() -> operationBusiness.cancelOperation(id));
    }
}
