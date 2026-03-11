package com.example.clientAPI.dto;

import dto.offerapi.OfferInput;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class OfferInputDTOTest {

    @Test
    void testOfferInputGettersSetters() {
        OfferInput input = new OfferInput();
        input.setPicturePath("/images/new.jpg");
        input.setTitle("Nouvelle Offre");
        input.setDescription("Description");
        input.setState(OfferInput.StateEnum.ACTIVE);
        input.setStartDate(LocalDate.of(2026, 3, 1));
        input.setEndDate(LocalDate.of(2026, 6, 30));

        assertEquals("/images/new.jpg", input.getPicturePath());
        assertEquals("Nouvelle Offre", input.getTitle());
        assertEquals("Description", input.getDescription());
        assertEquals(OfferInput.StateEnum.ACTIVE, input.getState());
        assertEquals(LocalDate.of(2026, 3, 1), input.getStartDate());
        assertEquals(LocalDate.of(2026, 6, 30), input.getEndDate());
    }

    @Test
    void testOfferInputFluentSetters() {
        OfferInput input = new OfferInput()
                .picturePath("/images/test.jpg")
                .title("Titre")
                .description("Desc")
                .state(OfferInput.StateEnum.INACTIVE)
                .startDate(LocalDate.of(2026, 1, 1))
                .endDate(LocalDate.of(2026, 12, 31));

        assertEquals("/images/test.jpg", input.getPicturePath());
        assertEquals("Titre", input.getTitle());
        assertEquals("Desc", input.getDescription());
        assertEquals(OfferInput.StateEnum.INACTIVE, input.getState());
    }

    @Test
    void testOfferInputEquals() {
        OfferInput i1 = new OfferInput().title("T").description("D")
                .state(OfferInput.StateEnum.ACTIVE)
                .startDate(LocalDate.of(2026, 1, 1))
                .endDate(LocalDate.of(2026, 12, 31));
        OfferInput i2 = new OfferInput().title("T").description("D")
                .state(OfferInput.StateEnum.ACTIVE)
                .startDate(LocalDate.of(2026, 1, 1))
                .endDate(LocalDate.of(2026, 12, 31));
        OfferInput i3 = new OfferInput().title("Other").description("D")
                .state(OfferInput.StateEnum.ACTIVE)
                .startDate(LocalDate.of(2026, 1, 1))
                .endDate(LocalDate.of(2026, 12, 31));

        assertEquals(i1, i2);
        assertEquals(i1, i1);
        assertNotEquals(i1, i3);
        assertNotEquals(i1, null);
        assertNotEquals(i1, "string");
    }

    @Test
    void testOfferInputHashCode() {
        OfferInput i1 = new OfferInput().title("T").description("D")
                .state(OfferInput.StateEnum.ACTIVE)
                .startDate(LocalDate.of(2026, 1, 1))
                .endDate(LocalDate.of(2026, 12, 31));
        OfferInput i2 = new OfferInput().title("T").description("D")
                .state(OfferInput.StateEnum.ACTIVE)
                .startDate(LocalDate.of(2026, 1, 1))
                .endDate(LocalDate.of(2026, 12, 31));

        assertEquals(i1.hashCode(), i2.hashCode());
    }

    @Test
    void testOfferInputToString() {
        OfferInput input = new OfferInput().title("Titre");
        String str = input.toString();
        assertNotNull(str);
        assertTrue(str.contains("Titre"));
    }

    @Test
    void testOfferInputNullFields() {
        OfferInput input = new OfferInput();
        assertNull(input.getPicturePath());
        assertNull(input.getTitle());
        assertNull(input.getDescription());
        assertNull(input.getState());
        assertNull(input.getStartDate());
        assertNull(input.getEndDate());
    }

    @Test
    void testOfferInputStateEnumFromValue() {
        assertEquals(OfferInput.StateEnum.ACTIVE, OfferInput.StateEnum.fromValue("active"));
        assertEquals(OfferInput.StateEnum.INACTIVE, OfferInput.StateEnum.fromValue("inactive"));
        assertEquals(OfferInput.StateEnum.EXPIRED, OfferInput.StateEnum.fromValue("expired"));
        assertNull(OfferInput.StateEnum.fromValue("unknown"));
    }

    @Test
    void testOfferInputStateEnumToString() {
        assertEquals("active", OfferInput.StateEnum.ACTIVE.toString());
        assertEquals("inactive", OfferInput.StateEnum.INACTIVE.toString());
        assertEquals("expired", OfferInput.StateEnum.EXPIRED.toString());
    }
}

