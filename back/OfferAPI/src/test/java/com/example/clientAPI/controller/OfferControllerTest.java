package com.example.clientAPI.controller;

import com.example.clientAPI.business.OfferBusiness;
import com.example.clientAPI.exception.NotFoundException;
import com.example.clientAPI.exception.UnauthorizedException;
import dto.offerapi.Offer;
import dto.offerapi.OfferInput;
import jakarta.ws.rs.core.Response;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OfferControllerTest {

    @Mock
    private OfferBusiness offerBusiness;

    @InjectMocks
    private OfferController offerController;

    private static final String VALID_TOKEN = "Bearer eyJhbGciOiJIUzI1NiJ9.test";
    private static final String INVALID_TOKEN = "InvalidToken";

    private Offer buildOffer(int id, String title) {
        Offer offer = new Offer();
        offer.setId(id);
        offer.setPicturePath("/images/test.jpg");
        offer.setTitle(title);
        offer.setDescription("Description test");
        offer.setState(Offer.StateEnum.ACTIVE);
        offer.setStartDate(LocalDate.of(2026, 1, 1));
        offer.setEndDate(LocalDate.of(2026, 12, 31));
        return offer;
    }

    private OfferInput buildOfferInput() {
        OfferInput input = new OfferInput();
        input.setPicturePath("/images/new.jpg");
        input.setTitle("Nouvelle Offre");
        input.setDescription("Nouvelle description");
        input.setState(OfferInput.StateEnum.ACTIVE);
        input.setStartDate(LocalDate.of(2026, 3, 1));
        input.setEndDate(LocalDate.of(2026, 6, 30));
        return input;
    }

    @Test
    void testGetAllOffersActiveOnly() {
        Offer offer = buildOffer(1, "Active Offer");
        when(offerBusiness.getActiveOffers()).thenReturn(List.of(offer));

        Response response = offerController.getAllOffers(null, true);

        assertEquals(200, response.getStatus());
        @SuppressWarnings("unchecked")
        List<Offer> body = (List<Offer>) response.getEntity();
        assertEquals(1, body.size());
        verify(offerBusiness).getActiveOffers();
        verify(offerBusiness, never()).getAllOffers();
    }

    @Test
    void testGetAllOffersAll() {
        Offer o1 = buildOffer(1, "Active");
        Offer o2 = buildOffer(2, "Inactive");
        o2.setState(Offer.StateEnum.INACTIVE);
        when(offerBusiness.getAllOffers()).thenReturn(List.of(o1, o2));

        Response response = offerController.getAllOffers(null, false);

        assertEquals(200, response.getStatus());
        @SuppressWarnings("unchecked")
        List<Offer> body = (List<Offer>) response.getEntity();
        assertEquals(2, body.size());
        verify(offerBusiness).getAllOffers();
        verify(offerBusiness, never()).getActiveOffers();
    }

    @Test
    void testGetAllOffersEmpty() {
        when(offerBusiness.getActiveOffers()).thenReturn(Collections.emptyList());

        Response response = offerController.getAllOffers(null, true);

        assertEquals(200, response.getStatus());
        @SuppressWarnings("unchecked")
        List<Offer> body = (List<Offer>) response.getEntity();
        assertTrue(body.isEmpty());
    }

    @Test
    void testGetActiveOffers() {
        Offer offer = buildOffer(1, "Active Offer");
        when(offerBusiness.getActiveOffers()).thenReturn(List.of(offer));

        Response response = offerController.getActiveOffers();

        assertEquals(200, response.getStatus());
        @SuppressWarnings("unchecked")
        List<Offer> body = (List<Offer>) response.getEntity();
        assertEquals(1, body.size());
    }

    @Test
    void testGetOfferById() {
        Offer offer = buildOffer(1, "Offre 1");
        when(offerBusiness.getOfferById(1)).thenReturn(offer);

        Response response = offerController.getOfferById(1);

        assertEquals(200, response.getStatus());
        Offer body = (Offer) response.getEntity();
        assertEquals(1, body.getId());
        assertEquals("Offre 1", body.getTitle());
    }

    @Test
    void testGetOfferByIdNotFound() {
        when(offerBusiness.getOfferById(999))
                .thenThrow(new NotFoundException("OFFER_NOT_FOUND", "Offre introuvable avec l'id : 999"));

        assertThrows(NotFoundException.class, () -> offerController.getOfferById(999));
    }

    @Test
    void testCreateOffer() {
        OfferInput input = buildOfferInput();
        Offer created = buildOffer(10, "Nouvelle Offre");
        when(offerBusiness.createOffer(VALID_TOKEN, input)).thenReturn(created);

        Response response = offerController.createOffer(VALID_TOKEN, input);

        assertEquals(201, response.getStatus());
        Offer body = (Offer) response.getEntity();
        assertEquals(10, body.getId());
    }

    @Test
    void testCreateOfferUnauthorized() {
        doThrow(new UnauthorizedException("UNAUTHORIZED", "Token manquant ou invalide"))
                .when(offerBusiness).createOffer(INVALID_TOKEN, buildOfferInput());

        assertThrows(UnauthorizedException.class,
                () -> offerController.createOffer(INVALID_TOKEN, buildOfferInput()));
    }

    @Test
    void testUpdateOffer() {
        OfferInput input = buildOfferInput();
        Offer updated = buildOffer(1, "Nouvelle Offre");
        when(offerBusiness.updateOffer(VALID_TOKEN, 1, input)).thenReturn(updated);

        Response response = offerController.updateOffer(VALID_TOKEN, 1, input);

        assertEquals(200, response.getStatus());
        Offer body = (Offer) response.getEntity();
        assertEquals(1, body.getId());
    }

    @Test
    void testUpdateOfferNotFound() {
        OfferInput input = buildOfferInput();
        when(offerBusiness.updateOffer(VALID_TOKEN, 999, input))
                .thenThrow(new NotFoundException("OFFER_NOT_FOUND", "Offre introuvable avec l'id : 999"));

        assertThrows(NotFoundException.class,
                () -> offerController.updateOffer(VALID_TOKEN, 999, input));
    }

    @Test
    void testUpdateOfferUnauthorized() {
        OfferInput input = buildOfferInput();
        doThrow(new UnauthorizedException("UNAUTHORIZED", "Token manquant ou invalide"))
                .when(offerBusiness).updateOffer(INVALID_TOKEN, 1, input);

        assertThrows(UnauthorizedException.class,
                () -> offerController.updateOffer(INVALID_TOKEN, 1, input));
    }

    @Test
    void testDeleteOffer() {
        doNothing().when(offerBusiness).deleteOffer(VALID_TOKEN, 1);

        Response response = offerController.deleteOffer(VALID_TOKEN, 1);

        assertEquals(204, response.getStatus());
        verify(offerBusiness).deleteOffer(VALID_TOKEN, 1);
    }

    @Test
    void testDeleteOfferNotFound() {
        doThrow(new NotFoundException("OFFER_NOT_FOUND", "Offre introuvable avec l'id : 999"))
                .when(offerBusiness).deleteOffer(VALID_TOKEN, 999);

        assertThrows(NotFoundException.class,
                () -> offerController.deleteOffer(VALID_TOKEN, 999));
    }

    @Test
    void testDeleteOfferUnauthorized() {
        doThrow(new UnauthorizedException("UNAUTHORIZED", "Token manquant ou invalide"))
                .when(offerBusiness).deleteOffer(INVALID_TOKEN, 1);

        assertThrows(UnauthorizedException.class,
                () -> offerController.deleteOffer(INVALID_TOKEN, 1));
    }
}

