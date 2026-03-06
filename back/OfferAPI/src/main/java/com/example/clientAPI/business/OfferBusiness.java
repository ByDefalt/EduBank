package com.example.clientAPI.business;

import com.example.clientAPI.entity.OfferEntity;
import com.example.clientAPI.entity.OfferInputEntity;
import com.example.clientAPI.mapper.OfferMapper;
import com.example.clientAPI.repository.OfferRepository;
import dto.offerapi.Offer;
import dto.offerapi.OfferInput;
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
        OfferEntity entity = offerRepository.getOfferById(id);
        return offerMapper.toDTO(entity);
    }

    public Offer createOffer(OfferInput dto) {
        OfferInputEntity inputEntity = offerMapper.toEntity(dto);
        OfferEntity entity = offerRepository.createOffer(inputEntity);
        return offerMapper.toDTO(entity);
    }

    public Offer updateOffer(int id, OfferInput dto) {
        OfferInputEntity inputEntity = offerMapper.toEntity(dto);
        OfferEntity entity = offerRepository.updateOffer(id, inputEntity);
        return offerMapper.toDTO(entity);
    }

    public void deleteOffer(int id) {
        offerRepository.deleteOffer(id);
    }
}