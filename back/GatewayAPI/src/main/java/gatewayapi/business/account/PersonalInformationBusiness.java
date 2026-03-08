package gatewayapi.business.account;

import dto.accountapi.PersonalInformation;
import dto.accountapi.PersonalInformationRegister;
import gatewayapi.repository.account.PersonalInformationRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PersonalInformationBusiness {

    private final PersonalInformationRepository personalInformationRepository;

    public PersonalInformationBusiness(PersonalInformationRepository personalInformationRepository) {
        this.personalInformationRepository = personalInformationRepository;
    }

    public List<PersonalInformation> getAllPersonalInformation() {
        return personalInformationRepository.findAll();
    }

    public PersonalInformation getPersonalInformationById(Integer id) {
        return personalInformationRepository.findById(id);
    }

    public PersonalInformation createPersonalInformation(PersonalInformationRegister personalInformationRegister) {
        return personalInformationRepository.create(personalInformationRegister);
    }

    public PersonalInformation updatePersonalInformation(Integer id, PersonalInformation personalInformation) {
        return personalInformationRepository.update(id, personalInformation);
    }
}
