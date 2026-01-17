package accountapi.controller;

import accountapi.business.PersonalInformationBusiness;
import accountapi.entity.PersonalInformationEntity;
import accountapi.mapper.PersonalInformationMapper;
import dto.accountapi.PersonalInformation;
import dto.accountapi.PersonalInformationRegister;
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
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllPersonalInformation() {
        List<PersonalInformationEntity> entities = personalInformationBusiness.getAllPersonalInformation();

        List<PersonalInformation> dtos = new ArrayList<>();
        for (PersonalInformationEntity entity : entities) {
            dtos.add(PersonalInformationMapper.toDto(entity));
        }
        return Response.ok(dtos).build();
    }

    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getPersonalInformationById(@PathParam("id") Integer id) {
        PersonalInformationEntity entity = personalInformationBusiness.getPersonalInformationById(id);

        if (entity == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.ok(PersonalInformationMapper.toDto(entity)).build();
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response createPersonalInformation(PersonalInformationRegister registerDto) {
        PersonalInformation personalInformationCreated = personalInformationBusiness.createPersonalInformation(registerDto);

        return Response.status(Response.Status.CREATED).entity(personalInformationCreated).build();
    }
}