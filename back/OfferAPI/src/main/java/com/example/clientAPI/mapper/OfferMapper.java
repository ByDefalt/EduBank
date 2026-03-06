package com.example.clientAPI.mapper;

import com.example.clientAPI.entity.OfferEntity;
import com.example.clientAPI.entity.OfferInputEntity;
import dto.offerapi.Offer;
import dto.offerapi.OfferInput;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class OfferMapper {

    // ─── Offer <-> OfferEntity ───────────────────────────────────────────────

    public OfferEntity toEntity(Offer dto) {
        if (dto == null) return null;

        OfferEntity entity = new OfferEntity();
        entity.setId(dto.getId());
        entity.setPicturePath(dto.getPicturePath());
        entity.setTitle(dto.getTitle());
        entity.setDescription(dto.getDescription());
        entity.setState(dto.getState().toString());
        entity.setStartDate(dto.getStartDate());
        entity.setEndDate(dto.getEndDate());
        return entity;
    }

    public Offer toDTO(OfferEntity entity) {
        if (entity == null) return null;

        Offer dto = new Offer();
        dto.setId(entity.getId());
        dto.setPicturePath(entity.getPicturePath());
        dto.setTitle(entity.getTitle());
        dto.setDescription(entity.getDescription());
        dto.setState(Offer.StateEnum.fromValue(entity.getState()));
        dto.setStartDate(entity.getStartDate());
        dto.setEndDate(entity.getEndDate());
        return dto;
    }

    public List<Offer> toDTOList(List<OfferEntity> entities) {
        return entities.stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    public List<OfferEntity> toEntityList(List<Offer> dtos) {
        return dtos.stream()
                .map(this::toEntity)
                .collect(Collectors.toList());
    }

    // ─── OfferInput <-> OfferInputEntity ─────────────────────────────────────

    public OfferInputEntity toEntity(OfferInput dto) {
        if (dto == null) return null;

        OfferInputEntity entity = new OfferInputEntity();
        entity.setPicturePath(dto.getPicturePath());
        entity.setTitle(dto.getTitle());
        entity.setDescription(dto.getDescription());
        entity.setState(dto.getState().toString());
        entity.setStartDate(dto.getStartDate());
        entity.setEndDate(dto.getEndDate());
        return entity;
    }

    public OfferInput toInputDTO(OfferInputEntity entity) {
        if (entity == null) return null;

        OfferInput dto = new OfferInput();
        dto.setPicturePath(entity.getPicturePath());
        dto.setTitle(entity.getTitle());
        dto.setDescription(entity.getDescription());
        dto.setState(OfferInput.StateEnum.fromValue(entity.getState()));
        dto.setStartDate(entity.getStartDate());
        dto.setEndDate(entity.getEndDate());
        return dto;
    }
}