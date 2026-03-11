package com.example.clientAPI.entity;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class OfferStateTest {

    @Test
    void testActiveValue() {
        assertEquals("active", OfferState.active.name());
    }

    @Test
    void testInactiveValue() {
        assertEquals("inactive", OfferState.inactive.name());
    }

    @Test
    void testExpiredValue() {
        assertEquals("expired", OfferState.expired.name());
    }

    @Test
    void testValuesCount() {
        assertEquals(3, OfferState.values().length);
    }

    @Test
    void testValueOf() {
        assertEquals(OfferState.active, OfferState.valueOf("active"));
        assertEquals(OfferState.inactive, OfferState.valueOf("inactive"));
        assertEquals(OfferState.expired, OfferState.valueOf("expired"));
    }

    @Test
    void testValueOfInvalid() {
        assertThrows(IllegalArgumentException.class, () -> OfferState.valueOf("unknown"));
    }
}

