package accountapi.controller;

import accountapi.annotation.AuthenticationRequired;
import accountapi.business.PersonalInformationBusiness;
import accountapi.entity.PersonalInformationEntity;
import accountapi.mapper.PersonalInformationMapper;
import dto.accountapi.PersonalInformation;
import dto.accountapi.PersonalInformationRegister;
import dto.accountapi.RoleEnum;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.springframework.stereotype.Controller;
import java.util.ArrayList;
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
        if (dtos.isEmpty()) {
            return Response.status(Response.Status.NO_CONTENT).build();
        }
        return Response.ok(dtos).build();
    }

    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getPersonalInformationById(@PathParam("id") Integer id) {
        PersonalInformation entity = personalInformationBusiness.getPersonalInformationById(id);
        if (entity == null) {
            return Response.status(Response.Status.NO_CONTENT).build();
        }
        return Response.ok(entity).build();
    }
}