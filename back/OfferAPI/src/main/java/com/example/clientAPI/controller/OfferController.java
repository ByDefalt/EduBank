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
    public Response getAllOffers() {
        List<Offer> offers;
        offers = offerBusiness.getAllOffers();
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
        return Response.ok(offer).build();
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response createOffer(OfferInput dto) {
        Offer created = offerBusiness.createOffer(dto);
        return Response.status(Response.Status.CREATED).entity(created).build();
    }

    @PUT
    @Path("/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateOffer(@PathParam("id") int id, OfferInput dto) {
        Offer updated = offerBusiness.updateOffer(id, dto);
        return Response.ok(updated).build();
    }

    @DELETE
    @Path("/{id}")
    public Response deleteOffer(@PathParam("id") int id) {
        offerBusiness.deleteOffer(id);
        return Response.noContent().build();
    }
}