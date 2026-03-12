package com.example.clientAPI.business;

import com.example.clientAPI.entity.TypesEntity;
import com.example.clientAPI.mapper.TypeMapper;
import com.example.clientAPI.repository.TypeRepository;
import dto.bankapi.Type;
import jakarta.ws.rs.NotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TypeBusiness {

    private final TypeRepository typeRepository;

    public TypeBusiness(TypeRepository typeRepository) {
        this.typeRepository = typeRepository;
    }

    public List<Type> getAllTypes() {
        List<TypesEntity> entities = typeRepository.getAllTypes();
        return entities.stream().map(TypeMapper::toDto).collect(Collectors.toList());
    }

    public Type getTypeById(Integer id) {
        TypesEntity entity = typeRepository.getTypeById(id);
        if (entity == null) {
            throw new NotFoundException("Type non trouvé");
        }
        return TypeMapper.toDto(entity);
    }

    public Type createType(Type dto) {
        if (dto.getName() == null || dto.getName().trim().isEmpty()) {
            throw new IllegalArgumentException("Le nom du type est obligatoire");
        }
        List<TypesEntity> existingTypes = typeRepository.getAllTypes();
        boolean exists = existingTypes.stream()
                .anyMatch(t -> t.getName().equalsIgnoreCase(dto.getName().trim()));
        if (exists) {
            throw new IllegalArgumentException("Ce type de compte existe déjà");
        }
        dto.setName(dto.getName().trim());
        TypesEntity entity = TypeMapper.toEntity(dto);
        TypesEntity created = typeRepository.createType(entity);
        return TypeMapper.toDto(created);
    }
}