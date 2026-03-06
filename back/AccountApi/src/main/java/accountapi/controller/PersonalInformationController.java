package accountapi.controller;

import accountapi.annotation.AuthenticationRequired;
import accountapi.business.PersonalInformationBusiness;
import dto.accountapi.PersonalInformation;
import dto.accountapi.PersonalInformationRegister;
import dto.accountapi.RoleEnum;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.springframework.stereotype.Controller;

import java.util.List;

@Controller
@Path("/personalInformation")
public class PersonalInformationController {

    private final PersonalInformationBusiness personalInformationBusiness;

    public PersonalInformationController(PersonalInformationBusiness personalInformationBusiness) {
        this.personalInformationBusiness = personalInformationBusiness;
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllPersonalInformation() {
        List<PersonalInformation> dtos = personalInformationBusiness.getAllPersonalInformation();
        return Response.ok(dtos).build();
    }

    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getPersonalInformationById(@PathParam("id") Integer id) {
        PersonalInformation entity = personalInformationBusiness.getPersonalInformationById(id);
        return Response.ok(entity).build();
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response createPersonalInformation(PersonalInformationRegister registerDto) {
        PersonalInformation created = personalInformationBusiness.createPersonalInformation(registerDto);
        return Response.status(Response.Status.CREATED).entity(created).build();
    }
}