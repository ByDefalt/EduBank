package accountapi.business;

import accountapi.entity.PersonalInformationEntity;
import accountapi.repository.PersonalInformationRepository;
import dto.accountapi.PersonalInformation;
import dto.accountapi.PersonalInformationRegister;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static accountapi.mapper.PersonalInformationMapper.toDto;
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

        PersonalInformation personalInfo = toDto(entity);

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

        PersonalInformation personalInfo = toDto(entity);

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

        PersonalInformation personalInfo = toDto(savedEntity);

        when(personalInformationRepository.create(any(PersonalInformationEntity.class))).thenReturn(savedEntity);

        PersonalInformation result = personalInformationBusiness.createPersonalInformation(registerDto);

        assertEquals(personalInfo.getFirstname(), result.getFirstname());
        assertEquals(personalInfo.getLastname(), result.getLastname());
    }

    @Test
    void testDeletePersonalInformation() {
        when(personalInformationRepository.delete(100)).thenReturn(true);

        boolean result = personalInformationBusiness.deletePersonalInformation(100);

        assertTrue(result);
        verify(personalInformationRepository).delete(100);
    }
}