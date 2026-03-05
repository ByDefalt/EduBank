package accountapi.controller;

import accountapi.annotation.AuthenticationRequired;
import accountapi.business.PersonalInformationBusiness;
import dto.accountapi.Error;
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
    @AuthenticationRequired(RoleEnum.ADMIN)
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
        if (entity == null) {
            Error error = new Error()
                    .code("404")
                    .message("Non trouvé")
                    .details("Aucune information personnelle trouvée avec l'ID : " + id);
            return Response.status(Response.Status.NOT_FOUND).entity(error).build();
        }
        return Response.ok(entity).build();
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response createPersonalInformation(PersonalInformationRegister registerDto) {
        PersonalInformation created = personalInformationBusiness.createPersonalInformation(registerDto);
        if (created == null) {
            Error error = new Error()
                    .code("400")
                    .message("Requête invalide")
                    .details("Impossible de créer les informations personnelles");
            return Response.status(Response.Status.BAD_REQUEST).entity(error).build();
        }
        return Response.status(Response.Status.CREATED).entity(created).build();
    }
}