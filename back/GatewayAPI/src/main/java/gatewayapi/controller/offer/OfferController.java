package gatewayapi.controller.offer;

import dto.accountapi.RoleEnum;
import dto.offerapi.OfferInput;
import gatewayapi.annotation.AuthenticationRequired;
import gatewayapi.business.offer.OfferBusiness;
import gatewayapi.wrapper.FeignExecutor;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.springframework.stereotype.Controller;

@Controller
@Path("/offers")
public class OfferController {

    private final OfferBusiness offerBusiness;
    private final FeignExecutor feignExecutor;

    public OfferController(OfferBusiness offerBusiness, FeignExecutor feignExecutor) {
        this.offerBusiness = offerBusiness;
        this.feignExecutor = feignExecutor;
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllOffers() {
        return feignExecutor.wrap(offerBusiness::getAllOffers);
    }

    @GET
    @Path("/active")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getActiveOffers() {
        return feignExecutor.wrap(offerBusiness::getActiveOffers);
    }

    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getOfferById(@PathParam("id") int id) {
        return feignExecutor.wrap(() -> offerBusiness.getOfferById(id));
    }

    @POST
    @AuthenticationRequired(RoleEnum.ADMIN)
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response createOffer(OfferInput offerInput) {
        return feignExecutor.wrap(() -> offerBusiness.createOffer(offerInput));
    }

    @PUT
    @AuthenticationRequired(RoleEnum.ADMIN)
    @Path("/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateOffer(@PathParam("id") int id, OfferInput offerInput) {
        return feignExecutor.wrap(() -> offerBusiness.updateOffer(id, offerInput));
    }

    @DELETE
    @AuthenticationRequired(RoleEnum.ADMIN)
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteOffer(@PathParam("id") int id) {
        return feignExecutor.wrap(() -> {
            offerBusiness.deleteOffer(id);
            return null;
        });
    }
}

