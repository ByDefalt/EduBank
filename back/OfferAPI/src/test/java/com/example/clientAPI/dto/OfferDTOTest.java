package com.example.clientAPI.dto;

import dto.offerapi.Offer;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class OfferDTOTest {

    @Test
    void testOfferGettersSetters() {
        Offer offer = new Offer();
        offer.setId(1);
        offer.setPicturePath("/images/test.jpg");
        offer.setTitle("Titre");
        offer.setDescription("Description");
        offer.setState(Offer.StateEnum.ACTIVE);
        offer.setStartDate(LocalDate.of(2026, 1, 1));
        offer.setEndDate(LocalDate.of(2026, 12, 31));

        assertEquals(1, offer.getId());
        assertEquals("/images/test.jpg", offer.getPicturePath());
        assertEquals("Titre", offer.getTitle());
        assertEquals("Description", offer.getDescription());
        assertEquals(Offer.StateEnum.ACTIVE, offer.getState());
        assertEquals(LocalDate.of(2026, 1, 1), offer.getStartDate());
        assertEquals(LocalDate.of(2026, 12, 31), offer.getEndDate());
    }

    @Test
    void testOfferFluentSetters() {
        Offer offer = new Offer()
                .id(1)
                .picturePath("/images/test.jpg")
                .title("Titre")
                .description("Description")
                .state(Offer.StateEnum.INACTIVE)
                .startDate(LocalDate.of(2026, 1, 1))
                .endDate(LocalDate.of(2026, 6, 30));

        assertEquals(1, offer.getId());
        assertEquals("/images/test.jpg", offer.getPicturePath());
        assertEquals("Titre", offer.getTitle());
        assertEquals("Description", offer.getDescription());
        assertEquals(Offer.StateEnum.INACTIVE, offer.getState());
        assertEquals(LocalDate.of(2026, 1, 1), offer.getStartDate());
        assertEquals(LocalDate.of(2026, 6, 30), offer.getEndDate());
    }

    @Test
    void testOfferEquals() {
        Offer o1 = new Offer().id(1).title("T").description("D")
                .state(Offer.StateEnum.ACTIVE)
                .startDate(LocalDate.of(2026, 1, 1))
                .endDate(LocalDate.of(2026, 12, 31));
        Offer o2 = new Offer().id(1).title("T").description("D")
                .state(Offer.StateEnum.ACTIVE)
                .startDate(LocalDate.of(2026, 1, 1))
                .endDate(LocalDate.of(2026, 12, 31));
        Offer o3 = new Offer().id(2).title("Other").description("D")
                .state(Offer.StateEnum.ACTIVE)
                .startDate(LocalDate.of(2026, 1, 1))
                .endDate(LocalDate.of(2026, 12, 31));

        assertEquals(o1, o2);
        assertEquals(o1, o1);
        assertNotEquals(o1, o3);
        assertNotEquals(o1, null);
        assertNotEquals(o1, "string");
    }

    @Test
    void testOfferHashCode() {
        Offer o1 = new Offer().id(1).title("T").description("D")
                .state(Offer.StateEnum.ACTIVE)
                .startDate(LocalDate.of(2026, 1, 1))
                .endDate(LocalDate.of(2026, 12, 31));
        Offer o2 = new Offer().id(1).title("T").description("D")
                .state(Offer.StateEnum.ACTIVE)
                .startDate(LocalDate.of(2026, 1, 1))
                .endDate(LocalDate.of(2026, 12, 31));

        assertEquals(o1.hashCode(), o2.hashCode());
    }

    @Test
    void testOfferToString() {
        Offer offer = new Offer().id(1).title("Titre");
        String str = offer.toString();
        assertNotNull(str);
        assertTrue(str.contains("Titre"));
        assertTrue(str.contains("class Offer"));
    }

    @Test
    void testOfferNullFields() {
        Offer offer = new Offer();
        assertNull(offer.getId());
        assertNull(offer.getPicturePath());
        assertNull(offer.getTitle());
        assertNull(offer.getDescription());
        assertNull(offer.getState());
        assertNull(offer.getStartDate());
        assertNull(offer.getEndDate());
    }

    @Test
    void testOfferStateEnumFromValue() {
        assertEquals(Offer.StateEnum.ACTIVE, Offer.StateEnum.fromValue("active"));
        assertEquals(Offer.StateEnum.INACTIVE, Offer.StateEnum.fromValue("inactive"));
        assertEquals(Offer.StateEnum.EXPIRED, Offer.StateEnum.fromValue("expired"));
        assertNull(Offer.StateEnum.fromValue("unknown"));
    }

    @Test
    void testOfferStateEnumToString() {
        assertEquals("active", Offer.StateEnum.ACTIVE.toString());
        assertEquals("inactive", Offer.StateEnum.INACTIVE.toString());
        assertEquals("expired", Offer.StateEnum.EXPIRED.toString());
    }
}

