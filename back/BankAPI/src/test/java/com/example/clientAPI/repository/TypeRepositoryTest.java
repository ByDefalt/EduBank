package com.example.clientAPI.repository;

import com.example.clientAPI.entity.TypesEntity;
import dto.bankapi.Type;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@JdbcTest
@ActiveProfiles("test")
@Import(TypeRepository.class)
class TypeRepositoryTest {

    @Autowired
    private TypeRepository typeRepository;

    private TypesEntity buildType(String name) {
        TypesEntity type = new TypesEntity();
        type.setName(name);
        return type;
    }

    // ==================== CREATE ====================

    @Test
    void testCreateType() {
        TypesEntity type = buildType("Livret Jeune");

        TypesEntity created = typeRepository.createType(type);

        assertNotNull(created);
        assertEquals("Livret Jeune", created.getName());
    }

    // ==================== READ ====================

    @Test
    void testGetAllTypes() {
        // Le schema.sql de test initialise déjà des types (ex: "Compte Courant", "Livret A" etc.)
        List<TypesEntity> result = typeRepository.getAllTypes();

        assertNotNull(result);
        assertFalse(result.isEmpty());
    }

    @Test
    void testGetAllTypesAfterCreate() {
        int sizeBefore = typeRepository.getAllTypes().size();

        typeRepository.createType(buildType("Compte Pro"));

        List<TypesEntity> result = typeRepository.getAllTypes();
        assertEquals(sizeBefore + 1, result.size());
    }

    @Test
    void testGetTypeById() {
        // On récupère un type existant depuis l'initialisation du schema
        List<TypesEntity> all = typeRepository.getAllTypes();
        assertFalse(all.isEmpty());
        Integer existingId = all.get(0).getId();

        TypesEntity found = typeRepository.getTypeById(existingId);

        assertNotNull(found);
        assertEquals(existingId, found.getId());
        assertNotNull(found.getName());
    }

    @Test
    void testGetTypeByIdReturnsNullWhenNotFound() {
        TypesEntity result = typeRepository.getTypeById(9999);

        assertNull(result);
    }

    @Test
    void testGetTypeByIdMatchesName() {
        typeRepository.createType(buildType("PEL Test"));

        List<TypesEntity> all = typeRepository.getAllTypes();
        TypesEntity pelType = all.stream()
                .filter(t -> "PEL Test".equals(t.getName()))
                .findFirst()
                .orElse(null);

        assertNotNull(pelType);
        assertNotNull(pelType.getId());

        TypesEntity found = typeRepository.getTypeById(pelType.getId());
        assertEquals("PEL Test", found.getName());
    }
}
