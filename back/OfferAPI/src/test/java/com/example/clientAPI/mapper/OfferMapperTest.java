package com.example.clientAPI.mapper;

import com.example.clientAPI.entity.OfferEntity;
import com.example.clientAPI.entity.OfferInputEntity;
import dto.offerapi.Offer;
import dto.offerapi.OfferInput;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class OfferMapperTest {

    private final OfferMapper mapper = new OfferMapper();


    @Test
    void testToDTOFromEntity() {
        OfferEntity entity = new OfferEntity(1, "/images/test.jpg", "Titre",
                "Description", "active",
                LocalDate.of(2026, 1, 1), LocalDate.of(2026, 12, 31));

        Offer dto = mapper.toDTO(entity);

        assertNotNull(dto);
        assertEquals(1, dto.getId());
        assertEquals("/images/test.jpg", dto.getPicturePath());
        assertEquals("Titre", dto.getTitle());
        assertEquals("Description", dto.getDescription());
        assertEquals(Offer.StateEnum.ACTIVE, dto.getState());
        assertEquals(LocalDate.of(2026, 1, 1), dto.getStartDate());
        assertEquals(LocalDate.of(2026, 12, 31), dto.getEndDate());
    }

    @Test
    void testToDTONull() {
        assertNull(mapper.toDTO(null));
    }

    @Test
    void testToDTOInactiveState() {
        OfferEntity entity = new OfferEntity(2, null, "Titre", "Desc",
                "inactive", LocalDate.of(2026, 1, 1), LocalDate.of(2026, 6, 30));

        Offer dto = mapper.toDTO(entity);

        assertNotNull(dto);
        assertEquals(Offer.StateEnum.INACTIVE, dto.getState());
    }

    @Test
    void testToDTOExpiredState() {
        OfferEntity entity = new OfferEntity(3, null, "Titre", "Desc",
                "expired", LocalDate.of(2025, 1, 1), LocalDate.of(2025, 6, 30));

        Offer dto = mapper.toDTO(entity);

        assertNotNull(dto);
        assertEquals(Offer.StateEnum.EXPIRED, dto.getState());
    }


    @Test
    void testToEntityFromDTO() {
        Offer dto = new Offer();
        dto.setId(1);
        dto.setPicturePath("/images/test.jpg");
        dto.setTitle("Titre");
        dto.setDescription("Description");
        dto.setState(Offer.StateEnum.ACTIVE);
        dto.setStartDate(LocalDate.of(2026, 1, 1));
        dto.setEndDate(LocalDate.of(2026, 12, 31));

        OfferEntity entity = mapper.toEntity(dto);

        assertNotNull(entity);
        assertEquals(1, entity.getId());
        assertEquals("/images/test.jpg", entity.getPicturePath());
        assertEquals("Titre", entity.getTitle());
        assertEquals("Description", entity.getDescription());
        assertEquals("active", entity.getState());
        assertEquals(LocalDate.of(2026, 1, 1), entity.getStartDate());
        assertEquals(LocalDate.of(2026, 12, 31), entity.getEndDate());
    }

    @Test
    void testToEntityFromDTONull() {
        assertNull(mapper.toEntity((Offer) null));
    }


    @Test
    void testToDTOList() {
        OfferEntity e1 = new OfferEntity(1, null, "A", "D1", "active",
                LocalDate.of(2026, 1, 1), LocalDate.of(2026, 6, 30));
        OfferEntity e2 = new OfferEntity(2, null, "B", "D2", "inactive",
                LocalDate.of(2026, 3, 1), LocalDate.of(2026, 9, 30));

        List<Offer> dtos = mapper.toDTOList(List.of(e1, e2));

        assertEquals(2, dtos.size());
        assertEquals("A", dtos.get(0).getTitle());
        assertEquals("B", dtos.get(1).getTitle());
    }

    @Test
    void testToDTOListEmpty() {
        List<Offer> dtos = mapper.toDTOList(Collections.emptyList());
        assertTrue(dtos.isEmpty());
    }


    @Test
    void testToEntityList() {
        Offer d1 = new Offer();
        d1.setId(1);
        d1.setTitle("A");
        d1.setDescription("D1");
        d1.setState(Offer.StateEnum.ACTIVE);
        d1.setStartDate(LocalDate.of(2026, 1, 1));
        d1.setEndDate(LocalDate.of(2026, 6, 30));

        List<OfferEntity> entities = mapper.toEntityList(List.of(d1));

        assertEquals(1, entities.size());
        assertEquals("A", entities.get(0).getTitle());
    }

    @Test
    void testToEntityListEmpty() {
        List<OfferEntity> entities = mapper.toEntityList(Collections.emptyList());
        assertTrue(entities.isEmpty());
    }


    @Test
    void testToEntityFromOfferInput() {
        OfferInput input = new OfferInput();
        input.setPicturePath("/images/new.jpg");
        input.setTitle("Nouvelle Offre");
        input.setDescription("Nouvelle description");
        input.setState(OfferInput.StateEnum.ACTIVE);
        input.setStartDate(LocalDate.of(2026, 3, 1));
        input.setEndDate(LocalDate.of(2026, 6, 30));

        OfferInputEntity entity = mapper.toEntity(input);

        assertNotNull(entity);
        assertEquals("/images/new.jpg", entity.getPicturePath());
        assertEquals("Nouvelle Offre", entity.getTitle());
        assertEquals("Nouvelle description", entity.getDescription());
        assertEquals("active", entity.getState());
        assertEquals(LocalDate.of(2026, 3, 1), entity.getStartDate());
        assertEquals(LocalDate.of(2026, 6, 30), entity.getEndDate());
    }

    @Test
    void testToEntityFromOfferInputNull() {
        assertNull(mapper.toEntity((OfferInput) null));
    }


    @Test
    void testToInputDTO() {
        OfferInputEntity entity = new OfferInputEntity("/images/promo.jpg",
                "Promo", "Desc promo", "inactive",
                LocalDate.of(2026, 4, 1), LocalDate.of(2026, 4, 30));

        OfferInput dto = mapper.toInputDTO(entity);

        assertNotNull(dto);
        assertEquals("/images/promo.jpg", dto.getPicturePath());
        assertEquals("Promo", dto.getTitle());
        assertEquals("Desc promo", dto.getDescription());
        assertEquals(OfferInput.StateEnum.INACTIVE, dto.getState());
        assertEquals(LocalDate.of(2026, 4, 1), dto.getStartDate());
        assertEquals(LocalDate.of(2026, 4, 30), dto.getEndDate());
    }

    @Test
    void testToInputDTONull() {
        assertNull(mapper.toInputDTO(null));
    }
}

