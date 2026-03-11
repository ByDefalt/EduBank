package com.example.clientAPI.business;

import com.example.clientAPI.entity.OfferEntity;
import com.example.clientAPI.entity.OfferInputEntity;
import com.example.clientAPI.exception.FunctionalException;
import com.example.clientAPI.exception.NotFoundException;
import com.example.clientAPI.exception.UnauthorizedException;
import com.example.clientAPI.mapper.OfferMapper;
import com.example.clientAPI.repository.OfferRepository;
import dto.offerapi.Offer;
import dto.offerapi.OfferInput;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OfferBusiness {

    private final OfferRepository offerRepository;
    private final OfferMapper offerMapper;

    public OfferBusiness(OfferRepository offerRepository, OfferMapper offerMapper) {
        this.offerRepository = offerRepository;
        this.offerMapper = offerMapper;
    }

    public List<Offer> getAllOffers() {
        List<OfferEntity> entities = offerRepository.getAllOffers();
        return offerMapper.toDTOList(entities);
    }

    public List<Offer> getActiveOffers() {
        List<OfferEntity> entities = offerRepository.getActiveOffers();
        return offerMapper.toDTOList(entities);
    }

    public Offer getOfferById(int id) {
        try {
            OfferEntity entity = offerRepository.getOfferById(id);
            return offerMapper.toDTO(entity);
        } catch (EmptyResultDataAccessException e) {
            throw new NotFoundException("OFFER_NOT_FOUND", "Offre introuvable avec l'id : " + id);
        }
    }

    public Offer createOffer(String token, OfferInput dto) {
        checkAuthorization(token);
        validateOfferInput(dto);
        OfferInputEntity inputEntity = offerMapper.toEntity(dto);
        OfferEntity entity = offerRepository.createOffer(inputEntity);
        return offerMapper.toDTO(entity);
    }

    public Offer updateOffer(String token, int id, OfferInput dto) {
        checkAuthorization(token);
        validateOfferInput(dto);
        try {
            offerRepository.getOfferById(id);
        } catch (EmptyResultDataAccessException e) {
            throw new NotFoundException("OFFER_NOT_FOUND", "Offre introuvable avec l'id : " + id);
        }
        OfferInputEntity inputEntity = offerMapper.toEntity(dto);
        OfferEntity entity = offerRepository.updateOffer(id, inputEntity);
        return offerMapper.toDTO(entity);
    }

    public void deleteOffer(String token, int id) {
        checkAuthorization(token);
        try {
            offerRepository.getOfferById(id);
        } catch (EmptyResultDataAccessException e) {
            throw new NotFoundException("OFFER_NOT_FOUND", "Offre introuvable avec l'id : " + id);
        }
        offerRepository.deleteOffer(id);
    }

    private void checkAuthorization(String token) {
        if (token == null || !token.startsWith("Bearer ")) {
            throw new UnauthorizedException("UNAUTHORIZED", "Token d'autorisation manquant ou invalide");
        }
    }

    private void validateOfferInput(OfferInput dto) {
        if (dto == null) {
            throw new FunctionalException("INVALID_INPUT", "Le corps de la requête est obligatoire");
        }
        if (dto.getTitle() == null || dto.getTitle().isBlank()) {
            throw new FunctionalException("INVALID_TITLE", "Le titre de l'offre est obligatoire");
        }
        if (dto.getStartDate() == null || dto.getEndDate() == null) {
            throw new FunctionalException("INVALID_DATES", "Les dates de début et de fin sont obligatoires");
        }
        if (dto.getEndDate().isBefore(dto.getStartDate())) {
            throw new FunctionalException("INVALID_DATE_RANGE", "La date de fin doit être après la date de début");
        }
    }
}