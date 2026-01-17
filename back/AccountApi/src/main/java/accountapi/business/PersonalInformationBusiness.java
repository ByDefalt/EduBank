package accountapi.business;

import accountapi.entity.PersonalInformationEntity;
import accountapi.mapper.PersonalInformationMapper;
import accountapi.repository.PersonalInformationRepository;
import dto.accountapi.PersonalInformation;
import dto.accountapi.PersonalInformationRegister;
import jakarta.inject.Inject;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class PersonalInformationBusiness {

    @Inject
    private PersonalInformationRepository personalInformationRepository;

    public List<PersonalInformation> getAllPersonalInformation() {
        List<PersonalInformationEntity> entities = personalInformationRepository.findAll();

        List<PersonalInformation> dtos = new ArrayList<>();
        for (PersonalInformationEntity entity : entities) {
            dtos.add(PersonalInformationMapper.toDto(entity));
        }
        return dtos;
    }

    public PersonalInformation getPersonalInformationById(Integer id) {
        return PersonalInformationMapper.toDto(personalInformationRepository.findById(id));
    }

    public PersonalInformation createPersonalInformation(PersonalInformationRegister registerDto) {
        PersonalInformationEntity personalInfo = PersonalInformationMapper.toEntity(registerDto);
        return PersonalInformationMapper.toDto(personalInformationRepository.create(personalInfo));
    }

    public boolean deletePersonalInformation(Integer id) {
        return personalInformationRepository.delete(id);
    }
}