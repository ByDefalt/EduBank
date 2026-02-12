package com.example.clientAPI.business;

import com.example.clientAPI.entity.TypesEntity;
import com.example.clientAPI.mapper.TypeMapper;
import com.example.clientAPI.repository.TypeRepository;
import dto.bankapi.Type;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TypeBusiness {

    private final TypeRepository typeRepository;

    public TypeBusiness(TypeRepository typeRepository) {
        this.typeRepository = typeRepository;
    }

    // ============== Type Business Logic ==============

    public List<Type> getAllTypes() {
        List<TypesEntity> entities = typeRepository.getAllTypes();
        return entities.stream()
                .map(TypeMapper::toDto)
                .collect(Collectors.toList());
    }

    public Type createType(Type dto) {
        TypesEntity entity = TypeMapper.toEntity(dto);
        typeRepository.createType(entity);
        return TypeMapper.toDto(entity);
    }
}

