package accountapi.business;

import accountapi.entity.PersonalInformationEntity;
import accountapi.repository.PersonalInformationRepository;
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

    public PersonalInformationEntity createPersonalInformation(PersonalInformationEntity personalInfo) {
        return personalInformationRepository.create(personalInfo);
    }

    public boolean deletePersonalInformation(Integer id) {
        return personalInformationRepository.delete(id);
    }
}