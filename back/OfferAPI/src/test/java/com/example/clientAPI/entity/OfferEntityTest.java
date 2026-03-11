package com.example.clientAPI.entity;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class OfferEntityTest {

    @Test
    void testDefaultConstructor() {
        OfferEntity entity = new OfferEntity();
        assertEquals(0, entity.getId());
        assertNull(entity.getPicturePath());
        assertNull(entity.getTitle());
        assertNull(entity.getDescription());
        assertNull(entity.getState());
        assertNull(entity.getStartDate());
        assertNull(entity.getEndDate());
    }

    @Test
    void testParameterizedConstructor() {
        LocalDate start = LocalDate.of(2026, 1, 1);
        LocalDate end = LocalDate.of(2026, 12, 31);

        OfferEntity entity = new OfferEntity(1, "/images/test.jpg", "Titre",
                "Description", "active", start, end);

        assertEquals(1, entity.getId());
        assertEquals("/images/test.jpg", entity.getPicturePath());
        assertEquals("Titre", entity.getTitle());
        assertEquals("Description", entity.getDescription());
        assertEquals("active", entity.getState());
        assertEquals(start, entity.getStartDate());
        assertEquals(end, entity.getEndDate());
    }

    @Test
    void testGettersSetters() {
        OfferEntity entity = new OfferEntity();
        LocalDate start = LocalDate.of(2026, 3, 1);
        LocalDate end = LocalDate.of(2026, 6, 30);

        entity.setId(5);
        entity.setPicturePath("/images/promo.jpg");
        entity.setTitle("Promo");
        entity.setDescription("Promo description");
        entity.setState("inactive");
        entity.setStartDate(start);
        entity.setEndDate(end);

        assertEquals(5, entity.getId());
        assertEquals("/images/promo.jpg", entity.getPicturePath());
        assertEquals("Promo", entity.getTitle());
        assertEquals("Promo description", entity.getDescription());
        assertEquals("inactive", entity.getState());
        assertEquals(start, entity.getStartDate());
        assertEquals(end, entity.getEndDate());
    }
}

