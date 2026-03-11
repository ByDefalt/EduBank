package com.example.clientAPI.business;

import com.example.clientAPI.entity.OfferEntity;
import com.example.clientAPI.entity.OfferInputEntity;
import com.example.clientAPI.mapper.OfferMapper;
import com.example.clientAPI.repository.OfferRepository;
import dto.offerapi.Offer;
import dto.offerapi.OfferInput;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OfferBusinessTest {

    @Mock
    private OfferRepository offerRepository;

    @Mock
    private OfferMapper offerMapper;

    @InjectMocks
    private OfferBusiness offerBusiness;


    private OfferEntity buildEntity(int id, String title, String state) {
        return new OfferEntity(id, "/images/test.jpg", title,
                "Description test", state,
                LocalDate.of(2026, 1, 1), LocalDate.of(2026, 12, 31));
    }

    private Offer buildDto(int id, String title, Offer.StateEnum state) {
        Offer dto = new Offer();
        dto.setId(id);
        dto.setPicturePath("/images/test.jpg");
        dto.setTitle(title);
        dto.setDescription("Description test");
        dto.setState(state);
        dto.setStartDate(LocalDate.of(2026, 1, 1));
        dto.setEndDate(LocalDate.of(2026, 12, 31));
        return dto;
    }

    private OfferInput buildInputDto() {
        OfferInput input = new OfferInput();
        input.setPicturePath("/images/new.jpg");
        input.setTitle("Nouvelle Offre");
        input.setDescription("Nouvelle description");
        input.setState(OfferInput.StateEnum.ACTIVE);
        input.setStartDate(LocalDate.of(2026, 3, 1));
        input.setEndDate(LocalDate.of(2026, 6, 30));
        return input;
    }

    private OfferInputEntity buildInputEntity() {
        return new OfferInputEntity("/images/new.jpg", "Nouvelle Offre",
                "Nouvelle description", "active",
                LocalDate.of(2026, 3, 1), LocalDate.of(2026, 6, 30));
    }


    @Test
    void testGetAllOffers() {
        OfferEntity entity = buildEntity(1, "Offre 1", "active");
        Offer dto = buildDto(1, "Offre 1", Offer.StateEnum.ACTIVE);

        when(offerRepository.getAllOffers()).thenReturn(List.of(entity));
        when(offerMapper.toDTOList(List.of(entity))).thenReturn(List.of(dto));

        List<Offer> result = offerBusiness.getAllOffers();

        assertEquals(1, result.size());
        assertEquals("Offre 1", result.get(0).getTitle());
        verify(offerRepository).getAllOffers();
    }

    @Test
    void testGetAllOffersEmpty() {
        when(offerRepository.getAllOffers()).thenReturn(Collections.emptyList());
        when(offerMapper.toDTOList(Collections.emptyList())).thenReturn(Collections.emptyList());

        List<Offer> result = offerBusiness.getAllOffers();

        assertTrue(result.isEmpty());
    }


    @Test
    void testGetActiveOffers() {
        OfferEntity entity = buildEntity(1, "Offre Active", "active");
        Offer dto = buildDto(1, "Offre Active", Offer.StateEnum.ACTIVE);

        when(offerRepository.getActiveOffers()).thenReturn(List.of(entity));
        when(offerMapper.toDTOList(List.of(entity))).thenReturn(List.of(dto));

        List<Offer> result = offerBusiness.getActiveOffers();

        assertEquals(1, result.size());
        assertEquals(Offer.StateEnum.ACTIVE, result.get(0).getState());
        verify(offerRepository).getActiveOffers();
    }

    @Test
    void testGetActiveOffersEmpty() {
        when(offerRepository.getActiveOffers()).thenReturn(Collections.emptyList());
        when(offerMapper.toDTOList(Collections.emptyList())).thenReturn(Collections.emptyList());

        List<Offer> result = offerBusiness.getActiveOffers();

        assertTrue(result.isEmpty());
    }


    @Test
    void testGetOfferById() {
        OfferEntity entity = buildEntity(1, "Offre 1", "active");
        Offer dto = buildDto(1, "Offre 1", Offer.StateEnum.ACTIVE);

        when(offerRepository.getOfferById(1)).thenReturn(entity);
        when(offerMapper.toDTO(entity)).thenReturn(dto);

        Offer result = offerBusiness.getOfferById(1);

        assertNotNull(result);
        assertEquals(1, result.getId());
        assertEquals("Offre 1", result.getTitle());
    }

    @Test
    void testGetOfferByIdNotFound() {
        when(offerRepository.getOfferById(999)).thenReturn(null);
        when(offerMapper.toDTO(null)).thenReturn(null);

        Offer result = offerBusiness.getOfferById(999);

        assertNull(result);
    }


    @Test
    void testCreateOffer() {
        OfferInput inputDto = buildInputDto();
        OfferInputEntity inputEntity = buildInputEntity();
        OfferEntity createdEntity = buildEntity(10, "Nouvelle Offre", "active");
        Offer expectedDto = buildDto(10, "Nouvelle Offre", Offer.StateEnum.ACTIVE);

        when(offerMapper.toEntity(inputDto)).thenReturn(inputEntity);
        when(offerRepository.createOffer(inputEntity)).thenReturn(createdEntity);
        when(offerMapper.toDTO(createdEntity)).thenReturn(expectedDto);

        Offer result = offerBusiness.createOffer(inputDto);

        assertNotNull(result);
        assertEquals(10, result.getId());
        assertEquals("Nouvelle Offre", result.getTitle());
        verify(offerRepository).createOffer(inputEntity);
    }


    @Test
    void testUpdateOffer() {
        OfferInput inputDto = buildInputDto();
        OfferInputEntity inputEntity = buildInputEntity();
        OfferEntity updatedEntity = buildEntity(1, "Nouvelle Offre", "active");
        Offer expectedDto = buildDto(1, "Nouvelle Offre", Offer.StateEnum.ACTIVE);

        when(offerMapper.toEntity(inputDto)).thenReturn(inputEntity);
        when(offerRepository.updateOffer(1, inputEntity)).thenReturn(updatedEntity);
        when(offerMapper.toDTO(updatedEntity)).thenReturn(expectedDto);

        Offer result = offerBusiness.updateOffer(1, inputDto);

        assertNotNull(result);
        assertEquals(1, result.getId());
        assertEquals("Nouvelle Offre", result.getTitle());
        verify(offerRepository).updateOffer(1, inputEntity);
    }

    @Test
    void testUpdateOfferNotFound() {
        OfferInput inputDto = buildInputDto();
        OfferInputEntity inputEntity = buildInputEntity();

        when(offerMapper.toEntity(inputDto)).thenReturn(inputEntity);
        when(offerRepository.updateOffer(999, inputEntity)).thenReturn(null);
        when(offerMapper.toDTO(null)).thenReturn(null);

        Offer result = offerBusiness.updateOffer(999, inputDto);

        assertNull(result);
    }


    @Test
    void testDeleteOffer() {
        doNothing().when(offerRepository).deleteOffer(1);

        offerBusiness.deleteOffer(1);

        verify(offerRepository).deleteOffer(1);
    }
}

