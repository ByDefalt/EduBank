package com.example.clientAPI.controller;

import com.example.clientAPI.business.OfferBusiness;
import dto.offerapi.Offer;
import dto.offerapi.OfferInput;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.springframework.stereotype.Controller;

import java.util.List;

@Controller
@Path("/offers")
public class OfferController {

    private final OfferBusiness offerBusiness;

    public OfferController(OfferBusiness offerBusiness) {
        this.offerBusiness = offerBusiness;
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllOffers(@QueryParam("state") String state,
                                 @QueryParam("active_only") @DefaultValue("true") boolean activeOnly) {
        List<Offer> offers;
        if (activeOnly) {
            offers = offerBusiness.getActiveOffers();
        } else {
            offers = offerBusiness.getAllOffers();
        }
        return Response.ok(offers).build();
    }

    @GET
    @Path("/active")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getActiveOffers() {
        List<Offer> offers = offerBusiness.getActiveOffers();
        return Response.ok(offers).build();
    }

    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getOfferById(@PathParam("id") int id) {
        Offer offer = offerBusiness.getOfferById(id);
        if (offer == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.ok(offer).build();
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response createOffer(@HeaderParam("Authorization") String token, OfferInput dto) {
        if (token == null || !token.startsWith("Bearer ")) {
            return Response.status(Response.Status.UNAUTHORIZED).build();
        }
        Offer created = offerBusiness.createOffer(dto);
        return Response.status(Response.Status.CREATED).entity(created).build();
    }

    @PUT
    @Path("/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateOffer(@HeaderParam("Authorization") String token,
                                @PathParam("id") int id, OfferInput dto) {
        if (token == null || !token.startsWith("Bearer ")) {
            return Response.status(Response.Status.UNAUTHORIZED).build();
        }
        Offer updated = offerBusiness.updateOffer(id, dto);
        if (updated == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.ok(updated).build();
    }

    @DELETE
    @Path("/{id}")
    public Response deleteOffer(@HeaderParam("Authorization") String token,
                                @PathParam("id") int id) {
        if (token == null || !token.startsWith("Bearer ")) {
            return Response.status(Response.Status.UNAUTHORIZED).build();
        }
        offerBusiness.deleteOffer(id);
        return Response.noContent().build();
    }
}