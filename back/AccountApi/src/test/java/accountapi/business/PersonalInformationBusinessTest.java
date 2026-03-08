package accountapi.business;

import accountapi.entity.PersonalInformationEntity;
import accountapi.exception.FunctionalException;
import accountapi.exception.NotFoundException;
import accountapi.repository.PersonalInformationRepository;
import dto.accountapi.PersonalInformation;
import dto.accountapi.PersonalInformationRegister;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PersonalInformationBusinessTest {

    @Mock
    private PersonalInformationRepository personalInformationRepository;

    @InjectMocks
    private PersonalInformationBusiness personalInformationBusiness;

    @Test
    void testGetAllPersonalInformation() {
        PersonalInformationEntity entity = new PersonalInformationEntity();
        entity.setId(100);
        entity.setFirstname("Jean");
        entity.setLastname("Martin");
        entity.setEmail("jean.martin@example.com");
        entity.setAddress("123 Rue de la République, 69001 Lyon");
        entity.setPhoneNumber("+33698765432");

        when(personalInformationRepository.findAll()).thenReturn(java.util.Collections.singletonList(entity));

        List<PersonalInformation> results = personalInformationBusiness.getAllPersonalInformation();

        assertEquals(1, results.size());
        assertEquals(entity.getFirstname(), results.get(0).getFirstname());
    }

    @Test
    void testGetPersonalInformationById() {
        PersonalInformationEntity entity = new PersonalInformationEntity();
        entity.setId(100);
        entity.setFirstname("Jean");
        entity.setLastname("Martin");
        entity.setEmail("jean.martin@example.com");
        entity.setAddress("123 Rue de la République, 69001 Lyon");
        entity.setPhoneNumber("+33698765432");

        when(personalInformationRepository.findById(100)).thenReturn(entity);

        PersonalInformation result = personalInformationBusiness.getPersonalInformationById(100);

        assertEquals(entity.getFirstname(), result.getFirstname());
        assertEquals(entity.getLastname(), result.getLastname());
    }

    @Test
    void testCreatePersonalInformation() {
        PersonalInformationRegister registerDto = new PersonalInformationRegister();
        registerDto.setFirstname("Jean");
        registerDto.setLastname("Martin");
        registerDto.setEmail("jean.martin@example.com");
        registerDto.setAddress("123 Rue de la République, 69001 Lyon");
        registerDto.setPhoneNumber("+33698765432");

        PersonalInformationEntity savedEntity = new PersonalInformationEntity();
        savedEntity.setId(100);
        savedEntity.setFirstname("Jean");
        savedEntity.setLastname("Martin");
        savedEntity.setEmail("jean.martin@example.com");
        savedEntity.setAddress("123 Rue de la République, 69001 Lyon");
        savedEntity.setPhoneNumber("+33698765432");

        when(personalInformationRepository.create(any(PersonalInformationEntity.class))).thenReturn(savedEntity);

        PersonalInformation result = personalInformationBusiness.createPersonalInformation(registerDto);

        assertEquals(savedEntity.getFirstname(), result.getFirstname());
        assertEquals(savedEntity.getLastname(), result.getLastname());
    }

    @Test
    void testGetPersonalInformationByIdNotFound() {
        when(personalInformationRepository.findById(999)).thenReturn(null);

        assertThrows(NotFoundException.class, () -> personalInformationBusiness.getPersonalInformationById(999));
    }

    @Test
    void testCreatePersonalInformationFails() {
        PersonalInformationRegister registerDto = new PersonalInformationRegister();
        registerDto.setFirstname("Jean");
        registerDto.setLastname("Martin");
        registerDto.setEmail("jean.martin@example.com");

        when(personalInformationRepository.create(any(PersonalInformationEntity.class))).thenReturn(null);

        assertThrows(FunctionalException.class, () -> personalInformationBusiness.createPersonalInformation(registerDto));
    }

    @Test
    void testDeletePersonalInformation() {
        when(personalInformationRepository.delete(100)).thenReturn(true);

        boolean result = personalInformationBusiness.deletePersonalInformation(100);

        assertTrue(result);
        verify(personalInformationRepository).delete(100);
    }

    @Test
    void testUpdatePersonalInformation() {
        PersonalInformation dto = new PersonalInformation();
        dto.setId(100);
        dto.setFirstname("Jean");
        dto.setLastname("Dupont");
        dto.setEmail("jean.dupont@example.com");
        dto.setAddress("456 Avenue des Fleurs, 75008 Paris");
        dto.setPhoneNumber("+33612345678");

        PersonalInformationEntity existing = new PersonalInformationEntity();
        existing.setId(100);
        existing.setFirstname("Jean");
        existing.setLastname("Martin");
        existing.setEmail("jean.martin@example.com");
        existing.setAddress("123 Rue de la République, 69001 Lyon");
        existing.setPhoneNumber("+33698765432");

        PersonalInformationEntity updated = new PersonalInformationEntity();
        updated.setId(100);
        updated.setFirstname("Jean");
        updated.setLastname("Dupont");
        updated.setEmail("jean.dupont@example.com");
        updated.setAddress("456 Avenue des Fleurs, 75008 Paris");
        updated.setPhoneNumber("+33612345678");

        when(personalInformationRepository.findById(100)).thenReturn(existing);
        when(personalInformationRepository.update(any(Integer.class), any(PersonalInformationEntity.class))).thenReturn(updated);

        PersonalInformation result = personalInformationBusiness.updatePersonalInformation(100, dto);

        assertEquals("Jean", result.getFirstname());
        assertEquals("Dupont", result.getLastname());
        assertEquals("jean.dupont@example.com", result.getEmail());
        verify(personalInformationRepository).update(any(Integer.class), any(PersonalInformationEntity.class));
    }

    @Test
    void testUpdatePersonalInformationNotFound() {
        PersonalInformation dto = new PersonalInformation();
        dto.setId(999);
        dto.setFirstname("Jean");
        dto.setLastname("Dupont");

        when(personalInformationRepository.findById(999)).thenReturn(null);

        assertThrows(NotFoundException.class, () -> personalInformationBusiness.updatePersonalInformation(999, dto));
    }

    @Test
    void testUpdatePersonalInformationFails() {
        PersonalInformation dto = new PersonalInformation();
        dto.setId(100);
        dto.setFirstname("Jean");
        dto.setLastname("Dupont");
        dto.setEmail("jean.dupont@example.com");
        dto.setAddress("456 Avenue des Fleurs, 75008 Paris");
        dto.setPhoneNumber("+33612345678");

        PersonalInformationEntity existing = new PersonalInformationEntity();
        existing.setId(100);
        existing.setFirstname("Jean");
        existing.setLastname("Martin");
        existing.setEmail("jean.martin@example.com");
        existing.setAddress("123 Rue de la République, 69001 Lyon");
        existing.setPhoneNumber("+33698765432");

        when(personalInformationRepository.findById(100)).thenReturn(existing);
        when(personalInformationRepository.update(any(Integer.class), any(PersonalInformationEntity.class))).thenReturn(null);

        assertThrows(FunctionalException.class, () -> personalInformationBusiness.updatePersonalInformation(100, dto));
    }

    @Test
    void testGetAllPersonalInformationEmpty() {
        when(personalInformationRepository.findAll()).thenReturn(java.util.Collections.emptyList());

        List<PersonalInformation> results = personalInformationBusiness.getAllPersonalInformation();

        assertTrue(results.isEmpty());
    }
}