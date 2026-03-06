package gatewayapi.controller.operation;

import dto.operationapi.Operation;
import dto.operationapi.OperationState;
import gatewayapi.client.OperationClient;
import gatewayapi.wrapper.FeignExecutor;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.springframework.stereotype.Controller;

@Controller
@Path("/operations")
public class OperationController {

    private final OperationClient operationClient;
    private final FeignExecutor feignExecutor;

    public OperationController(OperationClient operationClient, FeignExecutor feignExecutor) {
        this.operationClient = operationClient;
        this.feignExecutor = feignExecutor;
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllOperations() {
        return feignExecutor.wrap(operationClient::getAllOperations);
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response createOperation(Operation operation) {
        return feignExecutor.wrap(() -> operationClient.createOperation(operation));
    }

    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getOperationById(@PathParam("id") Integer id) {
        return feignExecutor.wrap(() -> operationClient.getOperationById(id));
    }

    @PATCH
    @Path("/{id}/state")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateOperationState(@PathParam("id") Integer id, OperationState state) {
        return feignExecutor.wrap(() -> operationClient.updateOperationState(id, state));
    }

    @POST
    @Path("/{id}/cancel")
    @Produces(MediaType.APPLICATION_JSON)
    public Response cancelOperation(@PathParam("id") Integer id) {
        return feignExecutor.wrap(() -> operationClient.cancelOperation(id));
    }
}
