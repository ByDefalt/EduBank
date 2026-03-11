package com.example.clientAPI.entity;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class OfferInputEntityTest {

    @Test
    void testDefaultConstructor() {
        OfferInputEntity entity = new OfferInputEntity();
        assertNull(entity.getPicturePath());
        assertNull(entity.getTitle());
        assertNull(entity.getDescription());
        assertNull(entity.getState());
        assertNull(entity.getStartDate());
        assertNull(entity.getEndDate());
    }

    @Test
    void testParameterizedConstructor() {
        LocalDate start = LocalDate.of(2026, 3, 1);
        LocalDate end = LocalDate.of(2026, 6, 30);

        OfferInputEntity entity = new OfferInputEntity("/images/new.jpg",
                "Nouvelle Offre", "Description", "active", start, end);

        assertEquals("/images/new.jpg", entity.getPicturePath());
        assertEquals("Nouvelle Offre", entity.getTitle());
        assertEquals("Description", entity.getDescription());
        assertEquals("active", entity.getState());
        assertEquals(start, entity.getStartDate());
        assertEquals(end, entity.getEndDate());
    }

    @Test
    void testGettersSetters() {
        OfferInputEntity entity = new OfferInputEntity();
        LocalDate start = LocalDate.of(2026, 4, 1);
        LocalDate end = LocalDate.of(2026, 4, 30);

        entity.setPicturePath("/images/promo.jpg");
        entity.setTitle("Promo");
        entity.setDescription("Promo desc");
        entity.setState("inactive");
        entity.setStartDate(start);
        entity.setEndDate(end);

        assertEquals("/images/promo.jpg", entity.getPicturePath());
        assertEquals("Promo", entity.getTitle());
        assertEquals("Promo desc", entity.getDescription());
        assertEquals("inactive", entity.getState());
        assertEquals(start, entity.getStartDate());
        assertEquals(end, entity.getEndDate());
    }
}

