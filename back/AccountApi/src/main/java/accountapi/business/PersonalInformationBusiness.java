package accountapi.business;

import accountapi.entity.PersonalInformationEntity;
import accountapi.exception.FunctionalException;
import accountapi.exception.NotFoundException;
import accountapi.mapper.PersonalInformationMapper;
import accountapi.repository.PersonalInformationRepository;
import dto.accountapi.PersonalInformation;
import dto.accountapi.PersonalInformationRegister;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class PersonalInformationBusiness {

    private PersonalInformationRepository personalInformationRepository;

    public PersonalInformationBusiness(PersonalInformationRepository personalInformationRepository) {
        this.personalInformationRepository = personalInformationRepository;
    }

    public List<PersonalInformation> getAllPersonalInformation() {
        List<PersonalInformationEntity> entities = personalInformationRepository.findAll();

        List<PersonalInformation> dtos = new ArrayList<>();
        for (PersonalInformationEntity entity : entities) {
            dtos.add(PersonalInformationMapper.toDto(entity));
        }
        return dtos;
    }

    public PersonalInformation getPersonalInformationById(Integer id) {
        PersonalInformationEntity entity = personalInformationRepository.findById(id);
        if (entity == null) {
            throw new NotFoundException("404", "Information personnelle non trouvée avec l'ID : " + id);
        }
        return PersonalInformationMapper.toDto(entity);
    }

    public PersonalInformation createPersonalInformation(PersonalInformationRegister registerDto) {
        PersonalInformationEntity personalInfo = PersonalInformationMapper.toEntity(registerDto);
        PersonalInformationEntity created = personalInformationRepository.create(personalInfo);
        if (created == null) {
            throw new FunctionalException("400", "Impossible de créer les informations personnelles");
        }
        return PersonalInformationMapper.toDto(created);
    }

    public boolean deletePersonalInformation(Integer id) {
        return personalInformationRepository.delete(id);
    }
}