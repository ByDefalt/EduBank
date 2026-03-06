package gatewayapi.controller.account;

import dto.accountapi.PersonalInformationRegister;
import gatewayapi.business.account.PersonalInformationBusiness;
import gatewayapi.wrapper.FeignExecutor;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.springframework.stereotype.Controller;

@Controller
@Path("/personalInformation")
public class PersonalInformationController {

    private final PersonalInformationBusiness personalInformationBusiness;
    private final FeignExecutor feignExecutor;

    public PersonalInformationController(PersonalInformationBusiness personalInformationBusiness, FeignExecutor feignExecutor) {
        this.personalInformationBusiness = personalInformationBusiness;
        this.feignExecutor = feignExecutor;
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllPersonalInformation() {
        return feignExecutor.wrap(personalInformationBusiness::getAllPersonalInformation);
    }

    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getPersonalInformationById(@PathParam("id") Integer id) {
        return feignExecutor.wrap(() -> personalInformationBusiness.getPersonalInformationById(id));
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response createPersonalInformation(PersonalInformationRegister personalInformationRegister) {
        return feignExecutor.wrap(() -> personalInformationBusiness.createPersonalInformation(personalInformationRegister));
    }
}