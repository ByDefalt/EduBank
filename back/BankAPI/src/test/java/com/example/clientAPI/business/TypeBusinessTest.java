package com.example.clientAPI.business;

import com.example.clientAPI.entity.TypesEntity;
import com.example.clientAPI.repository.TypeRepository;
import dto.bankapi.Type;
import jakarta.ws.rs.NotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TypeBusinessTest {

    @Mock
    private TypeRepository typeRepository;

    @InjectMocks
    private TypeBusiness typeBusiness;

    // ==================== Helpers ====================

    private TypesEntity buildTypeEntity(Integer id, String name) {
        TypesEntity entity = new TypesEntity();
        entity.setId(id);
        entity.setName(name);
        return entity;
    }

    private Type buildTypeDto(String name) {
        Type type = new Type();
        type.setName(name);
        return type;
    }

    // ==================== getAllTypes ====================

    @Test
    void testGetAllTypes() {
        when(typeRepository.getAllTypes()).thenReturn(List.of(
                buildTypeEntity(1, "Compte Courant"),
                buildTypeEntity(2, "Livret A")
        ));

        List<Type> result = typeBusiness.getAllTypes();

        assertEquals(2, result.size());
        assertEquals("Compte Courant", result.get(0).getName());
        assertEquals("Livret A", result.get(1).getName());
    }

    @Test
    void testGetAllTypesReturnsEmptyList() {
        when(typeRepository.getAllTypes()).thenReturn(List.of());

        List<Type> result = typeBusiness.getAllTypes();

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    // ==================== getTypeById ====================

    @Test
    void testGetTypeById() {
        when(typeRepository.getTypeById(1)).thenReturn(buildTypeEntity(1, "Compte Courant"));

        Type result = typeBusiness.getTypeById(1);

        assertNotNull(result);
        assertEquals(1, result.getId());
        assertEquals("Compte Courant", result.getName());
    }

    @Test
    void testGetTypeByIdThrowsNotFoundExceptionWhenNotFound() {
        when(typeRepository.getTypeById(9999)).thenReturn(null);

        NotFoundException ex = assertThrows(
                NotFoundException.class,
                () -> typeBusiness.getTypeById(9999)
        );

        assertTrue(ex.getMessage().contains("non trouvé"));
    }

    // ==================== createType ====================

    @Test
    void testCreateType() {
        when(typeRepository.getAllTypes()).thenReturn(List.of());
        when(typeRepository.createType(any(TypesEntity.class))).thenReturn(buildTypeEntity(5, "Livret Jeune"));

        Type result = typeBusiness.createType(buildTypeDto("Livret Jeune"));

        assertNotNull(result);
        assertEquals(5, result.getId());
        assertEquals("Livret Jeune", result.getName());
    }

    @Test
    void testCreateTypeTrimsName() {
        when(typeRepository.getAllTypes()).thenReturn(List.of());
        when(typeRepository.createType(any(TypesEntity.class))).thenReturn(buildTypeEntity(6, "PEL"));

        typeBusiness.createType(buildTypeDto("  PEL  "));

        verify(typeRepository).createType(argThat(e -> "PEL".equals(e.getName())));
    }

    @Test
    void testCreateTypeThrowsIllegalArgumentExceptionOnNullName() {
        Type dto = new Type();
        dto.setName(null);

        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> typeBusiness.createType(dto)
        );

        assertTrue(ex.getMessage().contains("obligatoire"));
    }

    @Test
    void testCreateTypeThrowsIllegalArgumentExceptionOnBlankName() {
        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> typeBusiness.createType(buildTypeDto("   "))
        );

        assertTrue(ex.getMessage().contains("obligatoire"));
    }

    @Test
    void testCreateTypeThrowsIllegalArgumentExceptionOnDuplicateName() {
        when(typeRepository.getAllTypes()).thenReturn(List.of(buildTypeEntity(1, "Compte Courant")));

        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> typeBusiness.createType(buildTypeDto("compte courant"))
        );

        assertTrue(ex.getMessage().contains("déjà"));
    }

    @Test
    void testCreateTypeThrowsIllegalArgumentExceptionOnExactDuplicateName() {
        when(typeRepository.getAllTypes()).thenReturn(List.of(buildTypeEntity(2, "Livret A")));

        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> typeBusiness.createType(buildTypeDto("Livret A"))
        );

        assertEquals("Ce type de compte existe déjà", ex.getMessage());
    }
}