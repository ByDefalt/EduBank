package accountapi.business;

import accountapi.entity.PersonalInformationEntity;
import accountapi.mapper.PersonalInformationMapper;
import accountapi.repository.PersonalInformationRepository;
import dto.accountapi.PersonalInformation;
import dto.accountapi.PersonalInformationRegister;
import jakarta.inject.Inject;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class PersonalInformationBusiness {

    @Inject
    private PersonalInformationRepository personalInformationRepository;

    public List<PersonalInformationEntity> getAllPersonalInformation() {
        return personalInformationRepository.findAll();
    }

    public PersonalInformationEntity getPersonalInformationById(Integer id) {
        return personalInformationRepository.findById(id);
    }

    public PersonalInformation createPersonalInformation(PersonalInformationRegister registerDto) {
        PersonalInformationEntity personalInfo = PersonalInformationMapper.toEntity(registerDto);
        return PersonalInformationMapper.toDto(personalInformationRepository.create(personalInfo));
    }

    public boolean deletePersonalInformation(Integer id) {
        return personalInformationRepository.delete(id);
    }
}